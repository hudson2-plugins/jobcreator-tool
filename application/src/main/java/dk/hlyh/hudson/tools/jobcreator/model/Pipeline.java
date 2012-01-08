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

import java.util.Collections;
import java.util.List;

public class Pipeline {
  
  private final String name;
  private final Group environment;
  private final List<Job> jobs;

  public Pipeline(String name, Group environment, List<Job> jobs) {
    this.name = name;
    this.environment = environment;
    this.jobs = Collections.unmodifiableList(jobs);
  }

  public Group getEnvironment() {
    return environment;
  }

  public List<Job> getJobs() {
    return jobs;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "Pipeline{" + "name=" + name + ", environment=" + environment + ", jobs=" + jobs + '}';
  }    
}
