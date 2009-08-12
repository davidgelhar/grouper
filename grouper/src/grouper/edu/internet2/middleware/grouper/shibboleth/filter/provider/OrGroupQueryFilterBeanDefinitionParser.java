/*
 * Copyright (C) 2004-2007 University Corporation for Advanced Internet Development, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package edu.internet2.middleware.grouper.shibboleth.filter.provider;

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.xml.util.XMLHelper;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import edu.internet2.middleware.grouper.shibboleth.config.GrouperNamespaceHandler;
import edu.internet2.middleware.grouper.shibboleth.filter.OrGroupFilter;
import edu.internet2.middleware.shibboleth.common.config.SpringConfigurationUtils;

public class OrGroupQueryFilterBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

  public static final QName TYPE_NAME = new QName(GrouperNamespaceHandler.NAMESPACE, "OR");

  public static final QName GROUP_FILTER = new QName(GrouperNamespaceHandler.NAMESPACE, "GroupFilter");

  protected Class getBeanClass(Element element) {
    return OrGroupFilter.class;
  }

  protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
    super.doParse(element, parserContext, builder);

    List<Element> groupFilterElements = XMLHelper.getChildElementsByTagNameNS(element,
        GrouperNamespaceHandler.NAMESPACE, "GroupFilter");

    builder.addConstructorArgValue(SpringConfigurationUtils.parseInnerCustomElement(groupFilterElements.get(0),
        parserContext));

    builder.addConstructorArgValue(SpringConfigurationUtils.parseInnerCustomElement(groupFilterElements.get(1),
        parserContext));
  }

  protected boolean shouldGenerateId() {
    return true;
  }
}
