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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.LogRecord;

public class LogFormatter extends java.util.logging.Formatter {

  private static final String lineSep = System.getProperty("line.separator");

  public LogFormatter() {
    super();
  }  
  
  @Override
  public String format(LogRecord record) {
    StringBuilder sb = new StringBuilder();
    sb.append(record.getLevel()).append(": ");
    sb.append(formatMessage(record));
    sb.append(lineSep);

    if (record.getThrown() != null) {
      System.out.println("Has exception");
      StringWriter sink = new StringWriter();
      record.getThrown().printStackTrace(new PrintWriter(sink, true));
      sb.append(sink.toString());
    }


    return sb.toString();
  }
}
