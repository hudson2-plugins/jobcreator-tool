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
package dk.hlyh.hudson.tools.jobcreator.input.xml;

import java.util.ArrayList;
import java.util.List;

class GroupTransformer {

  private List<String> includedJobs = new ArrayList<String>();
  private dk.hlyh.hudson.tools.jobcreator.model.Group activeEnvironment;

  GroupTransformer() {
    super();
  }

  void transformEnvironment(dk.hlyh.hudson.tools.jobcreator.input.xml.model.Group sourceEnvironment) {
    String envName = sourceEnvironment.getName();
    activeEnvironment = new dk.hlyh.hudson.tools.jobcreator.model.Group(envName);
    visitEnvironment(sourceEnvironment);
  }

  List<String> getIncludedJobs() {
    return includedJobs;
  }

  dk.hlyh.hudson.tools.jobcreator.model.Group getActiveEnvironment() {
    return activeEnvironment;
  }

  /** Perform a depth first traversal of the group inheritance.
   * @param sourceGroup The group to visit
   */
  private void visitEnvironment(dk.hlyh.hudson.tools.jobcreator.input.xml.model.Group sourceGroup) {
    // depth first 
    for (dk.hlyh.hudson.tools.jobcreator.input.xml.model.Group parent : sourceGroup.getParentEnv()) {
      visitEnvironment(parent);
    }

    // handle output pattern
    if (sourceGroup.getOutputPattern() != null && sourceGroup.getOutputPattern().trim().length() > 0) {
      activeEnvironment.setOutputPattern(sourceGroup.getOutputPattern());
    }

    // handle included jobs
    for (dk.hlyh.hudson.tools.jobcreator.input.xml.model.Job sourceJob : sourceGroup.getIncludedJobs()) {
      includedJobs.add(sourceJob.getName());
    }

    // handle excluded jobs
    for (dk.hlyh.hudson.tools.jobcreator.input.xml.model.Job sourceJob : sourceGroup.getExcludedJobs()) {
      includedJobs.remove(sourceJob.getName());
    }

    // handle property sets
    for (dk.hlyh.hudson.tools.jobcreator.input.xml.model.PropertySet sourceSet : sourceGroup.getProperties()) {
      dk.hlyh.hudson.tools.jobcreator.model.PropertySet activeSet = activeEnvironment.getPropertySet(sourceSet.getJob());

      for (dk.hlyh.hudson.tools.jobcreator.input.xml.model.Property sourceProperty : sourceSet.getProperties()) {
        dk.hlyh.hudson.tools.jobcreator.model.Property activeProperty = activeSet.createProperty(sourceProperty.getName());
        activeProperty.setValue(sourceProperty.getValue());
        activeProperty.setPropagation(Utils.convertPropagation(sourceProperty.getPropagation()));
        activeProperty.setMerging(Utils.convertMerging(sourceProperty.getMerging()));
      }
    }
  }
}
