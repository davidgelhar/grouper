/**
 * Copyright 2016 Internet2
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.internet2.middleware.grouper.misc;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.sql.Timestamp;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import bsh.Interpreter;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.app.loader.GrouperDaemonUtils;
import edu.internet2.middleware.grouper.app.loader.GrouperLoader;
import edu.internet2.middleware.grouper.app.loader.GrouperLoaderStatus;
import edu.internet2.middleware.grouper.app.loader.db.Hib3GrouperLoaderLog;
import edu.internet2.middleware.grouper.audit.GrouperEngineBuiltin;
import edu.internet2.middleware.grouper.exception.GrouperSessionException;
import edu.internet2.middleware.grouper.hibernate.GrouperContext;
import edu.internet2.middleware.grouper.util.GrouperUtil;

/**
 * @author shilen
 */
@DisallowConcurrentExecution
public class FindBadMembershipsDaemon implements Job {
  
  private static final Log LOG = GrouperUtil.getLog(FindBadMembershipsDaemon.class);
  
  /**
   * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
   */
  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    long startTime = System.currentTimeMillis();
    Hib3GrouperLoaderLog hib3GrouploaderLog = new Hib3GrouperLoaderLog();

    try {
      GrouperSession.internal_callbackRootGrouperSession(new GrouperSessionHandler() {
  
        @Override
        public Object callback(GrouperSession grouperSession) throws GrouperSessionException {
          try {
      GrouperContext.createNewDefaultContext(GrouperEngineBuiltin.LOADER, false, true);

      String jobName = context.getJobDetail().getKey().getName();

      if (GrouperLoader.isJobRunning(jobName, true)) {
        LOG.warn("Data in grouper_loader_log suggests that job " + jobName + " is currently running already.  Aborting this run.");
              return null;
      }
      
      hib3GrouploaderLog.setJobName(jobName);
      hib3GrouploaderLog.setHost(GrouperUtil.hostname());
      hib3GrouploaderLog.setStartedTime(new Timestamp(startTime));
      hib3GrouploaderLog.setJobType("OTHER_JOB");
      hib3GrouploaderLog.setStatus(GrouperLoaderStatus.STARTED.name());
      hib3GrouploaderLog.store();
      
      GrouperDaemonUtils.setThreadLocalHib3GrouperLoaderLogOverall(hib3GrouploaderLog);
      
      int runs = 0;
      
      while (true) {
        GrouperDaemonUtils.stopProcessingIfJobPaused();
        runs++;
        LOG.info("Checking for bad or missing memberships.");
        FindBadMemberships.clearResults();
        long count = FindBadMemberships.checkAll();
        LOG.info("Found " + count + " bad or missing memberships. ");
        hib3GrouploaderLog.appendJobMessage("Found " + count + " bad or missing memberships. ");
        if (count == 0) {
          break;
        }
        
        if (runs >= 4) {
          throw new RuntimeException("Tried to fix memberships 3 times and still have issues.");
        }
        
        hib3GrouploaderLog.appendJobMessage("Fixing and will try again. ");

        LOG.warn("Running script to fix " + count + " memberships:\n" + FindBadMemberships.gshScript.toString());
        String gsh = "importCommands(\"edu.internet2.middleware.grouper.app.gsh\");\nimport edu.internet2.middleware.grouper.*;\nimport edu.internet2.middleware.grouper.misc.*;\n" + FindBadMemberships.gshScript.toString();
       
        PrintStream psOut = null;
        PrintStream psErr = null;
        
        try {
          ByteArrayOutputStream baosOut = new ByteArrayOutputStream();
          psOut = new PrintStream(baosOut);
          
          ByteArrayOutputStream baosErr = new ByteArrayOutputStream();
          psErr = new PrintStream(baosErr);
          
          new Interpreter(new StringReader(gsh), psOut, psErr, false).run();
          LOG.warn("Done running script to fix " + count + " memberships:  Standard out=" + new String(baosOut.toByteArray(), "UTF-8") + ", Standard err=" + new String(baosErr.toByteArray(), "UTF-8"));
        } finally {
          if (psOut != null) {
            try {
              psOut.close();
            } catch (Exception e) {
              // ignore
            }
            
            hib3GrouploaderLog.setJobName(jobName);
            hib3GrouploaderLog.setHost(GrouperUtil.hostname());
            hib3GrouploaderLog.setStartedTime(new Timestamp(startTime));
            hib3GrouploaderLog.setJobType("OTHER_JOB");
            hib3GrouploaderLog.setStatus(GrouperLoaderStatus.STARTED.name());
            hib3GrouploaderLog.store();
            
            int runs = 0;
            
            while (true) {
              runs++;
              LOG.info("Checking for bad or missing memberships.");
              FindBadMemberships.clearResults();
              long count = FindBadMemberships.checkAll();
              LOG.info("Found " + count + " bad or missing memberships. ");
              hib3GrouploaderLog.appendJobMessage("Found " + count + " bad or missing memberships. ");
              if (count == 0) {
                break;
              }
              
              if (runs >= 4) {
                throw new RuntimeException("Tried to fix memberships 3 times and still have issues.");
              }
              
              hib3GrouploaderLog.appendJobMessage("Fixing and will try again. ");
  
              LOG.warn("Running script to fix " + count + " memberships:\n" + FindBadMemberships.gshScript.toString());
              String gsh = "importCommands(\"edu.internet2.middleware.grouper.app.gsh\");\nimport edu.internet2.middleware.grouper.*;\nimport edu.internet2.middleware.grouper.misc.*;\n" + FindBadMemberships.gshScript.toString();
             
              PrintStream psOut = null;
              PrintStream psErr = null;
              
              try {
                ByteArrayOutputStream baosOut = new ByteArrayOutputStream();
                psOut = new PrintStream(baosOut);
                
                ByteArrayOutputStream baosErr = new ByteArrayOutputStream();
                psErr = new PrintStream(baosErr);
                
                new Interpreter(new StringReader(gsh), psOut, psErr, false).run();
                LOG.warn("Done running script to fix " + count + " memberships:  Standard out=" + new String(baosOut.toByteArray(), "UTF-8") + ", Standard err=" + new String(baosErr.toByteArray(), "UTF-8"));
              } finally {
                if (psOut != null) {
                  try {
                    psOut.close();
                  } catch (Exception e) {
                    // ignore
                  }
                }
                
                if (psErr != null) {
                  try {
                    psErr.close();
                  } catch (Exception e) {
                    // ignore
                  }
                }
              }
            }
            
            hib3GrouploaderLog.setStatus(GrouperLoaderStatus.SUCCESS.name());
            storeLogInDb(hib3GrouploaderLog, true, startTime);
          } catch (Throwable e) {
            LOG.error("Error running job", e);
            hib3GrouploaderLog.setStatus(GrouperLoaderStatus.ERROR.name());
            hib3GrouploaderLog.appendJobMessage(ExceptionUtils.getFullStackTrace(e));
            
            if (!(e instanceof JobExecutionException)) {
              e = new JobExecutionException(e);
            }
            JobExecutionException jobExecutionException = (JobExecutionException)e;
            storeLogInDb(hib3GrouploaderLog, false, startTime);
            throw new RuntimeException(e);
          } finally {
            FindBadMemberships.clearResults();
          }
          return null;
        }
      });
    } catch (RuntimeException re) {
      if (re.getCause() instanceof JobExecutionException) {
        throw (JobExecutionException)re.getCause();
      }
      
      hib3GrouploaderLog.setStatus(GrouperLoaderStatus.SUCCESS.name());
      storeLogInDb(hib3GrouploaderLog, true, startTime);
    } catch (Throwable e) {
      LOG.error("Error running job", e);
      hib3GrouploaderLog.setStatus(GrouperLoaderStatus.ERROR.name());
      hib3GrouploaderLog.appendJobMessage(ExceptionUtils.getFullStackTrace(e));
      
      if (!(e instanceof JobExecutionException)) {
        e = new JobExecutionException(e);
      }
      JobExecutionException jobExecutionException = (JobExecutionException)e;
      storeLogInDb(hib3GrouploaderLog, false, startTime);
      throw jobExecutionException;
    } finally {
      GrouperDaemonUtils.clearThreadLocalHib3GrouperLoaderLogOverall();

      FindBadMemberships.clearResults();
      GrouperSession.stopQuietly(grouperSession);
    }
  }
  
  /**
   * @param hib3GrouploaderLog
   * @param throwException 
   * @param startTime
   */
  private static void storeLogInDb(Hib3GrouperLoaderLog hib3GrouploaderLog,
      boolean throwException, long startTime) {
    //store this safely
    try {
      
      long endTime = System.currentTimeMillis();
      hib3GrouploaderLog.setEndedTime(new Timestamp(endTime));
      hib3GrouploaderLog.setMillis((int)(endTime-startTime));
      
      hib3GrouploaderLog.store();
      
    } catch (RuntimeException e) {
      LOG.error("Problem storing final log", e);
      //dont preempt an existing exception
      if (throwException) {
        throw e;
      }
    }
  }
  
}
