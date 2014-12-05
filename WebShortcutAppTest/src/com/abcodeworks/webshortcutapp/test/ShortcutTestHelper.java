package com.abcodeworks.webshortcutapp.test;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.abcodeworks.webshortcutapp.LaunchUrlShortcutActivity;

public class ShortcutTestHelper {
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
}
