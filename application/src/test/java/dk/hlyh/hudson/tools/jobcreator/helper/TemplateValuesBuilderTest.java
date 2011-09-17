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
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TemplateValuesBuilderTest {
   
  @Test
  public void simpleProperty() throws ImportException{
    TemplateValuesBuilder builder =  new TemplateValuesBuilder();
    builder.setProperty("mykey", "myvalue");
    Map<String, Object> values = builder.getValues();
    Assert.assertEquals(values.size(), 1);
    Assert.assertTrue(values.containsKey("mykey"));
    Assert.assertTrue(values.get("mykey") instanceof String);
    Assert.assertTrue(values.get("mykey").equals("myvalue"));
  }

  @Test
  public void simplePropertyNewValue() throws ImportException{
    TemplateValuesBuilder builder =  new TemplateValuesBuilder();
    builder.setProperty("mykey", "myvalue");
    builder.setProperty("mykey", "myvalue2");
    Map<String, Object> values = builder.getValues();
    Assert.assertEquals(values.size(), 1);
    Assert.assertTrue(values.containsKey("mykey"));
    Assert.assertTrue(values.get("mykey") instanceof String);
    Assert.assertEquals(values.get("mykey"),"myvalue2");
  }  

  @Test
  public void simpleSubProperty() throws ImportException{
    TemplateValuesBuilder builder =  new TemplateValuesBuilder();
    builder.setProperty("my.key", "myvalue");   
    Map<String, Object> values = builder.getValues();
    Assert.assertEquals(values.size(), 1);
    Assert.assertTrue(values.containsKey("my"));
    Assert.assertTrue(values.get("my") instanceof Map);
    
    Map<String, Object> subValues = (Map<String, Object>)values.get("my");
    Assert.assertEquals(subValues.size(), 1);
    Assert.assertTrue(subValues.containsKey("key"));
    Assert.assertTrue(subValues.get("key") instanceof String);  
    Assert.assertEquals(subValues.get("key"),"myvalue");
  }
  
  @Test
  public void doubleSubProperty() throws ImportException {
    TemplateValuesBuilder builder =  new TemplateValuesBuilder();
    builder.setProperty("my.key", "myvalue");   
    builder.setProperty("my.key2", "myvalue2"); 
    Map<String, Object> values = builder.getValues();
    Assert.assertEquals(values.size(), 1);
    Assert.assertTrue(values.containsKey("my"));
    Assert.assertTrue(values.get("my") instanceof Map);
    
    Map<String, Object> subValues = (Map<String, Object>)values.get("my");
    Assert.assertEquals(subValues.size(), 2);
    Assert.assertTrue(subValues.containsKey("key"));
    Assert.assertTrue(subValues.containsKey("key2"));
    Assert.assertTrue(subValues.get("key") instanceof String);  
    Assert.assertTrue(subValues.get("key2") instanceof String);  
    Assert.assertEquals(subValues.get("key"),"myvalue");
    Assert.assertEquals(subValues.get("key2"),"myvalue2");
  }
  
  @Test(expectedExceptions=ImportException.class)
  public void setPropertyCannotBecomeParent() throws ImportException{
    TemplateValuesBuilder builder =  new TemplateValuesBuilder();
    builder.setProperty("mykey", "myvalue");   
    builder.setProperty("mykey.val", "myvalue");
  }  
  
  @Test(expectedExceptions=ImportException.class)
  public void setPropertyCannotBecomeParentNested() throws ImportException{
    TemplateValuesBuilder builder =  new TemplateValuesBuilder();
    builder.setProperty("mykey.folder.sub", "myvalue");   
    builder.setProperty("mykey.folder.sub.val", "myvalue");
  }    
  
  @Test(expectedExceptions=ImportException.class)
  public void parentCannotBecomeProperty() throws ImportException{
    TemplateValuesBuilder builder =  new TemplateValuesBuilder();    
    builder.setProperty("mykey.val", "myvalue");
    builder.setProperty("mykey", "myvalue");   
  }    

  @Test(expectedExceptions=ImportException.class)
  public void parentCannotBecomePropertyNested() throws ImportException{
    TemplateValuesBuilder builder =  new TemplateValuesBuilder();    
    builder.setProperty("mykey.folder.val", "myvalue");
    builder.setProperty("mykey.folder", "myvalue");   
  }    

  @Test(expectedExceptions=ImportException.class)
  public void emptyPartsNotAllowed() throws ImportException{
    TemplateValuesBuilder builder =  new TemplateValuesBuilder();    
    builder.setProperty("mykey..folder", "myvalue");   
  }      
  
  @Test(expectedExceptions=ImportException.class)
  public void keyCannotEndInDot() throws ImportException{
    TemplateValuesBuilder builder =  new TemplateValuesBuilder();    
    builder.setProperty("mykey.", "myvalue");   
  }    

  @Test(expectedExceptions=ImportException.class)
  public void keyCannotContainSpaces() throws ImportException{
    TemplateValuesBuilder builder =  new TemplateValuesBuilder();    
    builder.setProperty("my key", "myvalue");   
  }    
  
  @Test(expectedExceptions=ImportException.class)
  public void keyCannotBeNull() throws ImportException{
    TemplateValuesBuilder builder =  new TemplateValuesBuilder();    
    builder.setProperty(null, "myvalue");   
  }

  @Test(expectedExceptions=ImportException.class)
  public void keyCannotBeEmpty() throws ImportException{
    TemplateValuesBuilder builder =  new TemplateValuesBuilder();    
    builder.setProperty(" ", "myvalue");   
  }

  @Test(expectedExceptions=ImportException.class)
  public void keyCannotBeEmpty2() throws ImportException{
    TemplateValuesBuilder builder =  new TemplateValuesBuilder();    
    builder.setProperty("", "myvalue");   
  }  

}
