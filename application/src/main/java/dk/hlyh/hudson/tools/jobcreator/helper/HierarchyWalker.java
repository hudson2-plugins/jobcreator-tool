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

public final class HierarchyWalker {

  private HierarchyWalker() {
    super();
  }

  public static void walkJobs(Job job, Visitor visitor) {
    for (Job parent : job.getParentJobs()) {
      walkJobs(parent, visitor);
    }
    visitor.visitJob(job);
  }

  public static void walkEnvironments(Environment environment, Visitor visitor) {
    for (Environment parent : environment.getParentEnv()) {
      walkEnvironments(parent, visitor);
    }
    visitor.visitEnvironment(environment);
  }
}
