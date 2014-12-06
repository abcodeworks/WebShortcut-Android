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

package com.abcodeworks.webshortcutapp;


import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;


// Used the following file as a reference:
// https://github.com/intrications/intent-intercept/blob/master/IntentIntercept/src/uk/co/ashtonbrsc/intentexplode/Settings.java

public class SettingsActivity extends PreferenceActivity
                implements Preference.OnPreferenceChangeListener
{
        protected Preference enableLaunchUnknownShortcutPref = null,
        		             defaultCreateShortcutTypePref = null;
        
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
               
            // Is this really necessary?  I don't see this being done in examples.
            PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        }
        
        // Activates or deactivates the specified activity
        protected void setActivityEnabled(Class<? extends Activity> activityClass, boolean enabled) {
            int flag = (enabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                                : PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
            ComponentName component = new ComponentName(this, activityClass);

            getPackageManager().setComponentEnabledSetting(component, flag,
                PackageManager.DONT_KILL_APP);
        }
        
        @SuppressWarnings("deprecation")
        @Override
        protected void onPostCreate(Bundle savedInstanceState) {
                super.onPostCreate(savedInstanceState);


                addPreferencesFromResource(R.xml.pref_general);
                
        		// Set the version on the settings page
        		// Some of this code was copied from:
        		// http://stackoverflow.com/questions/3637665/user-versionname-value-of-androidmanifest-xml-in-code/3637686#3637686
        		try {
        			Preference appVersionPref = findPreference(getString(R.string.pref_app_version));
        		    String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        		    appVersionPref.setSummary(version);
        		} catch (NameNotFoundException e) {
        		    Log.e("WebShortcutApp", e.getMessage());
        		}
                
                enableLaunchUnknownShortcutPref = findPreference(getString(R.string.pref_enable_launch_unknown_shortcut));
                enableLaunchUnknownShortcutPref.setOnPreferenceChangeListener(this);
                
                defaultCreateShortcutTypePref = findPreference(getString(R.string.pref_default_create_shortcut_type));
                defaultCreateShortcutTypePref.setOnPreferenceChangeListener(this);
                
                // Set the summary to indicate the current value
                ListPreference listPref = (ListPreference)defaultCreateShortcutTypePref;
                listPref.setSummary(listPref.getEntry());
        }


        /**
         * A preference value change listener that updates the preference's summary
         * to reflect its new value.
         */
        public boolean onPreferenceChange(Preference preference, Object newValue)
        {
            if(preference == enableLaunchUnknownShortcutPref) {
            	/* When this checkbox is checked, the "unknown" shortcut
            	 * launcher is activated and the others are deactivated
            	 * (and vice-versa if the checkbox is unchecked).
            	 * Ideally we would still want to use the specific launchers
            	 * if the file matches the pattern, but if we leave those
            	 * active, we will get two icons in the chooser.
            	 */
                Boolean enable = (Boolean) newValue;
                Boolean invEnable = Boolean.valueOf(!enable.booleanValue());
                setActivityEnabled(LaunchUnknownShortcutActivity.class, enable);
                setActivityEnabled(LaunchUrlShortcutActivity.class, invEnable);
                setActivityEnabled(LaunchWebsiteShortcutActivity.class, invEnable);
                setActivityEnabled(LaunchDesktopShortcutActivity.class, invEnable);
                setActivityEnabled(LaunchWeblocShortcutActivity.class, invEnable);
            } else if(preference == defaultCreateShortcutTypePref) {
            	/* When the shortcut type is changed, update the
            	 * summary to reflect the new value.
            	 * This could probably be cleaner.  Should we be using
            	 * onSharedPreferenceChanged instead of onPreferenceChange?
            	 */
            	CharSequence newKey = (CharSequence)newValue;
            	ListPreference listPref = (ListPreference)defaultCreateShortcutTypePref;
            	int index = listPref.findIndexOfValue(newKey.toString());
            	CharSequence[] titles = listPref.getEntries();
            	listPref.setSummary(titles[index].toString());
            }

            return true;
        }
}

