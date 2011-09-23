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
import dk.hlyh.hudson.tools.jobcreator.schema.v1.Property;
import dk.hlyh.hudson.tools.jobcreator.schema.v1.PropertySet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ResolvePropertyVisitor implements Visitor {

  private final Job activeJob;
  private final Environment activeEnvironment;
  
  public ResolvePropertyVisitor(Job job) {
    super();
    this.activeJob = job;
    this.activeEnvironment = null;
  }

  public ResolvePropertyVisitor(Environment env) {
    super();
    this.activeJob = null;
    this.activeEnvironment = env;
  }

  @Override
  public void visitJob(Job currentJob) {
    for (Property currentProperty : currentJob.getProperties()) {
      activeJob.getResolvedProperties().put(currentProperty.getName(), currentProperty);
    }
    if (currentJob.getTemplate() != null && currentJob.getTemplate().trim().length() > 0) {
      activeJob.setResolvedTemplate(currentJob.getTemplate());
    }
  }

  @Override
  public void visitEnvironment(Environment environment) {
    Map<String, Map<String, Property>> resolvesPropertysets = activeEnvironment.getResolvesProperties();

    for (PropertySet currentPropertySet : environment.getProperties()) {

      Map<String, Property> resolvedPropertySet = resolvesPropertysets.get(currentPropertySet.getName());
      if (resolvedPropertySet == null) {
        resolvedPropertySet = new HashMap<String, Property>();
        resolvesPropertysets.put(currentPropertySet.getName(), resolvedPropertySet);
      }

      for (Property currentProperty : currentPropertySet.getProperties()) {
        resolvedPropertySet.put(currentProperty.getName(), currentProperty);
      }
    }
    
    if (environment.getOutputPattern() != null && environment.getOutputPattern().trim().length() > 0) {    
      activeEnvironment.setResolvesOutputPattern(environment.getOutputPattern());
    }
  }
}
