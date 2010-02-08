/**
 * @author mchyzer
 * $Id$
 */
package edu.internet2.middleware.grouper.xml.importXml;

import java.io.File;
import java.util.Set;

import junit.textui.TestRunner;
import edu.internet2.middleware.grouper.Field;
import edu.internet2.middleware.grouper.FieldFinder;
import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupFinder;
import edu.internet2.middleware.grouper.GroupType;
import edu.internet2.middleware.grouper.GroupTypeFinder;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.Member;
import edu.internet2.middleware.grouper.MemberFinder;
import edu.internet2.middleware.grouper.Stem;
import edu.internet2.middleware.grouper.StemFinder;
import edu.internet2.middleware.grouper.attr.AttributeDef;
import edu.internet2.middleware.grouper.attr.AttributeDefName;
import edu.internet2.middleware.grouper.attr.AttributeDefScope;
import edu.internet2.middleware.grouper.attr.AttributeDefType;
import edu.internet2.middleware.grouper.attr.assign.AttributeAssignAction;
import edu.internet2.middleware.grouper.attr.finder.AttributeDefFinder;
import edu.internet2.middleware.grouper.attr.finder.AttributeDefNameFinder;
import edu.internet2.middleware.grouper.audit.AuditEntry;
import edu.internet2.middleware.grouper.audit.AuditType;
import edu.internet2.middleware.grouper.audit.AuditTypeBuiltin;
import edu.internet2.middleware.grouper.helper.GrouperTest;
import edu.internet2.middleware.grouper.hibernate.HibernateSession;
import edu.internet2.middleware.grouper.misc.GrouperCheckConfig;
import edu.internet2.middleware.grouper.misc.GrouperDAOFactory;
import edu.internet2.middleware.grouper.permissions.role.Role;
import edu.internet2.middleware.grouper.util.GrouperUtil;


/**
 *
 */
public class XmlImportMainTest extends GrouperTest {

  /**
   * 
   * @param args
   */
  public static void main(String[] args) {
    TestRunner.run(new XmlImportMainTest("testImport_v1_6_0"));
  }

  /**
   * 
   */
  public XmlImportMainTest() {
    super();
    
  }

  /**
   * @param name
   */
  public XmlImportMainTest(String name) {
    super(name);
    
  }

  /**
   * test an import
   */
  public void testImport_v1_6_0() {
    
    File importFileXml = GrouperUtil.fileFromResourceName("edu/internet2/middleware/grouper/xml/importXml/xmlImport_v1_6_0.xml");
    
    GrouperSession grouperSession = GrouperSession.startRootSession();
    
    GrouperCheckConfig.checkGroups();

    Stem stem = StemFinder.findByName(grouperSession, "etc", true);
    AttributeDef studentsAttrDef = stem.addChildAttributeDef("students", AttributeDefType.attr);
    studentsAttrDef.setAssignToAttributeDef(true);
    studentsAttrDef.store();
    studentsAttrDef.setAssignToAttributeDef(false);
    studentsAttrDef.store();
    studentsAttrDef.setAssignToStem(true);
    studentsAttrDef.store();
    String studentsAttrDefUuid = studentsAttrDef.getUuid();
    studentsAttrDef.getAttributeDefActionDelegate().addAction("someAction");
    
    XmlImportMain xmlImportMain = new XmlImportMain();
    
    
    xmlImportMain = new XmlImportMain();
    xmlImportMain.setRecordReport(true);

    xmlImportMain.processXml(importFileXml);

    File readonlyImportFile = new File(xmlImportMain.getRecordReportFileCanonicalPath());
    assertTrue(readonlyImportFile.exists() || readonlyImportFile.length() > 0);
    readonlyImportFile.delete();

    assertEquals(134,xmlImportMain.getTotalImportFileCount());
    
    //probably at least 2 to get started
    assertTrue(2 < xmlImportMain.getOriginalDbCount());

    assertsFor_v1_6_0(grouperSession, studentsAttrDefUuid);

    assertTrue(0 < xmlImportMain.getInsertCount());
    assertTrue(0 < xmlImportMain.getUpdateCount());

    //now, lets do it again
    xmlImportMain = new XmlImportMain();

    xmlImportMain.processXml(importFileXml);

    assertEquals(134,xmlImportMain.getTotalImportFileCount());
    
    //probably at least 2 to get started
    assertTrue(2 < xmlImportMain.getOriginalDbCount());

    assertsFor_v1_6_0(grouperSession, studentsAttrDefUuid);

    assertEquals(0, xmlImportMain.getInsertCount());
    assertEquals(0, xmlImportMain.getUpdateCount());
    assertEquals(134, xmlImportMain.getSkipCount());

    //############################
    //try record report again, should not find anything
    xmlImportMain = new XmlImportMain();
    
    xmlImportMain.setRecordReport(true);
    
    xmlImportMain.processXml(importFileXml);

    assertEquals(134,xmlImportMain.getTotalImportFileCount());

    assertTrue(2 < xmlImportMain.getOriginalDbCount());

    assertEquals(0, xmlImportMain.getInsertCount());
    assertEquals(0, xmlImportMain.getUpdateCount());

    readonlyImportFile = new File(xmlImportMain.getRecordReportFileCanonicalPath());
    assertTrue(!readonlyImportFile.exists());
    
    GrouperSession.stopQuietly(grouperSession);
    
  }

  /**
   * 
   * @param grouperSession
   * @param studentsAttrDefUuid 
   */
  private void assertsFor_v1_6_0(GrouperSession grouperSession, String studentsAttrDefUuid) {
    
    Member groupAmember = MemberFinder.findBySubject(grouperSession, GroupFinder.findByName(grouperSession, "etc:b", true).toSubject(), false);
    assertEquals("36a51e854fd94884b294ff971c9313c6", groupAmember.getContextId());
    assertEquals("e38a6d0920524551b0892e445552f99d", groupAmember.getSubjectId());

    Stem stemEtc = StemFinder.findByName(grouperSession, "etc", true);
    assertEquals("2010/02/07 16:26:29.085", GrouperUtil.dateStringValue(stemEtc.getCreateTimeLong()));
    
    Group groupA = GroupFinder.findByName(grouperSession, "etc:a", false);
    Group groupB = GroupFinder.findByName(grouperSession, "etc:b", false);
    
    assertNotNull(groupA);
    assertEquals("description", groupA.getDescription());

    GroupType groupTypeTest = GroupTypeFinder.find("test", true);
    
    assertEquals("be755e1ffa104faabfc4a5b679863b91", groupTypeTest.getUuid());
    
    Field attrField = FieldFinder.find("attr", true);
    
    assertEquals(new Long(3L), attrField.getHibernateVersionNumber());
    
    assertFalse(groupA.hasType(groupTypeTest));
    assertTrue(groupB.hasType(groupTypeTest));
    
    assertTrue(groupA.hasComposite());
    
    assertEquals("value", groupB.getAttributeOrFieldValue("attr", false, true));
    
    Role userSharerRole = GrouperDAOFactory.getFactory().getRole().findByName("etc:userSharer", true);
    Role userReceiverRole = GrouperDAOFactory.getFactory().getRole().findByName("etc:userReceiver", true);
    
    assertTrue(userSharerRole.getRoleInheritanceDelegate().getRolesInheritPermissionsFromThis().contains(userReceiverRole));
    
    AttributeDef studentsAttrDef = AttributeDefFinder.findByName("etc:students", true);
    assertEquals(studentsAttrDefUuid, studentsAttrDef.getUuid());
    assertTrue(studentsAttrDef.isAssignToGroup());
    assertFalse(studentsAttrDef.isAssignToStem());
    assertEquals(new Long(1), studentsAttrDef.getHibernateVersionNumber());

    AttributeDef studentsAttrDef2 = AttributeDefFinder.findByName("etc:students2", true);
    assertEquals("a7a987d55b2e4a39bd55f840cc467d99", studentsAttrDef2.getUuid());
    assertTrue(studentsAttrDef2.isAssignToGroupAssn());

    Set<String> actions = studentsAttrDef.getAttributeDefActionDelegate().allowedActionStrings();
    assertEquals(3, actions.size());
    assertTrue(actions.contains("assign"));
    assertTrue(actions.contains("someAction"));
    assertTrue(actions.contains("someAction2"));
    
    AttributeAssignAction someAction = studentsAttrDef.getAttributeDefActionDelegate().findAction("someAction", true);
    @SuppressWarnings("unused")
    AttributeAssignAction someAction2 = studentsAttrDef.getAttributeDefActionDelegate().findAction("someAction2", true);
    
    assertTrue(someAction.getAttributeAssignActionSetDelegate().getAttributeAssignActionNamesImpliedByThis().contains("someAction2"));
    
    AttributeDefName studentsAttrName2 = AttributeDefNameFinder.findByName("etc:studentsName2", true);
    assertEquals("96e9263133f9455582d24cbbe05a209d", studentsAttrName2.getId());

    AttributeDefName studentsAttrName = AttributeDefNameFinder.findByName("etc:studentsName", true);

    assertTrue(studentsAttrName.getAttributeDefNameSetDelegate().getAttributeDefNameNamesImpliedByThis().contains("etc:studentsName2"));

    assertTrue(groupB.getAttributeDelegate().hasAttribute(studentsAttrName));

    assertTrue(1 <= HibernateSession.bySqlStatic().select(int.class, "select count(*) from grouper_attribute_assign_value"));
    
    //TODO do the values later when the API exists
//    AttributeAssignValue attributeAssignValue = new AttributeAssignValue();
//    attributeAssignValue.setId(edu.internet2.middleware.grouper.internal.util.GrouperUuid.getUuid());
//    attributeAssignValue.setAttributeAssignId(attributeAssignResult.getAttributeAssign().getId());
//    attributeAssignValue.setValueString("string");
//    HibernateSession.byObjectStatic().saveOrUpdate(attributeAssignValue);

    Set<AttributeDefScope> attributeDefScopes = GrouperDAOFactory.getFactory().getAttributeDefScope().findByAttributeDefId(studentsAttrDef.getUuid());
    assertEquals(1, attributeDefScopes.size());
    AttributeDefScope attributeDefScope = attributeDefScopes.iterator().next();
    assertEquals("whatever", attributeDefScope.getScopeString());

    AuditType auditType = GrouperDAOFactory.getFactory().getAuditType().findByUuidOrName(null, "exportCategoryTest", "exportActionTest", true);
    assertEquals("9bf61f2aaee64dd09c0c66e143090955", auditType.getContextId());
    
    AuditEntry auditEntry = GrouperDAOFactory.getFactory().getAuditEntry().findById("8f2f228dc3f04ae295f881ba69f284e2", true);
    assertEquals(AuditTypeBuiltin.STEM_ADD.getAuditType().getId(), auditEntry.getAuditTypeId());
    assertEquals("etc", auditEntry.retrieveStringValue("name"));
    
    
  }
  
}
