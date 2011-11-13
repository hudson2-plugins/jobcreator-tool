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

import dk.hlyh.hudson.tools.jobcreator.Arguments;
import dk.hlyh.hudson.tools.jobcreator.model.Environment;
import dk.hlyh.hudson.tools.jobcreator.model.Job;
import dk.hlyh.hudson.tools.jobcreator.model.Merge;
import dk.hlyh.hudson.tools.jobcreator.model.Pipeline;
import dk.hlyh.hudson.tools.jobcreator.model.Propagation;
import dk.hlyh.hudson.tools.jobcreator.model.Property;
import java.io.File;
import java.net.URL;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JobInheritenceTest {

  @Test
  public void loadJob3() {
    URL resource = this.getClass().getResource("/xmlloadertest/inheritence.xml");
    Arguments args = new Arguments();
    args.setInput(new File(resource.getFile()));
    args.setEnvironment("p1-first");
    XmlLoader handler = new XmlLoader(args);
    
    Pipeline pipeline = handler.loadPipeline();
    Assert.assertNotNull(pipeline);
    Job foundJob = null;
    for (Job job : pipeline.getJobs()) {
      if (job.getName().equals("job3")) {
        foundJob = job;
        break;
      }      
    }
    Assert.assertNotNull(foundJob);
    Assert.assertEquals(foundJob.getTemplate(), "template2.ftl");
  }
  
  @Test
  public void loadJob4() {
    URL resource = this.getClass().getResource("/xmlloadertest/inheritence.xml");
    Arguments args = new Arguments();
    args.setInput(new File(resource.getFile()));
    args.setEnvironment("p1-first");
    XmlLoader handler = new XmlLoader(args);
    Pipeline pipeline = handler.loadPipeline();
    Assert.assertNotNull(pipeline);
    Job foundJob = null;
    for (Job job : pipeline.getJobs()) {
      if (job.getName().equals("job4")) {
        foundJob = job;
        break;
      }      
    }
    Assert.assertNotNull(foundJob);
    Assert.assertEquals(foundJob.getTemplate(), "template1.ftl");
  }
  
  
}
