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
package dk.hlyh.hudson.tools.jobcreator.helper;

import dk.hlyh.hudson.tools.jobcreator.schema.v1.Environment;
import dk.hlyh.hudson.tools.jobcreator.schema.v1.Job;
import java.util.HashSet;
import java.util.Set;

public class IncludedJobsVisitor implements Visitor{

  private final Set<Job> jobs;
  public IncludedJobsVisitor() {
    jobs = new HashSet<Job>();
  }

  
  @Override
  public void visitEnvironment(Environment environment) {
    jobs.addAll(environment.getIncludedJobs());
    jobs.removeAll(environment.getExcludedJobs());    
  }

  @Override
  public void visitJob(Job currentJob) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
  public Set<Job> getJobs() {
    return jobs;
  }
  
  
  
}
