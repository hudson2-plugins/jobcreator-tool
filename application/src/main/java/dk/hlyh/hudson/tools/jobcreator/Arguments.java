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

import java.io.File;
import org.kohsuke.args4j.Option;

public class Arguments {

  @Option(name="--format", required=false, usage="Specify which format to use for input, xml is the default")
  private InputFormat inputFormat;
  
  @Option(name = "--input", aliases="-i", required = true, usage = "Which file to use for pipeline definition")
  private File input;
  
  @Option(name = "--template", required = true, usage = "Directory containing the templates")
  private File templateDirectory;
  
  @Option(name = "--output", aliases="-o", required = true, usage = "output directory")
  private File outputDirectory;
  
  @Option(name = "--environment", aliases="-e", required = true, usage = "Environemnt to load")
  private String environment;
  
  @Option(name = "--override", required = false, usage = "Personal override file")
  private File overrideFile;

  @Option(name="--quiet")
  private boolean quiet;
  
  @Option(name="--verbose", aliases="-v")
  private boolean verbose;
  
  @Option(name="--debug", aliases="-X")
  private boolean debug;
  
  public Arguments() {
    super();
  }

  public String getEnvironment() {
    return environment;
  }

  public File getOutputDirectory() {
    return outputDirectory;
  }

  public File getOverrideFile() {
    return overrideFile;
  }

  public File getInput() {
    return input;
  }

  public File getTemplateDirectory() {
    return templateDirectory;
  }

  public InputFormat getInputFormat() {    
    return inputFormat != null ? inputFormat : InputFormat.xmlv1;
  }

  public boolean isDebug() {
    return debug;
  }

  public boolean isVerbose() {
    return verbose;
  }

  public boolean isQuiet() {
    return quiet;
  }
    
  public enum InputFormat {
    xmlv1,
    yaml,    
  }
}
