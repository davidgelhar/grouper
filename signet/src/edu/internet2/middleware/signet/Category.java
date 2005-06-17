/*--
$Id: Category.java,v 1.3 2005-06-17 23:24:28 acohen Exp $
$Date: 2005-06-17 23:24:28 $

Copyright 2004 Internet2 and Stanford University.  All Rights Reserved.
Licensed under the Signet License, Version 1,
see doc/license.txt in this distribution.
*/
package edu.internet2.middleware.signet;

import java.util.Set;

import edu.internet2.middleware.subject.Subject;

/**
* Category organizes a group of {@link Function}s. Each {@link Function} is
* intended to correspond to a business-level task that a {@link Subject}
* must perform in order to accomplish some business operation.
* 
*/

public interface Category
extends SubsystemPart, Name, Comparable
{
  /**
   * Gets the ID of this entity.
   * 
   * @return Returns a short mnemonic id which will appear in XML
   *    documents and other documents used by analysts.
   */
  public String getId();
  
  /**
   * Gets the functions associated with this Category.
   * 
   * @return Returns the functions associated with this Category.
   */
  public Set getFunctions();
  
  /**
   * Sets the Functions associated with this Category.
   * 
   * @param functions The functions to set.
   */
  public void setFunctionsArray(Function[] functions);

  /**
   * Gets the Subsystem associated with this Category.
   * 
   * @return the Subsystem associated with this Category.
   */
  public Subsystem getSubsystem();
}
