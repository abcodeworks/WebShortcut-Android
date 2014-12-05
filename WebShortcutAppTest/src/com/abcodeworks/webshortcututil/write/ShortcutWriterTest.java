/**
 * Copyright 2014 by Andre Beckus
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.abcodeworks.webshortcututil.write;

import static org.junit.Assert.assertEquals;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.abcodeworks.webshortcututil.read.ShortcutReadException;
import com.abcodeworks.webshortcututil.write.DesktopShortcutWriter;
import com.abcodeworks.webshortcututil.write.ShortcutWriter;
import com.abcodeworks.webshortcututil.write.UrlShortcutWriter;
import com.abcodeworks.webshortcututil.write.WeblocBinaryShortcutWriter;
import com.abcodeworks.webshortcututil.write.WeblocXmlShortcutWriter;

public class ShortcutWriterTest {
    @Rule
    public ExpectedException thrown= ExpectedException.none();
    
    void testCreateFilename(ShortcutWriter writer, String expectedBase, String expectedExtension, String name)
    {
        String expectedFull = expectedBase + "." + expectedExtension;
        
        assertEquals(expectedBase, writer.createBaseFilename(name));
        assertEquals(expectedFull, writer.createFullFilename(name));
    }
    
    void testCreateFilename(ShortcutWriter writer, String expectedBase, String expectedExtension, String name, int length)
    {
        String expectedFull = expectedBase + "." + expectedExtension;
        
        assertEquals(expectedBase, writer.createBaseFilename(name, length));
        assertEquals(expectedFull, writer.createFullFilename(name, length));
    }
    
    @Test
    public void testCreateFilename()
    {
        ShortcutWriter desktopWriter = new DesktopShortcutWriter();
        ShortcutWriter urlWriter = new UrlShortcutWriter();
        ShortcutWriter weblocBinWriter = new WeblocBinaryShortcutWriter();
        ShortcutWriter weblocXmlWriter = new WeblocXmlShortcutWriter();
        
        //Generate desktop filename
        testCreateFilename(desktopWriter, "file", "desktop", "file");
        
        //Generate url filename
        testCreateFilename(urlWriter, "file", "url", "file");
        
        //Generate binary webloc filename
        testCreateFilename(weblocBinWriter, "file", "webloc", "file");
        
        //Generate xml webloc filename
        testCreateFilename(weblocXmlWriter, "file", "webloc", "file");
        
        
        //Generate truncated filename
        testCreateFilename(desktopWriter, "f", "desktop", "file", 9);
        
        //Generate max truncated filename
        testCreateFilename(desktopWriter, "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012", "desktop", "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        
        //Generate minimum undef filename
        testCreateFilename(desktopWriter, "_", "desktop", null, 9);
        
        //Generate minimum undef filename
        testCreateFilename(desktopWriter, "_", "desktop", "", 9);
        
        //Generate filename allowed special characters
        testCreateFilename(desktopWriter, " !#$&'()+,-.09;=@AZ[]_`az{}~中", "desktop", " !#$&'()+,-.09;=@AZ[]_`az{}~中");
        
        //Generate filename all characters
        testCreateFilename(desktopWriter, " !#$&'()+,-.09;=@AZ[]_`az{}~中", "desktop", " !\"#$%&'()*+,-./09:;<=>?@AZ[\\]^_`az{|}~中");
    }
    
    @Test
    public void testCreateBaseFilenameTooShort()
            throws FileNotFoundException,
                   ShortcutReadException,
                   IOException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("maxLength must be greater than or equal to 9");
        (new DesktopShortcutWriter()).createBaseFilename("file", 8);
    }
    
    @Test
    public void testCreateFullFilenameTooShort()
            throws FileNotFoundException,
                   ShortcutReadException,
                   IOException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("maxLength must be greater than or equal to 9");
        (new DesktopShortcutWriter()).createFullFilename("file", 8);
    }
}
