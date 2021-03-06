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
import java.util.List;
import java.util.Map.Entry;

import com.abcodeworks.webshortcutapp.LaunchDesktopShortcutActivity;
import com.abcodeworks.webshortcutapp.LaunchUnknownShortcutActivity;
import com.abcodeworks.webshortcutapp.LaunchUrlShortcutActivity;
import com.abcodeworks.webshortcutapp.LaunchWeblocShortcutActivity;
import com.abcodeworks.webshortcutapp.LaunchWebsiteShortcutActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

public class ShortcutTestHelper {
    static protected void setActivityEnabled(Context ctx, Class<? extends Activity> activityClass, boolean enabled) {
        int flag = (enabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                            : PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
        ComponentName component = new ComponentName(ctx, activityClass);


        ctx.getPackageManager().setComponentEnabledSetting(component, flag,
            PackageManager.DONT_KILL_APP);
    }
	
    /* Creates an intent using the given information and verifies that the specified activity matches against it */
	static public boolean testIntentMatches(PackageManager manager, String data, String type, Class<? extends Activity> activityClass) {
		Intent intent = new Intent();
    	intent.setAction(Intent.ACTION_VIEW);
    	intent.setDataAndType(Uri.parse(data), type);
    	
    	// Based on code in this thread:
    	// http://stackoverflow.com/questions/11752081/is-there-any-way-to-query-an-specific-type-of-intent-filter-capable-apps
    	boolean found = false;
    	List<ResolveInfo> infos = manager.queryIntentActivities (intent,
                PackageManager.GET_INTENT_FILTERS);
        for (ResolveInfo info : infos) {
            ActivityInfo activityInfo = info.activityInfo;
            String activityPackageName = activityInfo.packageName;
            String activityName = activityInfo.name;
            System.out.println("Activity "+activityPackageName + "/" + activityName);
            if(activityInfo.name.equals(activityClass.getName())) {
            	found = true;
            }
        }
        
        return found;
	}
	
	static public boolean isActivityEnabled(Context ctx, Class<? extends Activity> activityClass)
	{
		ComponentName component = new ComponentName(ctx, activityClass);
		int flag = ctx.getPackageManager().getComponentEnabledSetting(component);
    	boolean activityEnabled = flag == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
    	return activityEnabled;
	}
	
	/* Saves the current states of the launcher activities */
	static public HashMap<Class<? extends Activity>, Boolean> getActivitiesEnabledState(Context ctx) {
		HashMap<Class<? extends Activity>, Boolean> map = new HashMap<Class<? extends Activity>, Boolean>();
		map.put(LaunchUnknownShortcutActivity.class, isActivityEnabled(ctx, LaunchUnknownShortcutActivity.class));
		map.put(LaunchUrlShortcutActivity.class, isActivityEnabled(ctx, LaunchUrlShortcutActivity.class));
		map.put(LaunchWebsiteShortcutActivity.class, isActivityEnabled(ctx, LaunchWebsiteShortcutActivity.class));
		map.put(LaunchDesktopShortcutActivity.class, isActivityEnabled(ctx, LaunchDesktopShortcutActivity.class));
		map.put(LaunchWeblocShortcutActivity.class, isActivityEnabled(ctx, LaunchWeblocShortcutActivity.class));
		
		return map;
	}
	
	/* Enables the launcher activities */
	static public void enableAllActivities(Context ctx) {
		setActivityEnabled(ctx, LaunchUnknownShortcutActivity.class, true);
		setActivityEnabled(ctx, LaunchUrlShortcutActivity.class, true);
		setActivityEnabled(ctx, LaunchWebsiteShortcutActivity.class, true);
		setActivityEnabled(ctx, LaunchDesktopShortcutActivity.class, true);
		setActivityEnabled(ctx, LaunchWeblocShortcutActivity.class, true);
	}
	
	/* Restores the original states of the launcher activities */
	static public void restoreActivitiesEnabledState(Context ctx, HashMap<Class<? extends Activity>, Boolean> stateMap) {
		for(Entry<Class<? extends Activity>, Boolean> entry : stateMap.entrySet()) {
		    Class<? extends Activity> key = entry.getKey();
		    Boolean enabledObj = entry.getValue();
		    setActivityEnabled(ctx, key, enabledObj.booleanValue());
	    }
	}
}
