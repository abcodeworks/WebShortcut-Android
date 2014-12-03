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
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;


// Used https://github.com/intrications/intent-intercept/blob/master/IntentIntercept/src/uk/co/ashtonbrsc/intentexplode/Settings.java


/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity
                implements Preference.OnPreferenceChangeListener
{
        protected Preference enableCreateShortcutPref = null;
        
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
               
            // Is this really necessary?  I don't see this being done in examples.
            PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        }
        
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
                
                enableCreateShortcutPref = findPreference(getString(R.string.pref_enable_create_shortcut));
                enableCreateShortcutPref.setOnPreferenceChangeListener(this);
        }


        /**
         * A preference value change listener that updates the preference's summary
         * to reflect its new value.
         */
        public boolean onPreferenceChange(Preference preference, Object newValue)
        {
                if(preference == enableCreateShortcutPref){
                        Boolean enable = (Boolean) newValue;
                        setActivityEnabled(CreateShortcutActivity.class, enable);
                }


            return true;
        }
}
