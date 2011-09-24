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
import dk.hlyh.hudson.tools.jobcreator.input.xml.model.Pipeline;
import java.io.File;
import java.net.URL;
import org.testng.Assert;
import org.testng.annotations.Test;

public class XmlLoadTest {

  @Test
  public void loadValidFile() throws ImportException {
    URL resource = this.getClass().getResource("/xmlloadertest/valid.xml");
//    Loader handler = new Loader();
//    Assert.assertNotNull(handler.loadPipeline(new File(resource.getFile())));
  }

//  @Test(expectedExceptions=ImportException.class)
//  public void loadInvalidFile()  throws ImportException{
//    URL resource = this.getClass().getResource("/xmlloadertest/invalid.xml");
////    Loader handler = new Loader();
////    Pipeline loadPipeline = handler.loadPipeline(new File(resource.getFile()));
//  }
//  
//  @Test(expectedExceptions=IllegalArgumentException.class)
//  public void loadNullFile()  throws ImportException{
//    
////    Loader handler = new Loader();
////    Pipeline loadPipeline = handler.loadPipeline(null);
//  }
//  
//  @Test(expectedExceptions=ImportException.class)
//  public void loadNonExistingFile()  throws ImportException{
//    
////    Loader handler = new Loader();
////    Pipeline loadPipeline = handler.loadPipeline(new File("notthere.xsd"));
//  }    
  
}
