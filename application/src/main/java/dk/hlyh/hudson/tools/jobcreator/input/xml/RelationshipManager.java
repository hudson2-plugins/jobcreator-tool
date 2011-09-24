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

import java.util.List;

public class RelationshipManager {

  void setRelationships(List<dk.hlyh.hudson.tools.jobcreator.model.Job> activeJobs, dk.hlyh.hudson.tools.jobcreator.input.xml.model.Pipeline sourcePipeline) {
    for (dk.hlyh.hudson.tools.jobcreator.model.Job activeJob : activeJobs) {
      dk.hlyh.hudson.tools.jobcreator.input.xml.model.Job sourceJob = Utils.findJob(sourcePipeline, activeJob.getName());

      // set upstream/downstream
      for (dk.hlyh.hudson.tools.jobcreator.input.xml.model.Job downstreamSource : sourceJob.getDownstreamJobs()) {

        dk.hlyh.hudson.tools.jobcreator.model.Job activeDownStream = findActiveJob(activeJobs, downstreamSource.getName());
        if (activeDownStream != null) {
          activeJob.getDownstream().add(activeDownStream);
          activeDownStream.getUpstream().add(activeJob);
        }
      }

      // join relationship
      if (sourceJob.getJoinJob() != null) {
        dk.hlyh.hudson.tools.jobcreator.model.Job activeJoin = findActiveJob(activeJobs, sourceJob.getJoinJob().getName());
        if (activeJoin != null) {
          activeJob.setJoin(activeJoin);
        }
      }

    }
  }

  private dk.hlyh.hudson.tools.jobcreator.model.Job findActiveJob(List<dk.hlyh.hudson.tools.jobcreator.model.Job> activeJobs, String name) {
    for (dk.hlyh.hudson.tools.jobcreator.model.Job currentJob : activeJobs) {
      if (currentJob.getName().equals(name)) {
        return currentJob;
      }
    }
    return null;
  }
}
