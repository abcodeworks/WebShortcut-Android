package com.abcodeworks.webshortcutapp.test;

import java.io.File;
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

public class LaunchShortcutIntentTest extends ActivityInstrumentationTestCase2<LaunchUrlShortcutActivity> {
	public LaunchShortcutIntentTest() {
		super(LaunchUrlShortcutActivity.class);
	}
    
	@SmallTest
	public void testIntentMatch() {
		PackageManager manager = getInstrumentation().getTargetContext().getPackageManager();
    	assertFalse(ShortcutTestHelper.testIntentMatches(manager, "file://dummypath/dummyfile.txt", "text/plain", LaunchUrlShortcutActivity.class));
    	assertTrue(ShortcutTestHelper.testIntentMatches(manager, "file://dummypath/dummyfile.url", "text/plain", LaunchUrlShortcutActivity.class));
    	assertTrue(ShortcutTestHelper.testIntentMatches(manager, "file://dummypath/dummyfile.txt", "text/x-url", LaunchUrlShortcutActivity.class));
	}
}
