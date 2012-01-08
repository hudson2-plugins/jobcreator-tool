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
package dk.hlyh.hudson.tools.jobcreator;

import dk.hlyh.hudson.tools.jobcreator.helper.FreemarkerHelper;
import dk.hlyh.hudson.tools.jobcreator.helper.TemplateValuesBuilder;

import dk.hlyh.hudson.tools.jobcreator.model.Propagation;
import dk.hlyh.hudson.tools.jobcreator.model.Property;
import dk.hlyh.hudson.tools.jobcreator.model.Group;
import dk.hlyh.hudson.tools.jobcreator.model.Job;
import dk.hlyh.hudson.tools.jobcreator.model.Pipeline;
import dk.hlyh.hudson.tools.jobcreator.model.PropertySet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Resolver {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private final Arguments arguments;
  private final Pipeline pipeline;

  public Resolver() {
    super();
    arguments = null;
    pipeline = null;
  }

  public Resolver(Arguments arguments, Pipeline pipeline) {
    this.arguments = arguments;
    this.pipeline = pipeline;
  }

  public void resolve() throws ImportException {

    FreemarkerHelper.setupFreemarker(arguments.getTemplateDirectory());
    Group activeGroup = pipeline.getGroup();
    List<Job> activeJobs = pipeline.getJobs();

    LOGGER.log(Level.INFO, "Active environment: {0}", activeGroup.getName());
    LOGGER.log(Level.INFO, "Jobs to build: {0}", jobsAsString(activeJobs));

    // Apply environment properties
    for (Job job : pipeline.getJobs()) {

      PropertySet globalSet = activeGroup.getPropertySet(PropertySet.GLOBAL_SET);
      PropertySet jobSpecificSet = activeGroup.getPropertySet(job.getName());
      if (globalSet != null) {
        mergePropertySet(globalSet, job);
      }
      if (jobSpecificSet != null) {
        mergePropertySet(jobSpecificSet, job);
      }
    }

    // loop all jobs and propagate
    for (Job job : activeJobs) {
      if (job.getUpstream().isEmpty()) {
        LOGGER.log(Level.INFO, "Pushing properties downstream: {0}", job.getName());
        Map<String, Property> pushedProperties = new HashMap<String, Property>();
        pushDownstream(pushedProperties, job);
      }
    }
    for (Job job : activeJobs) {
      if (job.getDownstream().isEmpty()) {
        LOGGER.log(Level.INFO, "Pushing properties upstream: {0}", job.getName());
        Map<String, Property> pushedProperties = new HashMap<String, Property>();
        pushUpstream(pushedProperties, job);
      }
    }

    // Loop all jobs and and build the template model
    for (Job job : activeJobs) {
      LOGGER.log(Level.INFO, "Writing job: {0}", job.getName());
      TemplateValuesBuilder builder = new TemplateValuesBuilder();
      builder.setProperty("import.pipeline.name", pipeline.getName());
      builder.setProperty("import.env.name", arguments.getEnvironment());
      builder.setProperty("import.jobs", jobsAsString(activeJobs));
      builder.setProperty("import.job.upstream", jobsAsString(job.getUpstream()));
      builder.setProperty("import.job.downstream", jobsAsString(job.getDownstream()));
      for (Property property : job.getProperties()) {
        builder.setProperty(property.getKey(), property.getValue());
      }

      FreemarkerHelper.writeJob(arguments.getOutputDirectory(), job, activeGroup, builder, pipeline);
      LOGGER.log(Level.INFO, "Completed job: {0}", job.getName());
    }
  }

  private void pushUpstream(Map<String, Property> pushedProperties, Job job) {
    Map<String, Property> pushedPropertiesDownstream = propagate(pushedProperties, job, Propagation.Downstream);


    for (Job upstreamJob : job.getUpstream()) {
      pushUpstream(pushedPropertiesDownstream, upstreamJob);
    }
  }

  private void pushDownstream(Map<String, Property> pushedProperties, Job job) {
    Map<String, Property> pushedPropertiesDownstream = propagate(pushedProperties, job, Propagation.Downstream);

    for (Job downstreamJob : job.getDownstream()) {
      pushDownstream(pushedPropertiesDownstream, downstreamJob);
    }
  }

  /**
   * Propagate the values according to the following rules.
   * 1) For each of the known pushed properties
   * 1.1) if the pushed property does not exist on the current job create it.
   * 1.2) If exist and propagation set to none, stop propagation, but merge the value depending on the Merge setting.
   * 1.3) if exist and propagation set to Upstream/Downstream stop propagation.
   * 2) Add add currently pushed properties to the set of further pushed properties.
   * 3) Remove add properties found in step 1.2 and 1.3
   * 4) Add all properties with a propagation in the same direction to the further pushed properties.This potentianlly
   * re-adds some properties from step 1.3
   * @param pushedProperties
   * @param job
   * @param direction
   * @return 
   */
  private Map<String, Property> propagate(Map<String, Property> pushedProperties, Job job, Propagation direction) {
    LOGGER.log(Level.FINE, "Propagating for job {0}", job.getName());
    Map<String, Property> furtherPushedProperties = new HashMap<String, Property>();

    // apply currently pushed properties
    List<String> toRemove = new ArrayList<String>();
    for (Property pushed : pushedProperties.values()) {
      LOGGER.log(Level.FINE, "Evaluating {0}", pushed.getKey());

      Property currentProperty = job.getProperty(pushed.getKey());
      if (currentProperty == null) {
        LOGGER.log(Level.FINE, "Property does not exist");
        job.createProperty(pushed);
        continue;
      }

      LOGGER.log(Level.FINE, "Propagation :" + currentProperty.getPropagation() + ", merging=" + currentProperty.getMerging());
      switch (currentProperty.getPropagation()) {
        case Upstream:
        case Downstream:
          toRemove.add(pushed.getKey());
          break;
        case None:
          toRemove.add(pushed.getKey());
        case Continue:
          switch (currentProperty.getMerging()) {
            case Skip:
              // empty, don't replace existing values
              break;
            case Append:
              String appendedValue = currentProperty.getValue() + "," + pushed.getValue();
              currentProperty.setValue(appendedValue);
              break;
            case Prefix:
              String prefixedValue = pushed.getValue() + "," + currentProperty.getValue();
              currentProperty.setValue(prefixedValue);
              break;
            case Replace:
              String replacedValue = pushed.getValue();
              currentProperty.setValue(replacedValue);
              break;
          }
          break;
      }
    }

    furtherPushedProperties.putAll(pushedProperties);
    for (String name : toRemove) {
      furtherPushedProperties.remove(name);
    }
    for (Property property : job.getProperties()) {
      if (property.getPropagation() == direction) {
        LOGGER.log(Level.FINE,"Found property to push {0}",property);
        furtherPushedProperties.put(property.getKey(), property);
      }
    }

    return furtherPushedProperties;
  }

  private void mergePropertySet(PropertySet source, Job job) {
    if (source == null) {
      return;
    }
    for (Property sourceProperty : source.getProperties()) {
      if (sourceProperty.getValue() == null || sourceProperty.getValue().length() == 0) {
        job.removeProperty(sourceProperty.getKey());
        continue;
      }
      job.createProperty(sourceProperty);
    }
  }

  private String jobsAsString(Collection<Job> jobs) {
    StringBuilder sb = new StringBuilder();
    for (Job job : jobs) {
      if (sb.length() > 0) {
        sb.append(',');
      }
      sb.append(job.getName());
    }
    return sb.toString();
  }
}
