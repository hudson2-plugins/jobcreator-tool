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
package dk.hlyh.hudson.tools.jobcreator.schema.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum Merge {
    
    @XmlEnumValue(value="leave")
    Leave,
      
    @XmlEnumValue(value="replace")
    Replace,
    
    @XmlEnumValue(value="append")                
    Append,       
    
    @XmlEnumValue(value="prefix")
    Prefix
    
}
