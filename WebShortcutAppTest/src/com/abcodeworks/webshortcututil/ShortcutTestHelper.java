package com.abcodeworks.webshortcututil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import junit.framework.TestCase;

import com.abcodeworks.webshortcututil.read.ShortcutContents;
import com.abcodeworks.webshortcututil.read.ShortcutReadException;
import com.abcodeworks.webshortcututil.read.ShortcutReadUtil;
import com.abcodeworks.webshortcututil.read.ShortcutReader;
import com.abcodeworks.webshortcututil.write.FileAlreadyExistsException;
import com.abcodeworks.webshortcututil.write.ShortcutWriteException;
import com.abcodeworks.webshortcututil.write.ShortcutWriter;

public class ShortcutTestHelper extends TestCase {
    static public InputStream getTestStream(String path, String filename) throws IOException
    {
        String resourcePath = File.separator + path + File.separator + filename;
        
        URL resourceUrl = ShortcutTestHelper.class.getResource(resourcePath);
        if(resourceUrl == null) {
            fail("Resource not found: " + resourcePath);
        }
        return resourceUrl.openStream();
    }
    
    static public void testReadShortcut(ShortcutReader reader, String path, String filename, String expectedName, String expectedUrl)
            throws IOException, ShortcutReadException
    {
        String urlString;

        InputStream instream = getTestStream(path, filename);
        urlString = reader.readUrlString(instream);
        assertEquals(expectedUrl, urlString);
        instream.close();
        
        instream = getTestStream(path, filename);
        urlString = ShortcutReadUtil.readUrlStringTrialAndError(instream);
        assertEquals(expectedUrl, urlString);
        instream.close();
    }
}
