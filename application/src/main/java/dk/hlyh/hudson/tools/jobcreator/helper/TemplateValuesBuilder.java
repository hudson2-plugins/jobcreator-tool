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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class TemplateValuesBuilder {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private Map<String, Object> values;

  public TemplateValuesBuilder() {
    values = new HashMap<String, Object>();
  }

  public void setProperty(String key, String value) {
    LOGGER.info("Setting property '" + key + "' , '" + value + "'");
    String[] keyParts = key.split("\\.");

    Map<String, Object> currentMap = values;
    for (int i = 0; i < keyParts.length; i++) {
      String part = keyParts[i];

      // if this is not the last part
      if (i + 1 != keyParts.length) {
        Object mapContent = currentMap.get(part);
        // if the current map has value
        if (mapContent == null) {
          Map<String, Object> newMap = new HashMap<String, Object>();
          currentMap.put(part, newMap);
          currentMap = newMap;

        } else if (mapContent instanceof Map) {
          currentMap = (Map<String, Object>) mapContent;
        } else {
          throw new RuntimeException("Child is not map");
        }
      } else {
        currentMap.put(part, value);
      }
    }

  }

  public Map getValues() {
    return values;
  }

  /*  public static void mergeMaps(Map<String, Object> original, Map<String, Object> addition) {
  if (addition == null) {
  return;
  }
  for (Map.Entry<String, Object> additionEntry : addition.entrySet()) {
  
  String additionKey = additionEntry.getKey();
  Object additionValue = additionEntry.getValue();
  Object originalValue = original.get(additionKey);
  // make type check
  if (originalValue != null) {
  if (!(additionValue instanceof String || additionValue instanceof Map)) {
  throw new IllegalStateException("Error in config addition=" + additionValue.getClass());
  }
  if (additionValue instanceof String && !(originalValue instanceof String)) {
  throw new IllegalStateException("Error in config original class=" + originalValue.getClass() + " value=" + originalValue + ", addition class=" + additionValue.getClass() + ", value=" + additionValue);
  }
  if (additionValue instanceof Map && !(originalValue instanceof Map)) {
  throw new IllegalStateException("Error in config original=" + originalValue.getClass() + ", addition=" + additionValue.getClass());
  }
  }
  
  
  if (additionValue instanceof String) {
  original.put(additionKey, additionValue);
  } else {
  if (originalValue == null) {
  originalValue = new HashMap<String, Object>();
  original.put(additionKey, originalValue);
  }
  mergeMaps((Map<String, Object>) originalValue, (Map<String, Object>) additionValue);
  }
  }
  }*/
}
