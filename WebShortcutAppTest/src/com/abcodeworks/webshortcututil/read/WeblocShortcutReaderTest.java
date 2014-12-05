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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.abcodeworks.webshortcututil.read.ShortcutReadException;
import com.abcodeworks.webshortcututil.read.ShortcutReader;
import com.abcodeworks.webshortcututil.read.WeblocShortcutReader;

import static com.abcodeworks.webshortcututil.ShortcutTestHelper.testReadShortcut;
import static com.abcodeworks.webshortcututil.ShortcutTestHelper.getTestStream;

public class WeblocShortcutReaderTest extends AndroidTestCase
{
    ShortcutReader reader = null;
    
    final String
        BIN_PATH = "samples" + File.separator + "real" + File.separator + "webloc" + File.separator + "binary",
        BIN_PERCENT_PATH = "samples" + File.separator + "real" + File.separator + "webloc" + File.separator + "binary" + File.separator + "percent_encoded",
        XML_PATH = "samples" + File.separator + "real" + File.separator + "webloc" + File.separator + "xml",
        XML_PERCENT_PATH = "samples" + File.separator + "real" + File.separator + "webloc" + File.separator + "xml" + File.separator + "percent_encoded",
        XML_FAKE_PATH = "samples" + File.separator + "fake" + File.separator + "webloc" + File.separator + "xml";

    public void setUp() throws Exception {
        reader = new WeblocShortcutReader();
    }
    
    @SmallTest
    public void testReadUrlString()
                  throws FileNotFoundException,
                         ShortcutReadException,
                         IOException
    {
        // Binary PLIST shortcuts generated on OSX
        testReadShortcut(reader, BIN_PATH, "Google.webloc", "Google", "https://www.google.com/");
        testReadShortcut(reader, BIN_PATH, "Yahoo!.webloc", "Yahoo!", "http://www.yahoo.com/");
        
        // XML PLIST shortcuts generated on OSX
        testReadShortcut(reader, XML_PATH, "Google.webloc", "Google", "https://www.google.com/");
        testReadShortcut(reader, XML_PATH, "Yahoo!.webloc", "Yahoo!", "http://www.yahoo.com/");
    }
}
