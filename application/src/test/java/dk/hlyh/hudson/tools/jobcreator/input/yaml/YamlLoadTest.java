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
import java.io.File;
import java.net.URL;
import org.testng.annotations.Test;

public class YamlLoadTest {

  @Test
  public void loadValidFile() throws ImportException {
    System.out.println("************* loadValidFile");
    URL resource = this.getClass().getResource("/yamlloadertest/valid.yml");
    
    Arguments args = new Arguments();
    args.setInput(new File(resource.getFile()));
    YamlLoader handler = new YamlLoader(args);
    Object result= handler.loadFile(new File(resource.getFile()));
    System.out.println(result);
    System.out.println(result.getClass());
  }
//  @Test(expectedExceptions = ImportException.class)
//  public void loadInvalidFile() throws ImportException {
//    URL resource = this.getClass().getResource("/xmlloadertest/invalid.xml");
//    Arguments args = new Arguments();
//    args.setInput(new File(resource.getFile()));
//    XmlLoader handler = new XmlLoader(args);
//    handler.loadXml();
//  }
//
//  @Test(expectedExceptions = ImportException.class)
//  public void loadNullFile() throws ImportException {
//    Arguments args = new Arguments();
//    XmlLoader handler = new XmlLoader(args);
//    handler.loadXml();
//  }
//
//  @Test(expectedExceptions = ImportException.class)
//  public void loadNonExistingFile() throws ImportException {
//    Arguments args = new Arguments();
//    args.setInput(new File("notthere.xsd"));
//    XmlLoader handler = new XmlLoader(args);
//    handler.loadXml();
//  }
}
