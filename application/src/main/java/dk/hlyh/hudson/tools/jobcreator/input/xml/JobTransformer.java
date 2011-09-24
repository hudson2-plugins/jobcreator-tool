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

import dk.hlyh.hudson.tools.jobcreator.model.Job;

class JobTransformer {

  private dk.hlyh.hudson.tools.jobcreator.model.Job activeJob;

  JobTransformer() {
  }

  void transformJob(dk.hlyh.hudson.tools.jobcreator.input.xml.model.Job sourceJob) {
    String envName = sourceJob.getName();
    activeJob = new dk.hlyh.hudson.tools.jobcreator.model.Job(envName);
    visitJob(sourceJob);
  }

  Job getActiveJob() {
    return activeJob;
  }  
  
  private void visitJob(dk.hlyh.hudson.tools.jobcreator.input.xml.model.Job sourceJob) {
    // depth first 
    for (dk.hlyh.hudson.tools.jobcreator.input.xml.model.Job parent : sourceJob.getParentJobs()) {
      visitJob(parent);
    }
    // handle the template
    if (sourceJob.getTemplate() != null && sourceJob.getTemplate().trim().length() > 0) {
      activeJob.setTemplate(sourceJob.getTemplate());
    }

    //handle properties
    for (dk.hlyh.hudson.tools.jobcreator.input.xml.model.Property sourceProperty : sourceJob.getProperties()) {
        if (sourceProperty.getValue() == null || sourceProperty.getValue().length() == 0) {
          activeJob.removeProperty(sourceProperty.getName());
          continue;
        }      
      dk.hlyh.hudson.tools.jobcreator.model.Property activeProperty = activeJob.createProperty(sourceProperty.getName());
      activeProperty.setValue(sourceProperty.getValue());
      activeProperty.setPropagation(Utils.convertPropagation(sourceProperty.getPropagation()));
      activeProperty.setMerging(Utils.convertMerging(sourceProperty.getMerging()));
    }
  }
}

