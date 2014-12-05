package com.abcodeworks.webshortcutapp.test;

import android.content.pm.PackageManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.abcodeworks.webshortcutapp.LaunchDesktopShortcutActivity;
import com.abcodeworks.webshortcutapp.LaunchUrlShortcutActivity;
import com.abcodeworks.webshortcutapp.LaunchWeblocShortcutActivity;
import com.abcodeworks.webshortcutapp.LaunchWebsiteShortcutActivity;

public class LaunchShortcutIntentTest extends ActivityInstrumentationTestCase2<LaunchUrlShortcutActivity> {
	public LaunchShortcutIntentTest() {
		super(LaunchUrlShortcutActivity.class);
	}
    
	@SmallTest
	public void testUrlIntentMatch() {
		PackageManager manager = getInstrumentation().getTargetContext().getPackageManager();
    	assertFalse(ShortcutTestHelper.testIntentMatches(manager, "file://dummypath/dummyfile.txt", "text/plain", LaunchUrlShortcutActivity.class));
    	assertTrue(ShortcutTestHelper.testIntentMatches(manager, "file://dummypath/dummyfile.url", "text/plain", LaunchUrlShortcutActivity.class));
    	assertTrue(ShortcutTestHelper.testIntentMatches(manager, "content://dummypath/dummyfile.txt", "text/x-url", LaunchUrlShortcutActivity.class));
	}
	
	@SmallTest
	public void testWebsiteIntentMatch() {
		PackageManager manager = getInstrumentation().getTargetContext().getPackageManager();
    	assertFalse(ShortcutTestHelper.testIntentMatches(manager, "file://dummypath/dummyfile.txt", "text/plain", LaunchWebsiteShortcutActivity.class));
    	assertTrue(ShortcutTestHelper.testIntentMatches(manager, "file://dummypath/dummyfile.website", "text/plain", LaunchWebsiteShortcutActivity.class));
    	assertTrue(ShortcutTestHelper.testIntentMatches(manager, "content://dummypath/dummyfile.txt", "application/x-mswebsite", LaunchWebsiteShortcutActivity.class));
	}
	
	@SmallTest
	public void testDesktopIntentMatch() {
		PackageManager manager = getInstrumentation().getTargetContext().getPackageManager();
    	assertFalse(ShortcutTestHelper.testIntentMatches(manager, "file://dummypath/dummyfile.txt", "text/plain", LaunchDesktopShortcutActivity.class));
    	assertTrue(ShortcutTestHelper.testIntentMatches(manager, "file://dummypath/dummyfile.desktop", "text/plain", LaunchDesktopShortcutActivity.class));
	}
	
	@SmallTest
	public void testWebocIntentMatch() {
		PackageManager manager = getInstrumentation().getTargetContext().getPackageManager();
    	assertFalse(ShortcutTestHelper.testIntentMatches(manager, "file://dummypath/dummyfile.txt", "text/plain", LaunchWeblocShortcutActivity.class));
    	assertTrue(ShortcutTestHelper.testIntentMatches(manager, "file://dummypath/dummyfile.webloc", "text/plain", LaunchWeblocShortcutActivity.class));
	}
}
