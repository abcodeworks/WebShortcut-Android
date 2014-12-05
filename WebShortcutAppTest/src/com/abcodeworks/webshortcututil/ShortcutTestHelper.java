package com.abcodeworks.webshortcututil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.abcodeworks.webshortcututil.read.ShortcutContents;
import com.abcodeworks.webshortcututil.read.ShortcutReadException;
import com.abcodeworks.webshortcututil.read.ShortcutReadUtil;
import com.abcodeworks.webshortcututil.read.ShortcutReader;
import com.abcodeworks.webshortcututil.read.ShortcutReaderTest;
import com.abcodeworks.webshortcututil.write.FileAlreadyExistsException;
import com.abcodeworks.webshortcututil.write.ShortcutWriteException;
import com.abcodeworks.webshortcututil.write.ShortcutWriter;

import static org.junit.Assert.*;

public class ShortcutTestHelper {
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
    
    static public void testWriteShortcut(ShortcutWriter writer, File path, String name, String url)
            throws IOException, ShortcutReadException, FileAlreadyExistsException, ShortcutWriteException
    {
        String filename = writer.createFilename(name);
        File fullFilename = new File(path, filename);
        writer.write(fullFilename, name, url); 
        ShortcutContents contents = ShortcutReadUtil.read(fullFilename);
        
        assertEquals(name, contents.getName());
        assertEquals(url, contents.getUrlString());
        
        assertEquals(url, ShortcutReadUtil.readUrlString(fullFilename));
 
        FileInputStream instream = new FileInputStream(fullFilename);
        assertEquals(url, ShortcutReadUtil.readUrlStringTrialAndError(instream));
        instream.close();
        
        try {
            writer.write(fullFilename, name, url);
            fail("Did not get FileAlreadyExistsException");
        } catch(FileAlreadyExistsException e) {
        }
    }
}
