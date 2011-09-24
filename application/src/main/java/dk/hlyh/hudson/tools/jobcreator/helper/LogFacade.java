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

import java.util.logging.Level;
import java.util.logging.Logger;

public final class LogFacade {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private LogFacade() {
    super();
  }

  public static void severe(String msg, Object... parameters) {
    StackTraceElement[] stack= Thread.currentThread().getStackTrace();
    LOGGER.logp(Level.SEVERE, stack[2].getClassName(),stack[2].getMethodName(), msg, parameters);
  }
  
  public static void severe(Throwable exception) {
    StackTraceElement[] stack= Thread.currentThread().getStackTrace();
    LOGGER.logp(Level.SEVERE, stack[2].getClassName(),stack[2].getMethodName(),exception.getMessage(), exception);
  }  

  public static void warning(String msg, Object... parameters) {
    StackTraceElement[] stack= Thread.currentThread().getStackTrace();
    LOGGER.logp(Level.WARNING, stack[2].getClassName(),stack[2].getMethodName(),msg, parameters);
  }

  public static void info(String msg, Object... parameters) {
    LOGGER.log(Level.INFO, msg, parameters);
  }
  
  public static void verbose(String msg, Object... parameters) {
    LOGGER.log(Level.FINE, msg, parameters);
  }


}
