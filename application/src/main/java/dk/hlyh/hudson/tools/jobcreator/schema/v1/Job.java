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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.annotation.*;

@XmlType
public class Job {

  @XmlID
  @XmlElement(required = true)
  private String name;
  @XmlElement(required = true)
  private String template;
  @XmlElementWrapper(name = "inherit")
  @XmlElement(name = "job")
  @XmlIDREF  
  private Set<Job> parentJobs;
  @XmlElementWrapper(name = "upstream")
  @XmlElement(name = "job")
  @XmlIDREF
  private Set<Job> upstreamJobs;
  @XmlElementWrapper(name = "downstream")
  @XmlElement(name = "job")
  @XmlIDREF
  private Set<Job> downstreamJobs;
  @XmlElement(name = "join")
  @XmlIDREF
  private Job joinJob;
  @XmlElementWrapper(name = "properties")
  @XmlElement(name = "property")
  private Set<Property> properties;
  
  @XmlTransient
  private Map<String, Property> resolvedProperties;

  @XmlTransient String resolvedTemplate;
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public Set<Job> getDownstreamJobs() {
    if (downstreamJobs == null) {
      downstreamJobs = new HashSet<Job>();
    }
    return downstreamJobs;
  }

  public Set<Job> getParentJobs() {
    if (parentJobs == null) {
      parentJobs = new HashSet<Job>();
    }
    return parentJobs;
  }

  public Set<Property> getProperties() {
    if (properties == null) {
      properties = new HashSet<Property>();
    }
    return properties;
  }

  public Set<Job> getUpstreamJobs() {
    if (upstreamJobs == null) {
      upstreamJobs = new HashSet<Job>();
    }
    return upstreamJobs;
  }

  public Job getJoinJob() {
    return joinJob;
  }

  public Map<String, Property> getResolvedProperties() {
    if(resolvedProperties == null) {
      resolvedProperties = new HashMap<String, Property>();
    }
    return resolvedProperties;
  }

  public String getResolvedTemplate() {
    return resolvedTemplate;
  }

  public void setResolvedTemplate(String resolvedTemplate) {
    this.resolvedTemplate = resolvedTemplate;
  }
  
  

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Job other = (Job) obj;
    if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString() {
    return  name;
  }
}
