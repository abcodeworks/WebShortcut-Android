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

package com.abcodeworks.webshortcututil.read;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.*;
import org.junit.rules.TemporaryFolder;

import com.abcodeworks.webshortcututil.read.ShortcutContents;
import com.abcodeworks.webshortcututil.read.ShortcutReadException;
import com.abcodeworks.webshortcututil.read.ShortcutReader;

import java.io.File;

public class ShortcutReaderTest
{
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    final ShortcutReader reader = new ShortcutReaderChild();
    
    final String urlString = "urlString";
    
    private class ShortcutReaderChild
              extends ShortcutReader {
        public String readUrlString(InputStream stream)
                throws ShortcutReadException
        {
            return urlString;
        }
    }
    
    @Test
    public void testGetShortcutName()
                  throws ShortcutReadException
    {
        String base_filename = "MyShortcut";
        String filename_without_ext = "C:" + File.separator + "myfolder" +
                                      File.separator + base_filename;
        String filename_with_ext = filename_without_ext + ".url";

        assertEquals( base_filename, reader.getShortcutName(new File(filename_without_ext)) );
        assertEquals( base_filename, reader.getShortcutName(new File(filename_with_ext)) );
    }
    
    @Test
    public void testRead()
                  throws FileNotFoundException,
                         ShortcutReadException,
                         IOException
    {
        String base_filename = "MyShortcut";
        String filename = base_filename + ".url";
        File file = folder.newFile(filename);
        InputStream instream = new FileInputStream(file);

        assertEquals( urlString, reader.readUrlString(file));
        assertEquals( urlString, reader.readUrlString(instream));

        
        ShortcutContents contents;

        contents = reader.read(file);
        assertEquals( urlString, contents.getUrlString());
        assertEquals( base_filename, contents.getName());
    }
}
