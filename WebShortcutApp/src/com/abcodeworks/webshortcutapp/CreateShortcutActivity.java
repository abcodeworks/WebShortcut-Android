package com.abcodeworks.webshortcutapp;

import java.io.File;

import com.abcodeworks.webshortcututil.write.DesktopShortcutWriter;
import com.abcodeworks.webshortcututil.write.ShortcutWriter;
import com.abcodeworks.webshortcututil.write.UrlShortcutWriter;
import com.abcodeworks.webshortcututil.write.WeblocBinaryShortcutWriter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

// See:
//http://stackoverflow.com/questions/14099252/delete-file-after-sent-to-mail-as-intent-action-send

public class CreateShortcutActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);

		Intent intent = getIntent();
		Log.i("WebShortcut", "In Activity");
	    if (Intent.ACTION_SEND.equals(intent.getAction())) {
	    	try {
		    	Log.i("WebShortcut", "Got send intent...");
		    	Bundle extras = intent.getExtras();
		    	Log.i("WebShortcut", "received TEXT: " + extras.getString(Intent.EXTRA_TEXT));
		    	Log.i("WebShortcut", "received SUBJECT: " + extras.getString(Intent.EXTRA_SUBJECT));
		    	
		    	
		    	File cacheDir = getCacheDir();
		    	File shortcutDir = new File(cacheDir, "shareshortcuts");
		    	shortcutDir.mkdir();
		    	
		    	// Clean cache
		    	
		    	
		    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		    	String shortcutType = prefs.getString(getString(R.string.pref_default_create_shortcut_type), null);
		    	//System.out.println(shortcutType);
		    	
		    	String mimeType = null;
		    	ShortcutWriter writer = null;
		    	if(shortcutType.equals("url")) {
		    		mimeType = "text/x-url";
		    		writer = new UrlShortcutWriter();
		    	} else if(shortcutType.equals("desktop")) {
		    		mimeType = "application/octet-stream";
		    		writer = new DesktopShortcutWriter();
		    	} else if(shortcutType.equals("webloc")) {
		    		mimeType = "application/octet-stream";
		    		writer = new WeblocBinaryShortcutWriter();
		    	} else {
		    		throw new Exception("Invalid preference value");
		    	}
		    	
		    	String name = extras.getString(Intent.EXTRA_SUBJECT);
		    	String filename = writer.createFullFilename(name);
		    	String url = extras.getString(Intent.EXTRA_TEXT);
		    	File shortcutFile = new File(shortcutDir, filename);
		    	shortcutFile.delete();
		    	writer.write(shortcutFile, name, url);
		    	
		    	Intent sendIntent = new Intent();
		    	sendIntent.setAction(Intent.ACTION_SEND);
		    	
		    	// From http://stephendnicholas.com/archives/974
		    	sendIntent.putExtra(
		                Intent.EXTRA_STREAM,
		                    Uri.parse("content://" + CachedFileProvider.AUTHORITY + "/shareshortcuts/" + filename));

		    	//sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(shortcutFile));
		    	sendIntent.setType(mimeType);
		    	Toast.makeText(this, "content://" + CachedFileProvider.AUTHORITY + "/"
                        + filename, Toast.LENGTH_LONG).show();
		    	Log.i("webshortcut", "content://" + CachedFileProvider.AUTHORITY + "/"
                        + filename);
		    	sendIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

		    	try {
		    		startActivity(sendIntent);
		    	} catch(ActivityNotFoundException e) {
		    		Toast.makeText(this, getString(R.string.msg_no_app_for_share), Toast.LENGTH_LONG).show();
		    	}
	    	} catch(Exception e) {
	    		Log.e("WebShortcut", e.toString());
	    	}
	    }

		finish();
	}
}
