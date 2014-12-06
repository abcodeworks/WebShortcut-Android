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

import java.io.InputStream;

import com.abcodeworks.webshortcututil.read.ShortcutReadException;
import com.abcodeworks.webshortcututil.read.ShortcutReadUtil;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class LaunchUnknownShortcutActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
		
		Intent intent = getIntent();
		Log.i("WebShortcut", "In Unknown Shortcut Activity");
	    if (Intent.ACTION_VIEW.equals(intent.getAction())) {
	    	try {
		    	Log.i("WebShortcut", "Got view intent...");
		    	ContentResolver resolver = getContentResolver();
		    	Uri uri = intent.getData();
		    	InputStream instream = resolver.openInputStream(uri);
		    	String url = null;
		    	try {
		    		url = ShortcutReadUtil.readUrlStringTrialAndError(instream);
		    	} catch(ShortcutReadException sre) {
		    		Log.e("WebShortcut", sre.toString());
		    		Toast.makeText(this, getString(R.string.msg_error_opening_unknown_shortcut), Toast.LENGTH_LONG).show();
		    	}
		    	Log.i("WebShortcut", "URL: " + url);
		    	Uri uriUrl = Uri.parse(url);
		        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);  
		        startActivity(launchBrowser);
	    	} catch(Exception e) {
	    		Log.e("WebShortcut", e.toString());
	    	}
	    }
		finish();
	}
}
