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

import java.util.HashMap;
import java.util.Map;

public class Environment {

  private final String name;
  private String outputPattern;
  private Map<String, PropertySet> propertySets = new HashMap<String, PropertySet>();

  public Environment(String name) {
    super();
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getOutputPattern() {
    return outputPattern;
  }

  public PropertySet getPropertySet(String name) {
    PropertySet set = propertySets.get(name);
    if (set == null) {
      set = new PropertySet();
      propertySets.put(name, set);
    }
    return set;
  }

  public void setOutputPattern(String outputPattern) {
    this.outputPattern = outputPattern;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Environment other = (Environment) obj;
    if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 61 * hash + (this.name != null ? this.name.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString() {
    return "Environment{" + "name=" + name + ", outputPattern=" + outputPattern + ", propertySets=" + propertySets + '}';
  }
  
}
