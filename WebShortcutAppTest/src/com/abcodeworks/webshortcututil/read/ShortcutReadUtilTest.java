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
import com.abcodeworks.webshortcututil.read.ShortcutReadUtil;

import static com.abcodeworks.webshortcututil.ShortcutTestHelper.getTestStream;

public class ShortcutReadUtilTest extends AndroidTestCase
{
    @SmallTest
    public void testHasValidExtension()
    {
        assertTrue("Valid extention .desktop", ShortcutReadUtil.hasValidExtension(new File("file.desktop")));
        assertTrue("Valid extention .DESKTOP", ShortcutReadUtil.hasValidExtension(new File("file.DESKTOP")));
        assertTrue("Valid extension .url", ShortcutReadUtil.hasValidExtension(new File("file.url")));
        assertTrue("Valid extension .URL", ShortcutReadUtil.hasValidExtension(new File("file.URL")));
        assertTrue("Valid extention .webloc", ShortcutReadUtil.hasValidExtension(new File("file.webloc")));
        assertTrue("Valid extention .WEBLOC", ShortcutReadUtil.hasValidExtension(new File("file.WEBLOC")));
        assertTrue("Valid extention .website", ShortcutReadUtil.hasValidExtension(new File("file.website")));
        assertTrue("Valid extention .WEBSITE", ShortcutReadUtil.hasValidExtension(new File("file.WEBSITE")));
        assertTrue("Valid extension multiple dots", ShortcutReadUtil.hasValidExtension(new File("file.misleading.url")));
        assertTrue("Valid extension with full path", ShortcutReadUtil.hasValidExtension(new File("file://testpath" + File.pathSeparator + "file.url")));
        assertFalse("Invalid extension", ShortcutReadUtil.hasValidExtension(new File("file.badextension")));
        assertFalse("Invalid no extension", ShortcutReadUtil.hasValidExtension(new File("file")));
        assertFalse("Invalid no extension (with dot)", ShortcutReadUtil.hasValidExtension(new File("file.")));
        assertFalse("Invalid extension multiple dots", ShortcutReadUtil.hasValidExtension(new File("file.misleading.badextension")));
    }
}
