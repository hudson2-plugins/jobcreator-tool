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
import dk.hlyh.hudson.tools.jobcreator.ImportException;
import dk.hlyh.hudson.tools.jobcreator.helper.LogFacade;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlLoader {

  private static final String SCHEMA_FILE = "/jobcreator-v1.xsd";
  private static final Class ROOT_NODE = dk.hlyh.hudson.tools.jobcreator.input.xml.model.Pipeline.class;
  private final Arguments arguments;
  private EnvironmentTransformer envTransformer = new EnvironmentTransformer();
  private JobTransformer jobTransformer = new JobTransformer();
  private RelationshipManager relationShipManager = new RelationshipManager();

  public XmlLoader(Arguments args) {
    super();
    this.arguments = args;
  }

  public dk.hlyh.hudson.tools.jobcreator.model.Pipeline loadPipeline() {
    LogFacade.info("Loading pipeline definition using xml version 1 format");

    dk.hlyh.hudson.tools.jobcreator.model.Environment activeEnvironment = null;
    List<dk.hlyh.hudson.tools.jobcreator.model.Job> activeJobs = new ArrayList<dk.hlyh.hudson.tools.jobcreator.model.Job>();

    // Load and unmarshal the xml file
    dk.hlyh.hudson.tools.jobcreator.input.xml.model.Pipeline sourcePipeline = loadXml();

    // find the environment to generate
    dk.hlyh.hudson.tools.jobcreator.input.xml.model.Environment sourceEnv = Utils.findEnvironment(sourcePipeline, arguments.getEnvironment());
    if (sourceEnv == null) {
      throw new ImportException("Environment '"+arguments.getEnvironment()+"' not found");
    }
    // Build the active environment by collapsing the inheritence tree
    envTransformer.transformEnvironment(sourceEnv);
    activeEnvironment = envTransformer.getActiveEnvironment();

    // for each job, build the job by collapsing the inheritence tree
    for (String jobName : envTransformer.getIncludedJobs()) {
      jobTransformer.transformJob(Utils.findJob(sourcePipeline, jobName));
      activeJobs.add(jobTransformer.getActiveJob());
    }

    // set the upstream/downstream relationships
    relationShipManager.setRelationships(activeJobs, sourcePipeline);

    dk.hlyh.hudson.tools.jobcreator.model.Pipeline activePipeline = new dk.hlyh.hudson.tools.jobcreator.model.Pipeline(sourcePipeline.getName(), activeEnvironment, activeJobs);
    return activePipeline;
  }

  dk.hlyh.hudson.tools.jobcreator.input.xml.model.Pipeline loadXml() {
    File inputFile = arguments.getInput();

    if (inputFile == null || !inputFile.exists()) {
      throw new ImportException("Pipline file '" + inputFile + "' cannot be found");
    }

    try {
      JAXBContext context = JAXBContext.newInstance(ROOT_NODE);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      Schema schema = loadSchema();
      unmarshaller.setSchema(schema);
      dk.hlyh.hudson.tools.jobcreator.input.xml.model.Pipeline pipeline = (dk.hlyh.hudson.tools.jobcreator.input.xml.model.Pipeline) unmarshaller.unmarshal(new FileInputStream(inputFile));
      LogFacade.info("Successfully loaded pipline from {0}", inputFile);
      return pipeline;
    } catch (FileNotFoundException ex) {
      throw new ImportException("Could not find pipeline file: " + inputFile);
    } catch (UnmarshalException ex) {
      if (ex.getCause() instanceof org.xml.sax.SAXParseException) {
        SAXParseException pe = (SAXParseException) ex.getCause();
        LogFacade.severe("Failed to validate the pipeline definition against the schema: \"{0}\"  at line {1}, column={2}", pe.getMessage(), pe.getLineNumber(), pe.getColumnNumber());
      } else {
        LogFacade.severe(ex);
      }
      throw new ImportException("Failed to validate the pipeline definition against the schema" + ex);
    } catch (JAXBException ex) {
      throw new ImportException("Failed to configure JAXB", ex);
    }
  }

  private Schema loadSchema() {
    try {
      SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      URL schemaUrl = XmlLoader.class.getResource(SCHEMA_FILE);
      if (schemaUrl == null) {
        throw new ImportException("Could not find schema file " + SCHEMA_FILE);
      }
      return schemaFactory.newSchema(schemaUrl);
    } catch (SAXException ex) {
      throw new ImportException("Could not load schema file for validation", ex);
    }
  }
}
