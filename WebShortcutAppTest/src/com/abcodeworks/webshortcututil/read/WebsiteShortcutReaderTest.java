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
import org.junit.*;

import com.abcodeworks.webshortcututil.read.ShortcutReadException;
import com.abcodeworks.webshortcututil.read.ShortcutReader;
import com.abcodeworks.webshortcututil.read.WebsiteShortcutReader;

import static com.abcodeworks.webshortcututil.ShortcutTestHelper.testReadShortcut;

public class WebsiteShortcutReaderTest {
    ShortcutReader reader = null;

    final String
        WEBSITE_IE9_PATH =  "samples" + File.separator + "real" + File.separator + "website" + File.separator + "IE9",
        WEBSITE_IE10_PATH = "samples" + File.separator + "real" + File.separator + "website" + File.separator + "IE10";
    
    @Before
    public void setUp() throws Exception {
        reader = new WebsiteShortcutReader();
    }
    
    protected void testHelper(String path) throws ShortcutReadException, IOException {
        testReadShortcut(reader, path, "Google.website", "Google", "https://www.google.com/");
        testReadShortcut(reader, path, "Microsoft Corporation.website", "Microsoft Corporation", "http://www.microsoft.com/sv-se/default.aspx");
        testReadShortcut(reader, path, "Myspace  Social Entertainment.website", "Myspace  Social Entertainment", "http://www.myspace.com/");
        testReadShortcut(reader, path, "Yahoo!.website", "Yahoo!", "http://www.yahoo.com/");
    }
    
    @Test
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
    
    
    
    protected void testHelperNonAscii(String path) throws ShortcutReadException, IOException {
        testReadShortcut(reader, path, "CIOとITマネージャーの課題を解決するオンラインメディア - ZDNet Japan.website", "CIOとITマネージャーの課題を解決するオンラインメディア - ZDNet Japan", "http://japan.zdnet.com/");
        testReadShortcut(reader, path, "sverige - Sök på Google.website", "sverige - Sök på Google", "http://www.google.se/");
        testReadShortcut(reader, path, "www.ÖÐ¹úÕþ¸®.ÕþÎñ.cn.website", "www.ÖÐ¹úÕþ¸®.ÕþÎñ.cn", "http://www.中国政府.政务.cn/");
        testReadShortcut(reader, path, "中国雅虎首页.website", "中国雅虎首页", "http://cn.yahoo.com/");
        testReadShortcut(reader, path, "导航.中国.website", "导航.中国", "http://导航.中国/");
        testReadShortcut(reader, path, "百度一下，你就知道.website", "百度一下，你就知道", "http://www.baidu.com/");
    }
    
    @Test
    public void testReadUrlStringNonAscii()
                  throws FileNotFoundException,
                         ShortcutReadException,
                         IOException
    {
        // Website tests: IE9
        testHelperNonAscii(WEBSITE_IE9_PATH);
        
        // Website tests: IE10
        testHelperNonAscii(WEBSITE_IE10_PATH);
    }
}
