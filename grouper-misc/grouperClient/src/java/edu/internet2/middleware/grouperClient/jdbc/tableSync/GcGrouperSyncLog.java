package edu.internet2.middleware.grouperClient.jdbc.tableSync;

import java.net.InetAddress;
import java.sql.Timestamp;

import edu.internet2.middleware.grouperClient.jdbc.GcDbAccess;
import edu.internet2.middleware.grouperClient.jdbc.GcDbVersionable;
import edu.internet2.middleware.grouperClient.jdbc.GcPersist;
import edu.internet2.middleware.grouperClient.jdbc.GcPersistableClass;
import edu.internet2.middleware.grouperClient.jdbc.GcPersistableField;
import edu.internet2.middleware.grouperClient.jdbc.GcPersistableHelper;
import edu.internet2.middleware.grouperClient.jdbc.GcSqlAssignPrimaryKey;
import edu.internet2.middleware.grouperClient.util.GrouperClientUtils;
import edu.internet2.middleware.grouperClientExt.org.apache.commons.lang3.RandomStringUtils;
import edu.internet2.middleware.grouperClientExt.org.apache.commons.lang3.builder.EqualsBuilder;
import edu.internet2.middleware.grouperClientExt.org.apache.commons.logging.Log;

/**
 * last log for this sync that affected this group/member/membership/job
 * @author mchyzer
 *
 */
@GcPersistableClass(tableName="grouper_sync_log", defaultFieldPersist=GcPersist.doPersist)
public class GcGrouperSyncLog implements GcSqlAssignPrimaryKey, GcDbVersionable {


  //########## START GENERATED BY GcDbVersionableGenerate.java ###########
  /** save the state when retrieving from DB */
  @GcPersistableField(persist = GcPersist.dontPersist)
  private GcGrouperSyncLog dbVersion = null;

  /**
   * take a snapshot of the data since this is what is in the db
   */
  @Override
  public void dbVersionReset() {
    //lets get the state from the db so we know what has changed
    this.dbVersion = this.clone();
  }

  /**
   * if we need to update this object
   * @return if needs to update this object
   */
  @Override
  public boolean dbVersionDifferent() {
    return !this.equalsDeep(this.dbVersion);
  }

  /**
   * db version
   */
  @Override
  public void dbVersionDelete() {
    this.dbVersion = null;
  }

  /**
   * deep clone the fields in this object
   */
  @Override
  public GcGrouperSyncLog clone() {

    GcGrouperSyncLog gcGrouperSyncLog = new GcGrouperSyncLog();
    //connectionName  DONT CLONE
  
    //dbVersion  DONT CLONE
  
    gcGrouperSyncLog.description = this.description;
    //grouperSyncGroup  DONT CLONE
  
    gcGrouperSyncLog.grouperSyncId = this.grouperSyncId;
    //grouperSyncJob  DONT CLONE
  
    gcGrouperSyncLog.grouperSyncOwnerId = this.grouperSyncOwnerId;
    gcGrouperSyncLog.id = this.id;
    gcGrouperSyncLog.jobTookMillis = this.jobTookMillis;
    //lastUpdated  DONT CLONE
  
    gcGrouperSyncLog.recordsChanged = this.recordsChanged;
    gcGrouperSyncLog.recordsProcessed = this.recordsProcessed;
    gcGrouperSyncLog.server = this.server;
    gcGrouperSyncLog.statusDb = this.statusDb;
    gcGrouperSyncLog.syncTimestampStart = this.syncTimestampStart;
    gcGrouperSyncLog.syncTimestamp = this.syncTimestamp;

    return gcGrouperSyncLog;
  }

  /**
   *
   */
  public boolean equalsDeep(Object obj) {
    if (this==obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof GcGrouperSyncLog)) {
      return false;
    }
    GcGrouperSyncLog other = (GcGrouperSyncLog) obj;

    return new EqualsBuilder()


      //connectionName  DONT EQUALS

      //dbVersion  DONT EQUALS

      .append(this.getDescriptionOrDescriptionClob(), other.getDescriptionOrDescriptionClob())
      //grouperSyncGroup  DONT EQUALS

      .append(this.grouperSyncId, other.grouperSyncId)
      .append(this.grouperSyncOwnerId, other.grouperSyncOwnerId)
      .append(this.id, other.id)
      .append(this.jobTookMillis, other.jobTookMillis)
      //lastUpdated  DONT EQUALS

      .append(this.recordsChanged, other.recordsChanged)
      .append(this.recordsProcessed, other.recordsProcessed)
      .append(this.server, other.server)
      .append(this.statusDb, other.statusDb)
      .append(this.syncTimestampStart, other.syncTimestampStart)
      .append(this.syncTimestamp, other.syncTimestamp)
        .isEquals();

  }
  //########## END GENERATED BY GcDbVersionableGenerate.java ###########

  /**
   * delete all data if table is here
   */
  public static void reset() {
    
    new GcDbAccess().connectionName("grouper").sql("delete from " + GcPersistableHelper.tableName(GcGrouperSyncLog.class)).executeSql();
  }

  /**
   * 
   * @param args
   */
  public static void main(String[] args) {
    
    System.out.println("none");
    
    for (GcGrouperSyncLog theGcGrouperSyncLog : new GcDbAccess().connectionName("grouper").selectList(GcGrouperSyncLog.class)) {
      System.out.println(theGcGrouperSyncLog.toString());
    }
    
    // foreign key
    GcGrouperSync gcGrouperSync = new GcGrouperSync();
    gcGrouperSync.setSyncEngine("temp");
    gcGrouperSync.setProvisionerName("myJob");
    gcGrouperSync.getGcGrouperSyncDao().store();
    
    GcGrouperSyncJob gcGrouperSyncJob = new GcGrouperSyncJob();
    gcGrouperSyncJob.setGrouperSync(gcGrouperSync);
    gcGrouperSyncJob.setJobState(GcGrouperSyncJobState.notRunning);
    gcGrouperSyncJob.setLastSyncIndex(135L);
    gcGrouperSyncJob.setLastTimeWorkWasDone(new Timestamp(System.currentTimeMillis() + 2000));
    gcGrouperSyncJob.setSyncType("testSyncType");
    gcGrouperSync.getGcGrouperSyncJobDao().internal_jobStore(gcGrouperSyncJob);

    GcGrouperSyncGroup gcGrouperSyncGroup = gcGrouperSync.getGcGrouperSyncGroupDao().groupRetrieveOrCreateByGroupId("groupId");
    gcGrouperSyncGroup.setLastTimeWorkWasDone(new Timestamp(System.currentTimeMillis() + 2000));
    gcGrouperSync.getGcGrouperSyncGroupDao().internal_groupStore(gcGrouperSyncGroup);

    GcGrouperSyncMember gcGrouperSyncMember = gcGrouperSync.getGcGrouperSyncMemberDao().memberRetrieveOrCreateByMemberId("memberId");
    gcGrouperSyncMember.setLastTimeWorkWasDone(new Timestamp(System.currentTimeMillis() + 4000));
    gcGrouperSync.getGcGrouperSyncMemberDao().internal_memberStore(gcGrouperSyncMember);
    
    GcGrouperSyncMembership gcGrouperSyncMembership = gcGrouperSync.getGcGrouperSyncMembershipDao().membershipRetrieveById("membershipId");
    gcGrouperSync.getGcGrouperSyncMembershipDao().internal_membershipStore(gcGrouperSyncMembership);
    
    GcGrouperSyncLog gcGrouperSyncLog = new GcGrouperSyncLog();
    gcGrouperSyncLog.setDescriptionToSave("desc");
    gcGrouperSyncLog.jobTookMillis = 1223;
    gcGrouperSyncLog.recordsChanged = 12;
    gcGrouperSyncLog.recordsProcessed = 23;
    gcGrouperSync.getGcGrouperSyncLogDao().internal_logStore(gcGrouperSyncLog);
    
    System.out.println("stored");

    System.out.println(gcGrouperSyncLog);
    
    String longDescription = RandomStringUtils.random(4500, true, true);
    gcGrouperSyncLog.setDescriptionToSave(longDescription);
    gcGrouperSync.getGcGrouperSyncLogDao().internal_logStore(gcGrouperSyncLog);

    System.out.println("updated");

    for (GcGrouperSyncLog theGcGrouperSyncLog : new GcDbAccess().connectionName("grouper").selectList(GcGrouperSyncLog.class)) {
      System.out.println(theGcGrouperSyncLog.toString());
    }

//    gcGrouperSync.getGcGrouperSyncLogDao().logDelete(gcGrouperSyncLog);
//    gcGrouperSync.getGcGrouperSyncGroupDao().groupDelete(gcGrouperSyncGroup, false, false);
//    gcGrouperSync.getGcGrouperSyncJobDao().jobDelete(gcGrouperSyncJob, false);
//    gcGrouperSync.getGcGrouperSyncDao().delete();
    
    System.out.println("deleted");

    for (GcGrouperSyncGroup theGcGrouperSyncStatus : new GcDbAccess().connectionName("grouper").selectList(GcGrouperSyncGroup.class)) {
      System.out.println(theGcGrouperSyncStatus.toString());
    }
  }

  /**
   * 
   */
  public GcGrouperSyncLog() {
  }

  /**
   * when this record was last updated
   */
  private Timestamp lastUpdated;


  /**
   * when this record was last updated
   * @return when last updated
   */
  public Timestamp getLastUpdated() {
    return this.lastUpdated;
  }

  /**
   * when this record was last updated
   * @param lastUpdated1
   */
  public void setLastUpdated(Timestamp lastUpdated1) {
    this.lastUpdated = lastUpdated1;
  }
  
  /**
   * uuid of this record in this table
   */
  @GcPersistableField(primaryKey=true, primaryKeyManuallyAssigned=false)
  private String id;

  
  /**
   * uuid of this record in this table
   * @return id
   */
  public String getId() {
    return this.id;
  }

  /**
   * uuid of this record in this table
   * @param id1
   */
  public void setId(String id1) {
    this.id = id1;
  }
  
  /**
   * foreign key to grouper_sync_group table
   */
  private String grouperSyncId;


  /**
   * foreign key to grouper_sync_group table
   * @return group id
   */
  public String getGrouperSyncId() {
    return this.grouperSyncId;
  }

  /**
   * foreign key to grouper_sync_group table
   * @param grouperSyncId1
   */
  public void setGrouperSyncId(String grouperSyncId1) {
    this.grouperSyncId = grouperSyncId1;
    if (this.grouperSync == null || !GrouperClientUtils.equals(grouperSyncId1, this.grouperSync.getId())) {
      this.grouperSync = null;
    }
  }
  
  /**
   * either the grouper_sync_member_id or the grouper_sync_group_id or grouper_sync_job_id (if log for job wide)
   */
  private String grouperSyncOwnerId;


  /**
   * either the grouper_sync_member_id or the grouper_sync_group_id or grouper_sync_job_id (if log for job wide)
   * @return owner id
   */
  public String getGrouperSyncOwnerId() {
    return this.grouperSyncOwnerId;
  }

  /**
   * either the grouper_sync_member_id or the grouper_sync_group_id or grouper_sync_job_id (if log for job wide)
   * @param grouperSyncOwnerId1
   */
  public void setGrouperSyncOwnerId(String grouperSyncOwnerId1) {
    this.grouperSyncOwnerId = grouperSyncOwnerId1;
  }
  
  /**
   * SUCCESS, ERROR, WARNING, CONFIG_ERROR
   */
  @GcPersistableField(columnName="status")
  private String statusDb;

  /**
   * SUCCESS, ERROR, WARNING, CONFIG_ERROR
   * @return status
   */
  public String getStatusDb() {
    return this.statusDb;
  }

  /**
   * SUCCESS, ERROR, WARNING, CONFIG_ERROR
   * @param status1
   */
  public void setStatusDb(String status1) {
    this.statusDb = status1;
  }
  
  /**
   * 
   * @return the state or null if not there
   */
  public GcGrouperSyncLogState getStatus() {
    return GcGrouperSyncLogState.valueOfIgnoreCase(this.statusDb);
  }
  
  /**
   * 
   * @param gcGrouperSyncLogState
   */
  public void setStatus(GcGrouperSyncLogState gcGrouperSyncLogState) {
    this.statusDb = gcGrouperSyncLogState == null ? null : gcGrouperSyncLogState.name();
  }

  /**
   * when the last sync ended
   */
  private Timestamp syncTimestamp;

  /**
   * when the last sync started
   */
  private Timestamp syncTimestampStart;


  /**
   * when the last sync started
   * @return
   */
  public Timestamp getSyncTimestampStart() {
    return syncTimestampStart;
  }

  /**
   * when the last sync started
   * @param syncTimestampStart1
   */
  public void setSyncTimestampStart(Timestamp syncTimestampStart1) {
    this.syncTimestampStart = syncTimestampStart1;
  }

  /**
   * when the last sync ended
   * @return timestamp
   */
  public Timestamp getSyncTimestamp() {
    return this.syncTimestamp;
  }

  /**
   * when the last sync ended
   * @param syncTimestamp1
   */
  public void setSyncTimestamp(Timestamp syncTimestamp1) {
    this.syncTimestamp = syncTimestamp1;
  }

  /**
   * description of last sync
   */
  private String description;


  /**
   * description of last sync
   * @return description
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * description of last sync
   * @param description1
   */
  public void setDescription(String description1) {
    this.description = description1;
  }
  
  /**
   * description of last sync when it's too large to store in description field
   */
  private String descriptionClob;
  
  /**
   * description of last sync when it's too large to store in description field
   * @return descriptionClob
   */
  public String getDescriptionClob() {
    return descriptionClob;
  }

  /**
   * description of last sync when it's too large to store in description field
   * @param descriptionClob
   */
  public void setDescriptionClob(String descriptionClob) {
    this.descriptionClob = descriptionClob;
  }
  
  /**
   * size of description/descriptionClob in bytes
   */
  private Long descriptionBytes;
  
  /**
   * size of description/descriptionClob in bytes
   * @return  descriptionBytes
   */
  public Long getDescriptionBytes() {
    return descriptionBytes;
  }
  
  /**
   * size of description/descriptionClob in bytes
   * @param descriptionBytes
   */
  public void setDescriptionBytes(Long descriptionBytes) {
    this.descriptionBytes = descriptionBytes;
  }

  
  /**
   * retrieve description. based on the size, it will be retrieved from description or descriptionClob
   * @return
   */
  public String getDescriptionOrDescriptionClob() {
    
    if (GrouperClientUtils.isNotBlank(description)) {
      return description;
    }
    
    return descriptionClob;
    
  }
  
  /**
   * set description to save. based on the size, it will be saved in description or descriptionClob
   * @param description
   */
  public void setDescriptionToSave(String description) {
    if (description != null) {
      description = description.replaceAll("\u0000", "");
    }
    int lengthAscii = GrouperClientUtils.lengthAscii(description);
    if (GrouperClientUtils.lengthAscii(description) <= 3000) {
      this.description = description;
      this.descriptionClob = null;
    } else {
      this.descriptionClob = description;
      this.description = null;
    }
    this.descriptionBytes = new Long(lengthAscii);
  }
  
  
  
  /**
   * how many records were processed the last time this sync ran
   */
  private Integer recordsProcessed;


  /**
   * how many records were processed the last time this sync ran
   * @return records processed
   */
  public Integer getRecordsProcessed() {
    return this.recordsProcessed;
  }

  /**
   * how many records were processed the last time this sync ran
   * @param recordsProcessed1
   */
  public void setRecordsProcessed(Integer recordsProcessed1) {
    this.recordsProcessed = recordsProcessed1;
  }
  
  /**
   * how many records were changed the last time this sync ran
   */
  private Integer recordsChanged;


  /**
   * how many records were changed the last time this sync ran
   * @return records changed
   */
  public Integer getRecordsChanged() {
    return this.recordsChanged;
  }

  /**
   * how many records were changed the last time this sync ran
   * @param recordsChanged1
   */
  public void setRecordsChanged(Integer recordsChanged1) {
    this.recordsChanged = recordsChanged1;
  }
  
  /**
   * how many millis it took to run this job
   */
  private Integer jobTookMillis;


  /**
   * how many millis it took to run this job
   * @return job millis
   */
  public Integer getJobTookMillis() {
    return this.jobTookMillis;
  }

  /**
   * how many millis it took to run this job
   * @param jobTookMillis
   */
  public void setJobTookMillis(Integer jobTookMillis) {
    this.jobTookMillis = jobTookMillis;
  }
  
  /**
   * which server this occurred on
   */
  private String server;
  
  
  
  /**
   * which server this occurred on
   * @return server
   */
  public String getServer() {
    return this.server;
  }

  /**
   * which server this occurred on
   * @param server1
   */
  public void setServer(String server1) {
    this.server = server1;
  }

  /**
   * connection name or null for default
   */
  @GcPersistableField(persist=GcPersist.dontPersist)
  private String connectionName;

  /**
   * 
   */
  private static Log LOG = GrouperClientUtils.retrieveLog(GcGrouperSyncLog.class);

  /**
   * connection name or null for default
   * @return connection name
   */
  public String getConnectionName() {
    return this.connectionName;
  }

  /**
   * connection name or null for default
   * @param connectionName1
   */
  public void setConnectionName(String connectionName1) {
    this.connectionName = connectionName1;
  }
  
  /**
   * 
   */
  @Override
  public boolean gcSqlAssignNewPrimaryKeyForInsert() {
    if (this.id != null) {
      return false;
    }
    this.id = GrouperClientUtils.uuid();
    return true;
  }

  /**
   * prepare to store
   */
  public void storePrepare() {
    try {
      this.server = InetAddress.getLocalHost().getHostName();
    } catch (Exception e) {
      //dont worry about it
      LOG.info(e);
    }
    this.lastUpdated = new Timestamp(System.currentTimeMillis());
    this.connectionName = GcGrouperSync.defaultConnectionName(this.connectionName);
    if (description != null) {
      description = description.replaceAll("\u0000", "");
    }
    if (descriptionClob != null) {
      descriptionClob = descriptionClob.replaceAll("\u0000", "");
    }

  }

  /**
   * 
   */
  @Override
  public String toString() {
    return GrouperClientUtils.toStringReflection(this);
  }

  /**
   * 
   */
  @GcPersistableField(persist=GcPersist.dontPersist)
  private GcGrouperSync grouperSync;
  
  /**
   * 
   * @return gc grouper sync
   */
  public GcGrouperSync getGrouperSync() {
    return this.grouperSync;
  }
  
  /**
   * 
   * @param gcGrouperSync
   */
  public void setGrouperSync(GcGrouperSync gcGrouperSync) {
    this.grouperSync = gcGrouperSync;
    this.grouperSyncId = gcGrouperSync == null ? null : gcGrouperSync.getId();
    this.connectionName = gcGrouperSync == null ? this.connectionName : gcGrouperSync.getConnectionName();

  }

}
