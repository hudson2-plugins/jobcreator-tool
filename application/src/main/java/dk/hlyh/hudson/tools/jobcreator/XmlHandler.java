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

import dk.hlyh.hudson.tools.jobcreator.schema.v1.Pipeline;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

public class XmlHandler {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private static final String SCHEMA_FILE = "/jobcreator-v1.xsd";
  private static final Class ROOT_NODE = dk.hlyh.hudson.tools.jobcreator.schema.v1.Pipeline.class;

  public XmlHandler() {
    super();
  }

  public Pipeline loadPipeline(File file) throws ImportException {
    if (file == null) {
      throw new IllegalArgumentException("File cannot be null");
    }
    JAXBContext jc = null;
    try {
      JAXBContext context = JAXBContext.newInstance(ROOT_NODE);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      Schema schema = loadSchema();
      unmarshaller.setSchema(schema);
      Pipeline pipeline = (Pipeline) unmarshaller.unmarshal(new FileInputStream(file));
      LOGGER.info("Loaded pipeline from " + file);
      return pipeline;
    } catch (FileNotFoundException ex) {
      throw new ImportException("Could not find pipeline file: " + file);
    } catch (UnmarshalException ex) {
      if (ex.getCause() instanceof org.xml.sax.SAXParseException ) {
        LOGGER.severe("Failed to validate the pipeline definition against the schema: " + ex.getCause().getMessage());
      } else {
        LOGGER.severe("Failed to validate the pipeline definition against the schema" + ex);
      }
      throw new ImportException("Failed to validate the pipeline definition against the schema" + ex);
    } catch (JAXBException ex) {
      throw new ImportException("Failed to configure JAXB", ex);
    }
  }

  private Schema loadSchema() throws ImportException {
    try {
      SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      URL schemaUrl = this.getClass().getResource(SCHEMA_FILE);
      if (schemaUrl == null) {
        throw new ImportException("Could not find schema file " + SCHEMA_FILE);
      }
      Schema schema = schemaFactory.newSchema(new File(schemaUrl.getFile()));
      return schema;
    } catch (SAXException ex) {
      throw new ImportException("Could not load schema file for validation", ex);
    }
  }
}
