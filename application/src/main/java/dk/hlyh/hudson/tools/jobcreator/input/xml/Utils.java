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

final class Utils {

  private Utils() {
    super();
  }

  static dk.hlyh.hudson.tools.jobcreator.model.Propagation convertPropagation(dk.hlyh.hudson.tools.jobcreator.input.xml.model.Propagation sourcePropagation) {
    switch (sourcePropagation) {
      case Continue:
        return dk.hlyh.hudson.tools.jobcreator.model.Propagation.Continue;
      case Downstream:
        return dk.hlyh.hudson.tools.jobcreator.model.Propagation.Downstream;
      case Upstream:
        return dk.hlyh.hudson.tools.jobcreator.model.Propagation.Upstream;
      case None:
        return dk.hlyh.hudson.tools.jobcreator.model.Propagation.None;
      default:
        return dk.hlyh.hudson.tools.jobcreator.model.Propagation.Continue;
    }
  }

  static dk.hlyh.hudson.tools.jobcreator.model.Merge convertMerging(dk.hlyh.hudson.tools.jobcreator.input.xml.model.Merge sourceMerge) {
    switch (sourceMerge) {
      case Leave:
        return dk.hlyh.hudson.tools.jobcreator.model.Merge.Leave;
      case Replace:
        return dk.hlyh.hudson.tools.jobcreator.model.Merge.Replace;
      case Prefix:
        return dk.hlyh.hudson.tools.jobcreator.model.Merge.Prefix;
      case Append:
        return dk.hlyh.hudson.tools.jobcreator.model.Merge.Append;
      default:
        return dk.hlyh.hudson.tools.jobcreator.model.Merge.Replace;
    }
  }

  static dk.hlyh.hudson.tools.jobcreator.input.xml.model.Group findEnvironment(dk.hlyh.hudson.tools.jobcreator.input.xml.model.Pipeline sourcePipeline, String name) {
    for (dk.hlyh.hudson.tools.jobcreator.input.xml.model.Group currentEnvironment : sourcePipeline.getEnvironments()) {
      if (currentEnvironment.getName().equals(name)) {
        return currentEnvironment;
      }
    }
    return null;
  }

  static dk.hlyh.hudson.tools.jobcreator.input.xml.model.Job findJob(dk.hlyh.hudson.tools.jobcreator.input.xml.model.Pipeline sourcePipeline, String name) {
    for (dk.hlyh.hudson.tools.jobcreator.input.xml.model.Job currentJob : sourcePipeline.getJobs()) {
      if (currentJob.getName().equals(name)) {
        return currentJob;
      }
    }
    return null;
  }
}
