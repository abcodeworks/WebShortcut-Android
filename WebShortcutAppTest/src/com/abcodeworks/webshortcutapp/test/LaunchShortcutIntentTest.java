/**
 * Copyright 2014 by Andre Beckus
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.


 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.


 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.abcodeworks.webshortcutapp.test;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

import com.abcodeworks.webshortcutapp.LaunchDesktopShortcutActivity;
import com.abcodeworks.webshortcutapp.LaunchUrlShortcutActivity;
import com.abcodeworks.webshortcutapp.LaunchWeblocShortcutActivity;
import com.abcodeworks.webshortcutapp.LaunchWebsiteShortcutActivity;

/*
 * Tests that the intent filters work on the shortcut launchers
 */
public class LaunchShortcutIntentTest extends ActivityInstrumentationTestCase2<LaunchUrlShortcutActivity> {
	public LaunchShortcutIntentTest() {
		super(LaunchUrlShortcutActivity.class);
	}
	
	HashMap<Class<? extends Activity>, Boolean> activityStateMap = null;
	
	protected void setUp() throws Exception {
        super.setUp();
        
        // Make sure all activities are enabled, or this test will fail
        Context ctx = getInstrumentation().getTargetContext();
        activityStateMap = ShortcutTestHelper.getActivitiesEnabledState(ctx);
        ShortcutTestHelper.enableAllActivities(ctx);
    }

	protected void tearDown() throws Exception {
        // Restore the original activity states
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
