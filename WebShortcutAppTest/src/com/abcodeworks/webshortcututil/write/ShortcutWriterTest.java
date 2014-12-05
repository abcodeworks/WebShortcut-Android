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
    
    @Test
    public void testCreateFilename()
    {
        ShortcutWriter desktopWriter = new DesktopShortcutWriter();
        ShortcutWriter urlWriter = new UrlShortcutWriter();
        ShortcutWriter weblocBinWriter = new WeblocBinaryShortcutWriter();
        ShortcutWriter weblocXmlWriter = new WeblocXmlShortcutWriter();
        
        assertEquals("Generate desktop filename", "file.desktop", desktopWriter.createFilename("file"));
        assertEquals("Generate url filename", "file.url", urlWriter.createFilename("file"));
        assertEquals("Generate binary webloc filename", "file.webloc", weblocBinWriter.createFilename("file"));
        assertEquals("Generate xml webloc filename", "file.webloc", weblocXmlWriter.createFilename("file"));
        
        assertEquals("Generate truncated filename", "f.desktop", desktopWriter.createFilename("file", 9));
        assertEquals("Generate max truncated filename", "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012.desktop", desktopWriter.createFilename("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"));
        assertEquals("Generate minimum undef filename", "_.desktop", desktopWriter.createFilename(null, 9));
        assertEquals("Generate minimum undef filename", "_.desktop", desktopWriter.createFilename("", 9));
        assertEquals("Generate filename allowed special characters", " !#$&'()+,-.09;=@AZ[]_`az{}~中.desktop", desktopWriter.createFilename(" !#$&'()+,-.09;=@AZ[]_`az{}~中"));
        assertEquals("Generate filename all characters", " !#$&'()+,-.09;=@AZ[]_`az{}~中.desktop", desktopWriter.createFilename(" !\"#$%&'()*+,-./09:;<=>?@AZ[\\]^_`az{|}~中"));
    }
    
    @Test
    public void testCreateFilenameTooShort()
            throws FileNotFoundException,
                   ShortcutReadException,
                   IOException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("maxLength must be greater than or equal to 9");
        (new DesktopShortcutWriter()).createFilename("file", 8);
    }
}
