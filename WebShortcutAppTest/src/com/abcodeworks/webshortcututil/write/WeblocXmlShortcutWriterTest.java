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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.abcodeworks.webshortcututil.read.ShortcutReadException;
import com.abcodeworks.webshortcututil.write.FileAlreadyExistsException;
import com.abcodeworks.webshortcututil.write.ShortcutWriteException;
import com.abcodeworks.webshortcututil.write.ShortcutWriter;
import com.abcodeworks.webshortcututil.write.WeblocXmlShortcutWriter;

import static com.abcodeworks.webshortcututil.ShortcutTestHelper.testWriteShortcut;

public class WeblocXmlShortcutWriterTest {
    ShortcutWriter writer = null;
    
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    
    @Before
    public void setUp() throws Exception {
        writer = new WeblocXmlShortcutWriter();
    }
    
    @Test
    public void testWrite()
                  throws FileNotFoundException,
                         ShortcutReadException,
                         IOException,
                         FileAlreadyExistsException,
                         ShortcutWriteException
    {
        File folder = tempFolder.newFolder("weblocxml");
        testWriteShortcut(writer, folder, "Google", "http://www.google.com");
        testWriteShortcut(writer, folder, " !#$&'()+,-.09;=@AZ[]_`az{}~", "http://www.google.com");
    }
    
    @Test
    public void testWriteNonAscii()
                  throws FileNotFoundException,
                         ShortcutReadException,
                         IOException,
                         FileAlreadyExistsException,
                         ShortcutWriteException
    {
        File folder = tempFolder.newFolder("weblocxml_nonascii");
        testWriteShortcut(writer, folder, "导航.中国", "http://导航.中国/");
    }
}
