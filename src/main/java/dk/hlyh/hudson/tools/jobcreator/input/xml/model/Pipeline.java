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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Pipeline {

  @XmlElement(required = true)
  private String name;
  @XmlElementWrapper(name = "groups")
  @XmlElement(name = "group")
  private List<Group> environments;
  @XmlElementWrapper(name = "jobs")
  @XmlElement(name = "job")
  private List<Job> jobs;

  public Pipeline() {
    super();
  }

  
  
  public List<Group> getEnvironments() {
    if (environments == null) {
      environments = new ArrayList<Group>();
    }
    return environments;
  }

  public List<Job> getJobs() {
    if (jobs == null) {
      jobs = new ArrayList<Job>();
    }
    return jobs;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
