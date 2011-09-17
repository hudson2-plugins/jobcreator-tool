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

import dk.hlyh.hudson.tools.jobcreator.ImportException;
import java.io.File;
import org.testng.annotations.Test;

public class FreemarkerHelperTest {
  
 
  @Test
  public void validTemplateDir() throws ImportException {
    File tmpDir = new File(System.getProperty("java.io.tmpdir"));
    FreemarkerHelper.setupFreemarker(tmpDir);    
  }

  @Test(expectedExceptions=ImportException.class)
  public void nullTemplateDir() throws ImportException {
    //File tmpDir = new File(System.getProperty("java.io.tmpdir"));
    FreemarkerHelper.setupFreemarker(null);    
  } 
  
  @Test(expectedExceptions=ImportException.class)
  public void invalidTemplateDir() throws ImportException {
    File tmpDir = new File("/not-there");
    
    FreemarkerHelper.setupFreemarker(tmpDir);    
  }    
  
}
