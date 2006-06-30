/*--
$Id: Common.java,v 1.15 2006-06-30 02:04:41 ddonn Exp $
$Date: 2006-06-30 02:04:41 $

Copyright 2004 Internet2 and Stanford University.  All Rights Reserved.
Licensed under the Signet License, Version 1,
see doc/license.txt in this distribution.
*/
package edu.internet2.middleware.signet.test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.internet2.middleware.signet.Assignment;
import edu.internet2.middleware.signet.AssignmentHistory;
import edu.internet2.middleware.signet.Function;
import edu.internet2.middleware.signet.Grantable;
import edu.internet2.middleware.signet.Limit;
import edu.internet2.middleware.signet.LimitValue;
import edu.internet2.middleware.signet.ObjectNotFoundException;
import edu.internet2.middleware.signet.Privilege;
import edu.internet2.middleware.signet.PrivilegedSubject;
import edu.internet2.middleware.signet.Proxy;
import edu.internet2.middleware.signet.Signet;
import edu.internet2.middleware.signet.SignetAuthorityException;
import edu.internet2.middleware.signet.Status;
import edu.internet2.middleware.signet.Subsystem;
import edu.internet2.middleware.signet.tree.Tree;
import edu.internet2.middleware.signet.tree.TreeNode;
import edu.internet2.middleware.subject.Subject;

import junit.framework.TestCase;

/**
 * @author Andy Cohen
 *
 */
public class Common extends TestCase
{
  public static Object getSingleSetMember(Set set)
  {
    assertEquals(1, set.size());

    Object obj = null;
    Iterator setIterator = set.iterator();
    while (setIterator.hasNext())
    {
      obj = setIterator.next();
    }
    
    return obj;
  }
  
  public static Object getFirstSetMember(Set set)
  {
    assertTrue(0 < set.size());

    Object obj = null;
    Iterator setIterator = set.iterator();
    setIterator.hasNext();
    obj = setIterator.next();
    
    return obj;
  }
  
  static LimitValue[] getLimitValuesArray(Assignment assignment)
  {
    LimitValue limitValuesArray[] = new LimitValue[0];

    return
      (LimitValue[])(assignment.getLimitValues().toArray(limitValuesArray));
  }
  
  static LimitValue[] getLimitValuesArray(AssignmentHistory assignmentHistory)
  {
    LimitValue limitValuesArray[] = new LimitValue[0];
    Set limitValues = assignmentHistory.getLimitValues();
    assertNotNull(limitValues);
    limitValuesArray = (LimitValue[])(limitValues.toArray(limitValuesArray));

    return limitValuesArray;
  }

  static LimitValue[] getLimitValuesInDisplayOrder
    (Assignment assignment)
  {
    LimitValue[] limitValues = getLimitValuesArray(assignment);
    Arrays.sort(limitValues);
    
    return limitValues;
  }

  static LimitValue[] getLimitValuesInDisplayOrder
    (AssignmentHistory assignmentHistory)
  {
    LimitValue[] limitValues = getLimitValuesArray(assignmentHistory);
    Arrays.sort(limitValues);
    
    return limitValues;
  }

  static LimitValue[] getLimitValuesInDisplayOrder
    (Privilege privilege)
  {
    LimitValue[] limitValues = new LimitValue[0];
    limitValues
      = (LimitValue[])(privilege.getLimitValues().toArray(limitValues));
    Arrays.sort(limitValues);
    return limitValues;
  }
  
  static Limit[] getLimitsInDisplayOrder(Set limits)
  {
    Limit[] limitsArray = new Limit[0];
    limitsArray = (Limit[])(limits.toArray(limitsArray));
    
    if (limitsArray.length > 0)
    {
      Arrays.sort(limitsArray);
    }
    
    return limitsArray;
  }
  
  static Date getDate(int daysOffset)
  {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, daysOffset);
    
    // Some database-column-types we use (like Sybase's smalldatetime) store
    // time only to the nearest second.
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.set(Calendar.SECOND, 0);
    
    return calendar.getTime();
  }

  /**
   * @param assignment
   * @return
   * @throws SignetAuthorityException
   */
  public static PrivilegedSubject getOriginalGrantor(Grantable grantable)
  throws SignetAuthorityException
  {
    PrivilegedSubject originalGrantor;
    
    if (grantable.getProxy() == null)
    {
      originalGrantor = grantable.getGrantor();
    }
    else
    {
      originalGrantor = grantable.getProxy();
      originalGrantor.setActingAs(grantable.getGrantor());
    }
    
    return originalGrantor;
  }
  
  public static Set filterProxies(Set all, Status status)
  {
    Set statusSet = new HashSet();
    statusSet.add(status);
    return filterProxies(all, statusSet);
  }
  
  public static Set filterAssignments(Set all, Status status)
  {
    Set statusSet = new HashSet();
    statusSet.add(status);
    return filterAssignments(all, statusSet);
  }

  public static Set filterAssignments(Set all, Set statusSet)
  {
    if (statusSet == null)
    {
      return all;
    }

    Set subset = new HashSet();
    Iterator iterator = all.iterator();
    while (iterator.hasNext())
    {
      Assignment candidate = (Assignment) (iterator.next());
      if (statusSet.contains(candidate.getStatus()))
      {
        subset.add(candidate);
      }
    }

    return subset;
  }

  public static Set filterAssignments(Set all, Subsystem subsystem)
  {
    if (subsystem == null)
    {
      return all;
    }

    Set subset = new HashSet();
    Iterator iterator = all.iterator();
    while (iterator.hasNext())
    {
      Assignment candidate = (Assignment) (iterator.next());
      if (candidate.getFunction().getSubsystem().equals(subsystem))
      {
        subset.add(candidate);
      }
    }

    return subset;
  }

  public static Set filterProxies(Set all, Set statusSet)
  {
    if (statusSet == null)
    {
      return all;
    }

    Set subset = new HashSet();
    Iterator iterator = all.iterator();
    while (iterator.hasNext())
    {
      Proxy candidate = (Proxy) (iterator.next());
      if (statusSet.contains(candidate.getStatus()))
      {
        subset.add(candidate);
      }
    }

    return subset;
  }

  public static Set filterProxies(Set all, Subsystem subsystem)
  {
    if (subsystem == null)
    {
      return all;
    }

    Set subset = new HashSet();
    Iterator iterator = all.iterator();
    while (iterator.hasNext())
    {
      Proxy candidate = (Proxy) (iterator.next());
      if ((candidate.getSubsystem() == null)
          || candidate.getSubsystem().equals(subsystem))
      {
        subset.add(candidate);
      }
    }

    return subset;
  }
  
  static Set filterProxiesByGrantor
    (Set                all,
     PrivilegedSubject  grantor)
  {
    if (grantor == null)
    {
      return all;
    }
    
    Set subset = new HashSet();
    Iterator iterator = all.iterator();
    while (iterator.hasNext())
    {
      Proxy candidate = (Proxy)(iterator.next());
      if (candidate.getGrantor().equals(grantor))
      {
        subset.add(candidate);
      }
    }
    
    return subset;
  }

  static Set filterAssignments(Set all, Function function)
  {
    if (function == null)
    {
      return all;
    }

    Set subset = new HashSet();
    Iterator iterator = all.iterator();
    while (iterator.hasNext())
    {
      Assignment candidate = (Assignment) (iterator.next());
      if (candidate.getFunction().equals(function))
      {
        subset.add(candidate);
      }
    }

    return subset;
  }
  
  public static TreeNode getRootNode(Signet signet)
  throws ObjectNotFoundException
  {
    Tree tree = signet.getPersistentDB().getTree(Constants.TREE_ID);
    Set roots = tree.getRoots();
    TreeNode root = (TreeNode)(Common.getSingleSetMember(roots));
    
    return root;
  }
  
  public static PrivilegedSubject getPrivilegedSubject
    (Signet signet,
     int    subjectIndex)
  throws ObjectNotFoundException
  {
    Subject subject = getSubject(signet, subjectIndex);
    PrivilegedSubject pSubject = signet.getSubjectSources().getPrivilegedSubject(subject);
    return pSubject;
  }


  /**
   * @param i
   * @return
   * @throws ObjectNotFoundException
   * @throws ObjectNotFoundException
   */
  public static Subject getSubject(Signet signet, int subjectNumber)
  throws ObjectNotFoundException
  {
    Subject subject = null;

    subject
      = signet.getSubjectSources().getSubject
          (Signet.DEFAULT_SUBJECT_TYPE_ID,
           makeSubjectId(subjectNumber));
    
    return subject;
  }

  /**
   * @param subjectNumber
   * @return
   */
  public static String makeSubjectId(int subjectNumber)
  {
    return
      "SUBJECT"
      + Constants.DELIMITER
      + subjectNumber
      + Constants.DELIMITER
      + "ID";
  }

  public static Function getFunction(Signet signet, int functionNumber)
  throws ObjectNotFoundException
  {
    Subsystem subsystem = signet.getPersistentDB().getSubsystem(Constants.SUBSYSTEM_ID);
    String functionId = makeFunctionId(functionNumber);
    Function function = getFunction(subsystem, functionId);
    
    return function;
  }
  
  public static String makeFunctionId(int functionNumber)
  {
    return
      "FUNCTION"
      + Constants.DELIMITER
      + functionNumber
      + Constants.DELIMITER
      + "ID";
  }

  public static Function getFunction
    (Subsystem subsystem,
     String    functionId) throws ObjectNotFoundException
  {
    Iterator functionsIterator = subsystem.getFunctions().iterator();
    while (functionsIterator.hasNext())
    {
      Function candidate = (Function)(functionsIterator.next());
      if (candidate.getId().equals(functionId))
      {
        return candidate;
      }
    }
    
    throw new ObjectNotFoundException
      ("Unable to find function with ID '"
       + functionId
       + "' in subsystem '"
       + subsystem + "'");
  }
}
