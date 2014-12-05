package com.abcodeworks.webshortcutapp.test;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.preference.CheckBoxPreference;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.SmallTest;

import com.abcodeworks.webshortcutapp.LaunchDesktopShortcutActivity;
import com.abcodeworks.webshortcutapp.LaunchUrlShortcutActivity;
import com.abcodeworks.webshortcutapp.LaunchWeblocShortcutActivity;
import com.abcodeworks.webshortcutapp.LaunchWebsiteShortcutActivity;
import com.abcodeworks.webshortcutapp.R;

public class LaunchShortcutIntentTest extends ActivityInstrumentationTestCase2<LaunchUrlShortcutActivity> {
	public LaunchShortcutIntentTest() {
		super(LaunchUrlShortcutActivity.class);
	}
	
	HashMap<Class<? extends Activity>, Boolean> activityStateMap = null;
	
	protected void setUp() throws Exception {
        super.setUp();
        
        Context ctx = getInstrumentation().getTargetContext();
        activityStateMap = ShortcutTestHelper.getActivitiesEnabledState(ctx);
        ShortcutTestHelper.enableAllActivities(ctx);
    }
	
	protected void tearDown() throws Exception {
        Context ctx = getInstrumentation().getTargetContext();
        ShortcutTestHelper.restoreActivitiesEnabledState(ctx, activityStateMap);
		
		super.tearDown();
    }
	    
	@UiThreadTest
	public void testUrlIntentMatch() {
		PackageManager manager = getInstrumentation().getTargetContext().getPackageManager();
    	assertFalse(ShortcutTestHelper.testIntentMatches(manager, "file://dummypath/dummyfile.txt", "text/plain", LaunchUrlShortcutActivity.class));
    	assertTrue(ShortcutTestHelper.testIntentMatches(manager, "file://dummypath/dummyfile.url", "text/plain", LaunchUrlShortcutActivity.class));
    	assertTrue(ShortcutTestHelper.testIntentMatches(manager, "content://dummypath/dummyfile.txt", "text/x-url", LaunchUrlShortcutActivity.class));
	}
	
	@UiThreadTest
	public void testWebsiteIntentMatch() {
		PackageManager manager = getInstrumentation().getTargetContext().getPackageManager();
    	assertFalse(ShortcutTestHelper.testIntentMatches(manager, "file://dummypath/dummyfile.txt", "text/plain", LaunchWebsiteShortcutActivity.class));
    	assertTrue(ShortcutTestHelper.testIntentMatches(manager, "file://dummypath/dummyfile.website", "text/plain", LaunchWebsiteShortcutActivity.class));
    	assertTrue(ShortcutTestHelper.testIntentMatches(manager, "content://dummypath/dummyfile.txt", "application/x-mswebsite", LaunchWebsiteShortcutActivity.class));
	}
	
	@UiThreadTest
	public void testDesktopIntentMatch() {
		PackageManager manager = getInstrumentation().getTargetContext().getPackageManager();
    	assertFalse(ShortcutTestHelper.testIntentMatches(manager, "file://dummypath/dummyfile.txt", "text/plain", LaunchDesktopShortcutActivity.class));
    	assertTrue(ShortcutTestHelper.testIntentMatches(manager, "file://dummypath/dummyfile.desktop", "text/plain", LaunchDesktopShortcutActivity.class));
	}
	
	@UiThreadTest
	public void testWebocIntentMatch() {
		PackageManager manager = getInstrumentation().getTargetContext().getPackageManager();
    	assertFalse(ShortcutTestHelper.testIntentMatches(manager, "file://dummypath/dummyfile.txt", "text/plain", LaunchWeblocShortcutActivity.class));
    	assertTrue(ShortcutTestHelper.testIntentMatches(manager, "file://dummypath/dummyfile.webloc", "text/plain", LaunchWeblocShortcutActivity.class));
	}
}
