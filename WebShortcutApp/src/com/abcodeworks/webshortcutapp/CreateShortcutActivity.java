package com.abcodeworks.webshortcutapp;

import java.io.File;
import java.util.Arrays;

import com.abcodeworks.webshortcututil.write.DesktopShortcutWriter;
import com.abcodeworks.webshortcututil.write.FileAlreadyExistsException;
import com.abcodeworks.webshortcututil.write.ShortcutWriteException;
import com.abcodeworks.webshortcututil.write.ShortcutWriter;
import com.abcodeworks.webshortcututil.write.UrlShortcutWriter;
import com.abcodeworks.webshortcututil.write.WeblocBinaryShortcutWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

// See:
//http://stackoverflow.com/questions/14099252/delete-file-after-sent-to-mail-as-intent-action-send

public class CreateShortcutActivity extends Activity {
	Dialog dialog = null;
	
	@Override
	protected void onStart() {
		super.onStart();
		if(dialog != null) {
			dialog.show();
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if(dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
		finish();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final Activity thisActivity = this;
		
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
		    	final File shortcutDir = new File(cacheDir, "shareshortcuts");
		    	shortcutDir.mkdir();
		    	
		    	// Clean cache
		    	
		    	
		    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		    	String defaultShortcutType = prefs.getString(getString(R.string.pref_default_create_shortcut_type), null);
		    	//System.out.println(shortcutType);
		    	
		    	// See: http://stackoverflow.com/questions/2795300/how-to-implement-a-custom-alertdialog-view
		    	LayoutInflater inflater = getLayoutInflater();
		    	//FrameLayout frame = (FrameLayout) findViewById(android.R.id.body);
		    	View dialogView = inflater.inflate(R.layout.dialog_create_shortcut, null);
		    	//frame.addView(dialogView);
		    	
		    	ShortcutWriter dummyWriter = new UrlShortcutWriter();
		    	String rawFilename = extras.getString(Intent.EXTRA_SUBJECT);
		    	final String defaultFilename = dummyWriter.createBaseFilename(rawFilename);
		    	
		    	final EditText editFilename = (EditText)dialogView.findViewById(R.id.edit_filename);
		    	editFilename.setText(defaultFilename);
		    	
		    	final Spinner shortcutTypeSpinner = (Spinner)dialogView.findViewById(R.id.shortcut_type);
		    	String[] androidStrings = getResources().getStringArray(R.array.shortcut_type_keys);   
		    	int index = Arrays.asList(androidStrings).indexOf(defaultShortcutType);
		    	shortcutTypeSpinner.setSelection(index);
		    	
		    	final String url = extras.getString(Intent.EXTRA_TEXT);
		    	
		    	dialog = new AlertDialog.Builder(this)
			        .setTitle("Create Shortcut")
			        .setIcon(R.drawable.ic_launcher)
			        .setView(dialogView)
			        .setCancelable(true)
			        .setPositiveButton(R.string.dialog_create_shortcut_positive_button, new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) { 
			            	String name = editFilename.getText().toString();
			            	if(name.equals("")) {
			            		name = defaultFilename;
			            	}
			            	
			            	int shortcutTypePos = shortcutTypeSpinner.getSelectedItemPosition();
			            	String[] androidStrings = getResources().getStringArray(R.array.shortcut_type_keys);  
			            	String shortcutType = androidStrings[shortcutTypePos];
			            	
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
					    		Log.e("WebShortcutApp", "Invalid shortcut type");
					    		return;
					    	}
			            	
					    	String filename = writer.createFullFilename(name);
					    	File shortcutFile = new File(shortcutDir, filename);
					    	shortcutFile.delete();
					    	try {
								writer.write(shortcutFile, name, url);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								Toast.makeText(thisActivity, "Error creating shortcut", Toast.LENGTH_LONG).show();
							}
					    	
					    	Intent sendIntent = new Intent();
					    	sendIntent.setAction(Intent.ACTION_SEND);
					    	
					    	// From http://stephendnicholas.com/archives/974
					    	sendIntent.putExtra(
					                Intent.EXTRA_STREAM,
					                    Uri.parse("content://" + CachedFileProvider.AUTHORITY + "/shareshortcuts/" + filename));

					    	//sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(shortcutFile));
					    	sendIntent.setType(mimeType);
					    	Toast.makeText(thisActivity, "content://" + CachedFileProvider.AUTHORITY + "/"
			                        + filename, Toast.LENGTH_LONG).show();
					    	Log.i("webshortcut", "content://" + CachedFileProvider.AUTHORITY + "/"
			                        + filename);
					    	sendIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

					    	try {
					    		startActivity(sendIntent);
					    	} catch(ActivityNotFoundException e) {
					    		Toast.makeText(thisActivity, getString(R.string.msg_no_app_for_share), Toast.LENGTH_LONG).show();
					    	}
			            }
			         })
			         .create();
	    	} catch(Exception e) {
	    		Log.e("WebShortcutApp", e.toString());
	    	}
	    }

		//finish();
	}
}
