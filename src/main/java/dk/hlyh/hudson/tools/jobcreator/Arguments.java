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
  
  @Option(name = "--group", aliases="-g", required = true, usage = "Group to load")
  private String group;
  
//  @Option(name = "--override", required = false, usage = "Personal override file")
//  private File overrideFile;

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
    return group;
  }

  public File getOutputDirectory() {
    return outputDirectory;
  }

//  public File getOverrideFile() {
//    return overrideFile;
//  }

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

  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  public void setEnvironment(String environment) {
    this.group = environment;
  }

  public void setInputFormat(InputFormat inputFormat) {
    this.inputFormat = inputFormat;
  }

  public void setOutputDirectory(File outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

//  public void setOverrideFile(File overrideFile) {
//    this.overrideFile = overrideFile;
//  }

  public void setQuiet(boolean quiet) {
    this.quiet = quiet;
  }

  public void setTemplateDirectory(File templateDirectory) {
    this.templateDirectory = templateDirectory;
  }

  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }
  
  public void setInput(File file) {
    input = file;
  }
    
  public enum InputFormat {
    xmlv1,
    //yaml,    
  }
}
