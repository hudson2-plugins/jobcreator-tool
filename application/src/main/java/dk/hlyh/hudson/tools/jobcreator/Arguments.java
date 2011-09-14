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
    
    @Option(name="--pipeline",required=true,usage="Which xml file to use for pipeline definition")
    private File pipeline;

    @Option(name="--template",required=true,usage="directory containing the templates")
    private File templateDirectory;
        
    @Option(name="--output",required=true,usage="output directory")
    private File outputDirectory;

    @Option(name="--environment",required=true,usage="environemnt to load")
    private String environment;
    
    @Option(name="--override",required=false,usage="Personal override file")
    private File overrideFile;
         
    public String getEnvironment() {
        return environment;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public File getOverrideFile() {
        return overrideFile;
    }

    public File getPipeline() {
        return pipeline;
    }

    public File getTemplateDirectory() {
        return templateDirectory;
    }
    
    
}
            
