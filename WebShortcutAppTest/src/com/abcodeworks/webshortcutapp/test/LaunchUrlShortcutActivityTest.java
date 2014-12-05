package com.abcodeworks.webshortcutapp.test;

import java.io.File;
import java.util.List;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.abcodeworks.webshortcutapp.LaunchUrlShortcutActivity;

public class LaunchUrlShortcutActivityTest extends ActivityUnitTestCase<LaunchUrlShortcutActivity> {
	public LaunchUrlShortcutActivityTest() {
		super(LaunchUrlShortcutActivity.class);
		
	}
	
	final String
    	CHROME_PATH =   "samples" + File.separator + "real" + File.separator + "url" + File.separator + "Chrome",
    	FIREFOX_PATH =  "samples" + File.separator + "real" + File.separator + "url" + File.separator + "Firefox",
    	IE_PATH =       "samples" + File.separator + "real" + File.separator + "url" + File.separator + "IE",
    	FAKE_PATH =     "samples" + File.separator + "fake" + File.separator + "url";
	
	LaunchUrlShortcutActivity activity;
    
	@SmallTest
	public void testIntentMatch() {
		PackageManager manager = getInstrumentation().getTargetContext().getPackageManager();
    	assertFalse(ShortcutTestHelper.testIntentMatches(manager, "file://dummypath/dummyfile.txt", "text/plain", LaunchUrlShortcutActivity.class));
    	assertTrue(ShortcutTestHelper.testIntentMatches(manager, "file://dummypath/dummyfile.url", "text/plain", LaunchUrlShortcutActivity.class));
    	assertTrue(ShortcutTestHelper.testIntentMatches(manager, "file://dummypath/dummyfile.txt", "text/x-url", LaunchUrlShortcutActivity.class));
	}
	
    @SmallTest
    public void testSampleUrls() {
    	// Borrowed code from http://stackoverflow.com/questions/5708630/how-can-i-automate-a-test-that-sends-multiple-mock-intents-to-an-android-activit
    	
    	String uri = "file://" + CHROME_PATH + "/Google.url";
    	String type = "text/plain";
    	
    	Intent intent = new Intent();
    	intent.setAction(Intent.ACTION_VIEW);
    	intent.setDataAndType(Uri.parse(uri), type);
    	
    	startActivity(intent, null, null);
    	
    	AssetManager assetManager = getInstrumentation().getTargetContext().getResources().getAssets();
    	ParcelFileDescriptor pfd = ParcelFileDescriptor.open(new File(
                fileLocation), ParcelFileDescriptor.MODE_READ_ONLY);
    	
    	Intent shareIntent = getStartedActivityIntent();
    	assertEquals(Intent.ACTION_SEND, shareIntent.getAction());
    	String sharedUri = shareIntent.getExtras().get(Intent.EXTRA_STREAM).toString();
    	assertEquals(uri, sharedUri);
    	assertNull(shareIntent.getData());
        assertEquals(type, shareIntent.getType());
    }
}
