/**
 * 
 */
package edu.internet2.middleware.grouper.grouperUi.beans.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupFinder;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.Member;
import edu.internet2.middleware.grouper.MemberFinder;
import edu.internet2.middleware.grouper.audit.AuditEntry;
import edu.internet2.middleware.grouper.audit.AuditTypeBuiltin;
import edu.internet2.middleware.grouper.grouperUi.beans.api.GuiGroup;
import edu.internet2.middleware.grouper.grouperUi.beans.api.GuiMember;
import edu.internet2.middleware.grouper.ui.GrouperUiFilter;


/**
 * @author mchyzer
 *
 */
public class GuiAuditEntry {

  /**
   * gui group for the audit if applicable
   */
  private GuiGroup guiGroup;

  
  
  /**
   * gui group for the audit if applicable
   * @return the gui group
   */
  public GuiGroup getGuiGroup() {
    return this.guiGroup;
  }

  /**
   * gui group for the audit if applicable
   * @param guiGroup1
   */
  public void setGuiGroup(GuiGroup guiGroup1) {
    this.guiGroup = guiGroup1;
  }

  /**
   * gui member for the audit if applicable
   * @return the member
   */
  public GuiMember getGuiMember() {
    return this.guiMember;
  }

  /**
   * gui member for the audit if applicable
   * @param guiMember1
   */
  public void setGuiMember(GuiMember guiMember1) {
    this.guiMember = guiMember1;
  }

  /**
   * gui member for the audit if applicable
   */
  private GuiMember guiMember;
  
  /**
   * default constructor
   */
  public GuiAuditEntry() {
    
  }
  
  /**
   * 
   * @param theAuditEntry
   */
  public GuiAuditEntry(AuditEntry theAuditEntry) {
    this.auditEntry = theAuditEntry;
  }

  
  /**
   * 2/1/2013 8:03 AM
   * @return the date for screen
   */
  public String getGuiDate() {
    
    HttpServletRequest httpServletRequest = GrouperUiFilter.retrieveHttpServletRequest();
    Locale locale = httpServletRequest.getLocale();
    DateFormat guiDateFormat = new SimpleDateFormat("yyyy/MM/dd kk:mm aa", locale);
    return guiDateFormat.format(this.auditEntry.getCreatedOn());
  }
  
  /**
   * underlying audit entry
   */
  private AuditEntry auditEntry;
  
  /** logger */
  protected static final Log LOG = LogFactory.getLog(GuiAuditEntry.class);

  /**
   * underlying audit entry
   * @return audit
   */
  public AuditEntry getAuditEntry() {
    return this.auditEntry;
  }

  /**
   * underlying audit entry
   * @param auditEntry1
   */
  public void setAuditEntry(AuditEntry auditEntry1) {
    this.auditEntry = auditEntry1;
  }
  
  /**
   * convert the audit to an audit line for screen
   * @return the audit line
   */
  public String getAuditLine() {
    String actionName = this.auditEntry.getAuditType().getActionName();
    String category = this.auditEntry.getAuditType().getAuditCategory();
    
    AuditTypeBuiltin auditTypeBuiltin = AuditTypeBuiltin.valueOfIgnoreCase(category, actionName, false);
    
    if (auditTypeBuiltin == null) {
      LOG.error("Cant find audit builtin for category: " + category + " and action: " + actionName);
      return TextContainer.retrieveFromRequest().getText().get("auditsUndefinedAction");
    }
    
    //set this so it can be accessed from text
    GrouperRequestContainer.retrieveFromRequestOrCreate().setGuiAuditEntry(this);
    
    switch (auditTypeBuiltin) {
      
      case ATTRIBUTE_ASSIGN_ANYMSHIP_ADD:
        
        break;
        
      case ATTRIBUTE_ASSIGN_ANYMSHIP_DELETE:
      
        break;
      
      case ATTRIBUTE_ASSIGN_ANYMSHIP_UPDATE:
        
        break;
      
      case ATTRIBUTE_ASSIGN_ASSIGN_ADD:
        
        break;
      
      case ATTRIBUTE_ASSIGN_ASSIGN_DELETE:
        
        break;
      
      case ATTRIBUTE_ASSIGN_ASSIGN_UPDATE:
        
        break;
      
      case ATTRIBUTE_ASSIGN_ATTRDEF_ADD:
        
        break;
      
      case ATTRIBUTE_ASSIGN_ATTRDEF_DELETE:
        
        break;
      
      case ATTRIBUTE_ASSIGN_ATTRDEF_UPDATE:
        
        break;
      
      case ATTRIBUTE_ASSIGN_GROUP_ADD:
        
        break;
      
      case ATTRIBUTE_ASSIGN_GROUP_DELETE:
        
        break;
      
      case ATTRIBUTE_ASSIGN_GROUP_UPDATE:
        
        break;
      
      case ATTRIBUTE_ASSIGN_IMMMSHIP_ADD:
        
        break;
      
      case ATTRIBUTE_ASSIGN_IMMMSHIP_DELETE:
        
        break;
      
      case ATTRIBUTE_ASSIGN_IMMMSHIP_UPDATE:
        
        break;
      
      case ATTRIBUTE_ASSIGN_MEMBER_ADD:
        
        break;
      
      case ATTRIBUTE_ASSIGN_MEMBER_DELETE:
        
        break;
      
      case ATTRIBUTE_ASSIGN_MEMBER_UPDATE:
        
        break;
      
      case ATTRIBUTE_ASSIGN_STEM_ADD:
        
        break;
      
      case ATTRIBUTE_ASSIGN_STEM_DELETE:
        
        break;
      
      case ATTRIBUTE_ASSIGN_STEM_UPDATE:
        
        break;
      
      case ATTRIBUTE_ASSIGN_VALUE_ADD:
        
        break;
      
      case ATTRIBUTE_ASSIGN_VALUE_DELETE:
        
        break;
      
      case ATTRIBUTE_ASSIGN_VALUE_UPDATE:
        
        break;
      
      case ATTRIBUTE_DEF_ADD:
        
        break;
      
      case ATTRIBUTE_DEF_DELETE:
        
        break;
      
      case ATTRIBUTE_DEF_NAME_ADD:
        
        break;
      
      case ATTRIBUTE_DEF_NAME_DELETE:
        
        break;
      
      case ATTRIBUTE_DEF_NAME_UPDATE:
        
        break;
      
      case ATTRIBUTE_DEF_UPDATE:
        
        break;
      
      case ENTITY_ADD:
        
        break;
      
      case ENTITY_DELETE:
        
        break;
      
      case ENTITY_UPDATE:
        
        break;
      
      case EXTERNAL_SUBJ_ATTR_ADD:
        
        break;
      
      case EXTERNAL_SUBJ_ATTR_DELETE:
        
        break;
      
      case EXTERNAL_SUBJ_ATTR_UPDATE:
        
        break;
      
      case EXTERNAL_SUBJECT_ADD:
        
        break;
      
      case EXTERNAL_SUBJECT_DELETE:
        
        break;
      
      case EXTERNAL_SUBJECT_INVITE_EMAIL:
        
        break;
      
      case EXTERNAL_SUBJECT_INVITE_IDENTIFIER:
        
        break;
      
      case EXTERNAL_SUBJECT_REGISTER_ADD:
        
        break;
      
      case EXTERNAL_SUBJECT_REGISTER_DELETE:
        
        break;
      
      case EXTERNAL_SUBJECT_REGISTER_UPDATE:
        
        break;
      
      case EXTERNAL_SUBJECT_UPDATE:
        
        break;
      
      case GROUP_ADD:
        
        break;
      
      case GROUP_ATTRIBUTE_ADD:
        
        break;
      
      case GROUP_ATTRIBUTE_DELETE:
        
        break;
      
      case GROUP_ATTRIBUTE_UPDATE:
        
        break;
      
      case GROUP_COMPOSITE_ADD:
        
        break;
      
      case GROUP_COMPOSITE_DELETE:
        
        break;
      
      case GROUP_COMPOSITE_UPDATE:
        
        break;
      
      case GROUP_COPY:
        
        break;
      
      case GROUP_DELETE:
        
        break;
      
      case GROUP_FIELD_ADD:
        
        break;
      
      case GROUP_FIELD_DELETE:
        
        break;
      
      case GROUP_FIELD_UPDATE:
        
        break;
      
      case GROUP_MOVE:
        
        break;
      
      case GROUP_TYPE_ADD:
        
        break;
      
      case GROUP_TYPE_ASSIGN:
        
        break;
      
      case GROUP_TYPE_DELETE:
        
        break;
      
      case GROUP_TYPE_UNASSIGN:
        
        break;
      
      case GROUP_TYPE_UPDATE:
        
        break;
      
      case GROUP_UPDATE:
        
        break;
      
      case MEMBER_CHANGE_SUBJECT:
        
        break;
      
      case MEMBERSHIP_GROUP_ADD:

        String groupId = this.auditEntry.retrieveStringValue("groupId");
        String memberId = this.auditEntry.retrieveStringValue("memberId");
        
        Group group = GroupFinder.findByUuid(GrouperSession.staticGrouperSession(), groupId, false);
        
        Member member = MemberFinder.findByUuid(GrouperSession.staticGrouperSession(), memberId, false);

        GuiGroup guiGroup = new GuiGroup(group);
        
        this.setGuiGroup(guiGroup);
        
        GuiMember guiMember = new GuiMember(member);

        this.setGuiMember(guiMember);
        
        return TextContainer.retrieveFromRequest().getText().get("audits_MEMBERSHIP_GROUP_ADD");
        
        // <%-- <strong>Added</strong> <a href="#">John Smith</a> as a member of 
        // the&nbsp;<a href="#" rel="tooltip" data-html="true" data-delay-show='200' 
        // data-placement="right" title="&lt;strong&gt;FOLDER:&lt;/strong&gt;&lt;br 
        // /&gt;Full : Path : To : The : Entity&lt;br /&gt;&lt;br /&gt;This is the 
        // description for this entity. Lorem ipsum dolor sit amet, consectetur 
        // adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.">Editors</a>&nbsp;group. --%>

        
      case MEMBERSHIP_GROUP_DELETE:
        
        break;
      
      case MEMBERSHIP_GROUP_UPDATE:
        
        break;
      
      case PRIVILEGE_GROUP_ADD:
        
        break;
      
      case PRIVILEGE_GROUP_DELETE:
        
        break;
      
      case PRIVILEGE_GROUP_UPDATE:
        
        break;
        
      case PRIVILEGE_STEM_ADD:
        
        break;
      
      case PRIVILEGE_STEM_DELETE:
        
        break;
      
      case PRIVILEGE_STEM_UPDATE:
        
        break;
      
      case STEM_ADD:
        
        break;
      
      case STEM_COPY:
        
        break;
      
      case STEM_DELETE:
        
        break;
      
      case STEM_MOVE:
        
        break;
      
      case STEM_UPDATE:
        
        break;
      
      case XML_IMPORT:
        
        break;
      
      default:
        LOG.error("Cant find audit builtin for category: " + category + " and action: " + actionName);
        return TextContainer.retrieveFromRequest().getText().get("auditsUndefinedAction");
        
    }
    
    if (actionName != null) {
      return category + " - " + actionName;
    }

    LOG.error("Cant find audit builtin for category: " + category + " and action: " + actionName);
    return TextContainer.retrieveFromRequest().getText().get("auditsUndefinedAction");
    
  }
  
}
