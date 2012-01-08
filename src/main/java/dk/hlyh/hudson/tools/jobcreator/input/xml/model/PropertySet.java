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
package dk.hlyh.hudson.tools.jobcreator.input.xml.model;

import java.util.Set;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class PropertySet {

  @XmlAttribute()
  private String job;
  
  @XmlElement(name = "property")
  private Set<Property> properties;

  public PropertySet() {
    super();
  }
  
  public String getJob() {
    return job != null && job.trim().length() > 0 ? job : dk.hlyh.hudson.tools.jobcreator.model.PropertySet.GLOBAL_SET;
  }

  public Set<Property> getProperties() {
    return properties;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final PropertySet other = (PropertySet) obj;
    if ((this.job == null) ? (other.job != null) : !this.job.equals(other.job)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 29 * hash + (this.job != null ? this.job.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString() {
    return "PropertySet{" + "job=" + job + ", properties=" + properties + '}';
  }
  
  
}
