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

import com.abcodeworks.webshortcutapp.LaunchDesktopShortcutActivity;
import com.abcodeworks.webshortcutapp.LaunchUnknownShortcutActivity;
import com.abcodeworks.webshortcutapp.LaunchUrlShortcutActivity;
import com.abcodeworks.webshortcutapp.LaunchWeblocShortcutActivity;
import com.abcodeworks.webshortcutapp.LaunchWebsiteShortcutActivity;
import com.abcodeworks.webshortcutapp.SettingsActivity;

import static com.abcodeworks.webshortcutapp.test.ShortcutTestHelper.isActivityEnabled;

import com.abcodeworks.webshortcutapp.R;

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
