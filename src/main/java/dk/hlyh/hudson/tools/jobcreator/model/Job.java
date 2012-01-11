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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class Job {

  private final String name;
  private String template;
  private Set<Job> upstream = new HashSet<Job>();
  private Set<Job> downstream = new HashSet<Job>();
  private PropertySet propertySet = new PropertySet();
  
  public Job(String name) {
    super();
    this.name = name;
  }

  public String getName() {
    return name;
  }
   
  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public Set<Job> getDownstream() {
    return downstream;
  }

  public Set<Job> getUpstream() {
    return upstream;
  }
  
  public Collection<Property> getProperties() {
    return propertySet.getProperties();
  }
  public Property getProperty(String key) {
    return propertySet.getProperty(key);
  }

  public Property createProperty(String key) {
    return propertySet.createProperty(key);
  }
  
  public Property createProperty(Property original) {
    Property newProperty = propertySet.createProperty(original.getKey());
    newProperty.setMerging(original.getMerging());
    newProperty.setPropagation(original.getPropagation());
    newProperty.setValue(original.getValue());
    return newProperty;
  }  
  
  public void removeProperty(String key) {
    propertySet.removeProperty(key);
  }

  @Override
  public String toString() {
    return "Job{" + "name=" + name + ", template=" + template + ", propertySet=" + propertySet + '}';
  }
  
  
  
}
