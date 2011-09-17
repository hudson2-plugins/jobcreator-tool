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
package dk.hlyh.hudson.tools.jobcreator.schema.v1;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlType(name = "job-property")
public class Property {

  @XmlValue
  private String value;
  
  @XmlAttribute(name = "name", required = true)
  private String name;
  
  @XmlAttribute(name = "propagation", required = false)
  private Propagation propagation;
  
  @XmlAttribute(name = "merge", required = false)
  private Merge merging;

  public Property() {
    super();
  }

  public Property(Property original) {
    this.name = original.name;
    this.value = original.value;
    this.propagation = original.propagation;
    this.merging = original.merging;
  }

  
  public Merge getMerging() {
    return merging;
  }

  public String getName() {
    return name;
  }

  public Propagation getPropagation() {
    return propagation;
  }

  public String getValue() {
    return value;
  }

  public void setMerging(Merge merging) {
    this.merging = merging;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPropagation(Propagation propagation) {
    this.propagation = propagation;
  }

  public void setValue(String value) {
    this.value = value;
  }
  
  

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Property other = (Property) obj;
    if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString() {
    return "Property{" + "value=" + value + ", name=" + name + ", propagation=" + propagation + ", merging=" + merging + '}';
  }  
}
