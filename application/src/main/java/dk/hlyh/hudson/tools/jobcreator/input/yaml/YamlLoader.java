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
package dk.hlyh.hudson.tools.jobcreator.input.yaml;

import dk.hlyh.hudson.tools.jobcreator.Arguments;
import dk.hlyh.hudson.tools.jobcreator.ImportException;
import dk.hlyh.hudson.tools.jobcreator.helper.LogFacade;
import dk.hlyh.hudson.tools.jobcreator.model.Group;
import dk.hlyh.hudson.tools.jobcreator.model.Pipeline;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yaml.snakeyaml.Yaml;

public class YamlLoader {

  private transient final Arguments arguments;
  private Map<String, Group> foundEnvironments = new HashMap<String, Group>();
  private Map<String, Group> foundJobs = new HashMap<String, Group>();  
  private String pipelineName = "unknown";
  private List<String> activeJobNames = new ArrayList<String>();
          
//  private EnvironmentTransformer envTransformer = new EnvironmentTransformer();
//  private JobTransformer jobTransformer = new JobTransformer();
//  private RelationshipManager relationShipManager = new RelationshipManager();

  public YamlLoader(Arguments args) {
    super();
    this.arguments = args;
  }

  public Pipeline loadPipeline() {
    LogFacade.info("Loading pipeline definition using yaml version 1 format");

    dk.hlyh.hudson.tools.jobcreator.model.Group activeEnvironment = null;
    List<dk.hlyh.hudson.tools.jobcreator.model.Job> activeJobs = new ArrayList<dk.hlyh.hudson.tools.jobcreator.model.Job>();

    Deque<Map<String, Object>> loadedFiles = new ArrayDeque<Map<String, Object>>();
    Queue<File> filesToLoad = new ArrayDeque<File>();
    filesToLoad.add(arguments.getInput());

    // load all files in the order discovered
    while (!filesToLoad.isEmpty()) {
      File file = filesToLoad.poll();
      Map<String, Object> loadedFile = loadFile(file);
      loadedFiles.push(loadedFile);
      List<String> fileList = getListEntries(loadedFile, "imports", "file");
      for (String fileName : fileList) {
        filesToLoad.add(new File(fileName));
      }
    }

    //parse the files in opposite order so references can be resolved    
    while (!loadedFiles.isEmpty()) {
      Map<String, Object> loadedFile = loadedFiles.pop();
      parseFile(loadedFile);
      pipelineName = getPipelineName(loadedFile);
    }

    // find the environment to generate
    activeEnvironment = foundEnvironments.get(arguments.getEnvironment());
    if (activeEnvironment == null) {
      throw new ImportException("Could not find environment with name '" + arguments.getEnvironment() + "'");
    }

    // Build the active environment by collapsing the inheritence tree

    // for each job, build the job by collapsing the inheritence tree

    // set the upstream/downstream relationships

    // return pipeline
    return new Pipeline(pipelineName, activeEnvironment, activeJobs);
  }

  Map<String, Object> loadFile(File file) throws ImportException {
    try {
      FileInputStream fis = new FileInputStream(file);
      Yaml yaml = new Yaml();
      Map<String, Object> loaded = (Map<String, Object>) yaml.load(fis);
      return loaded;
    } catch (FileNotFoundException ex) {
      Logger.getLogger(YamlLoader.class.getName()).log(Level.SEVERE, null, ex);
      throw new ImportException("Failed to load yaml object");
    }
  }

  private void parseFile(Map<String, Object> loadedFile) {
    for (Map<String, Object> environment : getListEntries(loadedFile, "environments")) {
      parseEnvironment(environment);
    }
    for (Map<String, Object> job : getListEntries(loadedFile, "jobs")) {
      parseJob(job);
    }
  }

  private void parseEnvironment(Map<String, Object> environment) {
    List<String> includedJobs = getListEntries(environment, "included", "job");    
    List<String> excludedJobs = getListEntries(environment, "excluded", "job");
    List<String> parentEnvironments = getListEntries(environment, "parents", "environment");
    activeJobNames.addAll(includedJobs);
    activeJobNames.removeAll(excludedJobs);
            
  }

  private void parseJob(Map<String, Object> job) {
  }

  private String getPipelineName(Map<String, Object> loadedFile) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  private List<String> getListEntries(Map<String, Object> parent, String holderName, String key) {
    List<String> result = new ArrayList<String>();

    if (!parent.containsKey(holderName)) {
      return result;
    }

    List<Map> entries = (List<Map>) parent.get(holderName);
    for (Map entry : entries) {
      String entryValue = (String) entry.get(key);
      if (entryValue != null) {
        result.add(entryValue);
      }
    }
    return result;
  }

  private List<Map<String, Object>> getListEntries(Map<String, Object> parent, String holderName) {
    if (!parent.containsKey(holderName)) {
      return new ArrayList<Map<String, Object>>();
    }

    return (List<Map<String, Object>>) parent.get(holderName);
  }
}
