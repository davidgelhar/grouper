/*******************************************************************************
 * Copyright 2012 Internet2
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
 ******************************************************************************/
/**
 * @author mchyzer
 * $Id$
 */
package edu.internet2.middleware.grouper.ws.rest.attribute;

import edu.internet2.middleware.grouper.ws.coresoap.WsAttributeDefLookup;
import edu.internet2.middleware.grouper.ws.coresoap.WsParam;
import edu.internet2.middleware.grouper.ws.coresoap.WsSubjectLookup;
import edu.internet2.middleware.grouper.ws.rest.WsRequestBean;
import edu.internet2.middleware.grouper.ws.rest.method.GrouperRestHttpMethod;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * Bean for rest request to get attributes assign actions
 */
@ApiModel(description = "bean that will be the data from rest request for getting attribute assign actions<br /><br /><b>actAsSubjectLookup</b>: If allowed to act as other users (e.g. if a UI uses the Grouper WS behind the scenes), specify the user to act as here<br />"
    + "<br /><br /><b>params</b>: optional params for this request<br />"
    + "<br /><br /><b>wsAttributeDefLookup</b>: find assignments in these attribute defs<br />")
public class WsRestGetAttributeAssignActionsRequest implements WsRequestBean {

  /**
   * @see edu.internet2.middleware.grouper.ws.rest.WsRequestBean#retrieveRestHttpMethod()
   */
  @Override
  public GrouperRestHttpMethod retrieveRestHttpMethod() {
    return GrouperRestHttpMethod.GET;
  }
  
  /** is the version of the client.  Must be in GrouperWsVersion, e.g. v1_3_000 */
  private String clientVersion;
  
  /**
   * is the version of the client.  Must be in GrouperWsVersion, e.g. v1_3_000
   * @return version
   */
  @ApiModelProperty(value = "Version of the client (i.e. that the client was coded against)", example = "v2_6_001")
  public String getClientVersion() {
    return this.clientVersion;
  }

  /**
   * is the version of the client.  Must be in GrouperWsVersion, e.g. v1_3_000
   * @param clientVersion1
   */
  public void setClientVersion(String clientVersion1) {
    this.clientVersion = clientVersion1;
  }

  /**
   * find assignments in these attribute defs
   */
  private WsAttributeDefLookup[] wsAttributeDefLookups;
  
  /**
   * find assignments in these attribute defs
   * @return defs
   */
  public WsAttributeDefLookup[] getWsAttributeDefLookups() {
    return this.wsAttributeDefLookups;
  }

  /**
   * find assignments in these attribute defs
   * @param wsAttributeDefLookups1
   */
  public void setWsAttributeDefLookups(WsAttributeDefLookup[] wsAttributeDefLookups1) {
    this.wsAttributeDefLookups = wsAttributeDefLookups1;
  }

  /**
   * actions to query, or none to query all actions
   */
  private String[] actions; 
  
  /**
   * actions to query, or none to query all actions
   * @return actions
   */
  @ApiModelProperty(value = "action to query, or none to query all actions", example = "read")
  public String[] getActions() {
    return this.actions;
  }

  /**
   * actions to query, or none to query all actions
   * @param actions1
   */
  public void setActions(String[] actions1) {
    this.actions = actions1;
  }

  /** if acting as someone else */
  private WsSubjectLookup actAsSubjectLookup;
  
  /**
   * if acting as someone else
   * @return act as subject
   */
  public WsSubjectLookup getActAsSubjectLookup() {
    return this.actAsSubjectLookup;
  }

  /**
   * if acting as someone else
   * @param actAsSubjectLookup1
   */
  public void setActAsSubjectLookup(WsSubjectLookup actAsSubjectLookup1) {
    this.actAsSubjectLookup = actAsSubjectLookup1;
  }

  /** optional: reserved for future use */
  private  WsParam[] params;

  /**
   * optional: reserved for future use
   * @return params
   */
  public WsParam[] getParams() {
    return this.params;
  }

  /**
   * optional: reserved for future use
   * @param params1
   */
  public void setParams(WsParam[] params1) {
    this.params = params1;
  }

}
