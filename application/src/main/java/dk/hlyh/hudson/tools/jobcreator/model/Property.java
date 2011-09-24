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

public class Property {

  private final String key;  
  private String value;
  private Propagation propagation;  
  private Merge merging;

  public Property(String key) {
    super();
    this.key = key;
  }
 
  public Merge getMerging() {
    if (merging == null) {
      merging = Merge.Replace;
    }
    return merging;
  }

  public String getKey() {
    return key;
  }

  public Propagation getPropagation() {
    if (propagation == null) {
      propagation = Propagation.Continue;
    }
    return propagation;
  }

  public String getValue() {
    return value;
  }

  public void setMerging(Merge merging) {
    this.merging = merging;
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
    if ((this.key == null) ? (other.key != null) : !this.key.equals(other.key)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + (this.key != null ? this.key.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString() {
    return "Property{" + "key=" + key+ ", value=" + value  + ", propagation=" + propagation + ", merging=" + merging + '}';
  }  
}
