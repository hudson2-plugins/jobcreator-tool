/*
 * Copyright (c) 2011 Henrik Lynggaard Hansen 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Henrik Lynggaard Hansen
 */
package dk.hlyh.hudson.tools.jobcreator.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PropertySet {
  
  private Map<String, Property> properties = new HashMap<String, Property>();

  public PropertySet() {
    super();
  }
  
  public Property getProperty(String key) {
    return properties.get(key);
  }
  
  public Property createProperty(String key) {
    Property property = properties.get(key);
    if (property == null) {
      property =  new Property(key);
      properties.put(key, property);
    }
    return property;
  }
  
  public Collection<Property> getProperties() {
    return properties.values();
  }

  void removeProperty(String key) {
    properties.remove(key);
  }

  @Override
  public String toString() {
    return "PropertySet{" + "properties=" + properties + '}';
  }
  
  
}
