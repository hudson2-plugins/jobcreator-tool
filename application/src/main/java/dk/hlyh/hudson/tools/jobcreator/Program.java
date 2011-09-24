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

import dk.hlyh.hudson.tools.jobcreator.input.xml.Loader;
import dk.hlyh.hudson.tools.jobcreator.helper.LogFormatter;
import dk.hlyh.hudson.tools.jobcreator.model.Pipeline;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class Program {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private Program() {
    super();
  }

  public static void main(String[] args) {

    Arguments arguments = new Arguments();
    CmdLineParser parser = new CmdLineParser(arguments);
    try {
      parser.parseArgument(args);
      // reconfigure logging

      for (Handler handler : Logger.getLogger("").getHandlers()) {
        handler.setFormatter(new LogFormatter());        
      }
      Loader xmlHandler = new Loader(arguments);
      Pipeline pipeline = xmlHandler.loadPipeline();
      Resolver resolver = new Resolver(arguments, pipeline);
      resolver.resolve();

    } catch (CmdLineException e) {
      System.err.println(e.getMessage());
      parser.printUsage(System.err);
      return;
    } catch (ImportException e) {      
      System.err.println("Import aborted because of errors");
      System.exit(-1);      
    }
  }
}
