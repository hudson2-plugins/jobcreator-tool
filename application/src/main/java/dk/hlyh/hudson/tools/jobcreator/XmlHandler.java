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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XmlHandler {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public XmlHandler() {
    super();
  }

  public Pipeline loadPipeline(File file) {
    try {
      JAXBContext jc = JAXBContext.newInstance(dk.hlyh.hudson.tools.jobcreator.schema.v1.Pipeline.class);
      Unmarshaller u = jc.createUnmarshaller();
      Pipeline pipeline = (Pipeline) u.unmarshal(new FileInputStream(file));
      LOGGER.info("Loaded pipeline from "+file);
      return pipeline;
    } catch (FileNotFoundException ex) {
      LOGGER.log(Level.SEVERE,"File not found " +file, ex);
    } catch (JAXBException ex) {
      LOGGER.log(Level.SEVERE,"Unmarshal exception", ex);
    }
    return null;
  }
}
