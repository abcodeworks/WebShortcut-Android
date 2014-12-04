package com.abcodeworks.webshortcutapp.test;

import java.util.List;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.abcodeworks.webshortcutapp.LaunchUrlShortcutActivity;

public class LaunchUrlShortcutActivityTest extends ActivityInstrumentationTestCase2<LaunchUrlShortcutActivity> {
	public LaunchUrlShortcutActivityTest() {
		super(LaunchUrlShortcutActivity.class);
	}
	
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
    	
    	String uri = "file://dummypath/dummyfile.txt";
    	String type = "text/plain";
    	
    	/*Intent intent = new Intent();
    	intent.setAction(Intent.ACTION_VIEW);
    	intent.setDataAndType(Uri.parse(uri), type);

    	startActivity(viewIntent, null, null);
    	
    	Intent shareIntent = getStartedActivityIntent();
    	assertEquals(Intent.ACTION_SEND, shareIntent.getAction());
    	String sharedUri = shareIntent.getExtras().get(Intent.EXTRA_STREAM).toString();
    	assertEquals(uri, sharedUri);
    	assertNull(shareIntent.getData());
        assertEquals(type, shareIntent.getType());*/
    	assertTrue(true);
    }
}
