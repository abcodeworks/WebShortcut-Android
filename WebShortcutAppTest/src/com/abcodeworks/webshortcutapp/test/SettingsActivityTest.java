package com.abcodeworks.webshortcutapp.test;

import com.abcodeworks.webshortcutapp.LaunchDesktopShortcutActivity;
import com.abcodeworks.webshortcutapp.LaunchUnknownShortcutActivity;
import com.abcodeworks.webshortcutapp.LaunchUrlShortcutActivity;
import com.abcodeworks.webshortcutapp.LaunchWeblocShortcutActivity;
import com.abcodeworks.webshortcutapp.LaunchWebsiteShortcutActivity;
import com.abcodeworks.webshortcutapp.SettingsActivity;

import static com.abcodeworks.webshortcutapp.test.ShortcutTestHelper.isActivityEnabled;

import com.abcodeworks.webshortcutapp.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

public class SettingsActivityTest extends ActivityInstrumentationTestCase2<SettingsActivity> {

	public SettingsActivityTest() {
		super(SettingsActivity.class);
	}

	SettingsActivity settingsActivity = null;
	CheckBoxPreference enableLaunchUnknownShortcutPref = null;
	
	@SuppressWarnings("deprecation")
	Preference getPref(int key) {
		String prefkey = settingsActivity.getResources().getString(key);
    	return settingsActivity.findPreference(prefkey);
	}
	
	protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(true);
        settingsActivity = getActivity();
        
        enableLaunchUnknownShortcutPref = (CheckBoxPreference)getPref(R.string.pref_enable_launch_unknown_shortcut);
    }
	
    void changeUnknownCheckboxAndVerify(CheckBoxPreference pref, boolean checked) {
		pref.setChecked(checked);
		
		// Setting the checkbox does not call the change listener.
		// This is ugly but works.  The better alternative would be to simulate
		// an actual click, but I do not know how to get the widget for the preference...
		settingsActivity.onPreferenceChange(pref, Boolean.valueOf(checked));
    	
		assertEquals(checked, pref.isChecked());
    	assertEquals(checked, isActivityEnabled(settingsActivity, LaunchUnknownShortcutActivity.class));
    	assertEquals(!checked, isActivityEnabled(settingsActivity, LaunchUrlShortcutActivity.class));
    	assertEquals(!checked, isActivityEnabled(settingsActivity, LaunchWebsiteShortcutActivity.class));
    	assertEquals(!checked, isActivityEnabled(settingsActivity, LaunchDesktopShortcutActivity.class));
    	assertEquals(!checked, isActivityEnabled(settingsActivity, LaunchWeblocShortcutActivity.class));
	}
    
    
    
    @UiThreadTest
    public void testEnableUnknownShortcutPref() {
    	CheckBoxPreference pref = enableLaunchUnknownShortcutPref;
    	changeUnknownCheckboxAndVerify(pref, true);
    	changeUnknownCheckboxAndVerify(pref, false);
    	changeUnknownCheckboxAndVerify(pref, true);
    }
}
