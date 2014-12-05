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
import com.abcodeworks.webshortcututil.read.WebsiteShortcutReader;

import static com.abcodeworks.webshortcututil.ShortcutTestHelper.testReadShortcut;

public class WebsiteShortcutReaderTest extends AndroidTestCase {
    ShortcutReader reader = null;

    final String
        WEBSITE_IE9_PATH =  "samples" + File.separator + "real" + File.separator + "website" + File.separator + "IE9",
        WEBSITE_IE10_PATH = "samples" + File.separator + "real" + File.separator + "website" + File.separator + "IE10";
    
    public void setUp() throws Exception {
        reader = new WebsiteShortcutReader();
    }
    
    protected void testHelper(String path) throws ShortcutReadException, IOException {
        testReadShortcut(reader, path, "Google.website", "Google", "https://www.google.com/");
        testReadShortcut(reader, path, "Microsoft Corporation.website", "Microsoft Corporation", "http://www.microsoft.com/sv-se/default.aspx");
        testReadShortcut(reader, path, "Myspace  Social Entertainment.website", "Myspace  Social Entertainment", "http://www.myspace.com/");
        testReadShortcut(reader, path, "Yahoo!.website", "Yahoo!", "http://www.yahoo.com/");
    }
    
    @SmallTest
    public void testReadUrlString()
                  throws FileNotFoundException,
                         ShortcutReadException,
                         IOException
    {
        // Website tests: IE9
        testHelper(WEBSITE_IE9_PATH);
        
        // Website tests: IE10
        testHelper(WEBSITE_IE10_PATH);
    }
}
