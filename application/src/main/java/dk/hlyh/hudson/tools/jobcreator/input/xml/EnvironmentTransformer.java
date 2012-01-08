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

class EnvironmentTransformer {

  private List<String> includedJobs = new ArrayList<String>();
  private dk.hlyh.hudson.tools.jobcreator.model.Group activeEnvironment;

  EnvironmentTransformer() {
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

  private void visitEnvironment(dk.hlyh.hudson.tools.jobcreator.input.xml.model.Group sourceEnvironment) {
    // depth first 
    for (dk.hlyh.hudson.tools.jobcreator.input.xml.model.Group parent : sourceEnvironment.getParentEnv()) {
      visitEnvironment(parent);
    }

    // handle output pattern
    if (sourceEnvironment.getOutputPattern() != null && sourceEnvironment.getOutputPattern().trim().length() > 0) {
      activeEnvironment.setOutputPattern(sourceEnvironment.getOutputPattern());
    }

    // handle included jobs
    for (dk.hlyh.hudson.tools.jobcreator.input.xml.model.Job sourceJob : sourceEnvironment.getIncludedJobs()) {
      includedJobs.add(sourceJob.getName());
    }

    // handle excluded jobs
    for (dk.hlyh.hudson.tools.jobcreator.input.xml.model.Job sourceJob : sourceEnvironment.getExcludedJobs()) {
      includedJobs.remove(sourceJob.getName());
    }

    // handle property sets
    for (dk.hlyh.hudson.tools.jobcreator.input.xml.model.PropertySet sourceSet : sourceEnvironment.getProperties()) {
      dk.hlyh.hudson.tools.jobcreator.model.PropertySet activeSet = activeEnvironment.getPropertySet(sourceSet.getJob());

      for (dk.hlyh.hudson.tools.jobcreator.input.xml.model.Property sourceProperty : sourceSet.getProperties()) {
        if (sourceProperty.getValue() == null || sourceProperty.getValue().length() == 0) {
          activeSet.removeProperty(sourceProperty.getName());
          continue;
        }

        dk.hlyh.hudson.tools.jobcreator.model.Property activeProperty = activeSet.createProperty(sourceProperty.getName());
        activeProperty.setValue(sourceProperty.getValue());
        activeProperty.setPropagation(Utils.convertPropagation(sourceProperty.getPropagation()));
        activeProperty.setMerging(Utils.convertMerging(sourceProperty.getMerging()));
      }
    }
  }
}
