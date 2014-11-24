package com.abcodeworks.webshortcutapp;

import java.io.InputStream;

import com.abcodeworks.webshortcututil.read.ShortcutReader;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

abstract public class LaunchShortcutActivity extends Activity {
	abstract protected ShortcutReader getShortcutReader();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		Log.i("WebShortcut", "In Activity");
	    if (Intent.ACTION_VIEW.equals(intent.getAction())) {
	    	try {
		    	Log.i("WebShortcut", "Got view intent...");
		    	ContentResolver resolver = getContentResolver();
		    	Uri uri = intent.getData();
		    	InputStream instream = resolver.openInputStream(uri);
		    	ShortcutReader reader = getShortcutReader();
		    	String url = reader.readUrlString(instream);
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
