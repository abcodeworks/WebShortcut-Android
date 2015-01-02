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

import java.io.File;
import java.util.Arrays;

import com.abcodeworks.webshortcututil.write.DesktopShortcutWriter;
import com.abcodeworks.webshortcututil.write.ShortcutWriter;
import com.abcodeworks.webshortcututil.write.UrlShortcutWriter;
import com.abcodeworks.webshortcututil.write.WeblocBinaryShortcutWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

// See:
// http://stackoverflow.com/questions/14099252/delete-file-after-sent-to-mail-as-intent-action-send

public class CreateShortcutActivity extends Activity {
	Dialog dialog = null;
	
	/* Assume that the dialog was created in onCreate.
	 * Note that onStart can be called multiple times.
	 * Therefore, the dialog field should be nulled out
	 * if we don't want to re-show the dialog the next time
	 * onStart is called.
	 */
	@Override
	protected void onStart() {
		super.onStart();
		if(dialog != null) {
			dialog.show();
		}
	}
	
	/* When the activity is stopped or paused, dismiss the dialog
	 * and discard it (null it out so that it won't be redisplayed
	 * the next time onStart is called.  Note that the activity
	 * is paused when the share intent is sent with the URL.
	 * I am not sure if this is the correct way to do things,
	 * but it seems to work.
	 */
	@Override
	protected void onStop() {
		super.onStop();
		if(dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
		// Assume that we no longer need the activity
		// when it is stopped.  This may be redundant...
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
	
	/* Receives the intent and creates a dialog to prompt the user
	 * for the file name and shortcut type.
	 * Note that some variables are made final so that they can
	 * be referenced from the anonymous dialog class.
	 * In order to share the created shortcut file,
	 * we use a content provider.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final Activity thisActivity = this;
		
		PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);

		Intent intent = getIntent();
		//Log.i("WebShortcut", "In Activity");
	    if (Intent.ACTION_SEND.equals(intent.getAction())) {
	    	try {
	    		/* Get the information from the intent. */
		    	//Log.i("WebShortcut", "Got send intent...");
		    	Bundle extras = intent.getExtras();
		    	
		    	final String url = extras.getString(Intent.EXTRA_TEXT);
		    	if(url == null) {
		    		Toast.makeText(this, getString(R.string.msg_no_intent_extra_text), Toast.LENGTH_LONG).show();
		    		finish();
		    		return;
		    	}
		    	
		    	// Get the title - use the url if there is not title.
		    	String subject = extras.getString(Intent.EXTRA_SUBJECT);
		    	final String originalName = (subject == null) ? url : subject;
		    	

		    	//Log.i("WebShortcut", "received TEXT: " + extras.getString(Intent.EXTRA_TEXT));
		    	//Log.i("WebShortcut", "received SUBJECT: " + extras.getString(Intent.EXTRA_SUBJECT));
		    	
		    	// Get the cache folder and make a folder (if it doesn't exist already)
		    	// This is where we will create and store the shortcut.
		    	File cacheDir = getCacheDir();
		    	final File shortcutDir = new File(cacheDir, "shareshortcuts");
		    	shortcutDir.mkdir();
		    	
		    	/* Delete any files that are in the folder.
		    	 * In theory, if we perform this every time,
		    	 * there should only be one shortcut in the cache at a time.
		    	 * The shortcut that is here is the last one that was generated.
		    	 * Is it safe to delete it?  Is it possible that it is still in use
		    	 * (for example if the last shortcut that was shared with an email
		    	 * client, maybe the client has not yet retrieved the shortcut
		    	 * to send out)?
		    	 * As an alternative, we should probably implement a queue where
		    	 * several shortcuts are kept around and old ones deleted as new ones
		    	 * are created.  Or delete them when they reach a certain age. Can we 
		    	 * use time stamps?
		    	 * We could also just leave the files there indefinitely and leave
		    	 * it to the user to clear the cache, but this would be bad practice.
		    	 * For now, I am leaving this as-is... */
		    	for(File file: shortcutDir.listFiles()) {
		    		file.delete();
		    	}
		    	
		    	// Get the default shortcut type from the settings.
		    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		    	String defaultShortcutType = prefs.getString(getString(R.string.pref_default_create_shortcut_type), null);
		    	//System.out.println(shortcutType);
		    	
		    	/* For our dialog box, we are using a custom view.  We need to be able to modify
		    	 * the widgets first.  In order to do this, we manually inflate the view here so
		    	 * that we have a handle to it.
		    	 */
		    	// See: http://stackoverflow.com/questions/2795300/how-to-implement-a-custom-alertdialog-view
		    	LayoutInflater inflater = getLayoutInflater();
		    	
		    	/* It is recommended to use the body or custom frames as the root.
		    	 * However body gives a compile error (not found), and custom compiles
		    	 * but returns null when finding the view. 
		    	 * Passing in null as the root for inflation gives a warning, but
		    	 * I am not sure what else to do without more research or experimentation...*/
		    	// FrameLayout frame = (FrameLayout) findViewById(android.R.id.body);
		    	// FrameLayout frame = (FrameLayout) findViewById(android.R.id.custom);
		    	View dialogView = inflater.inflate(R.layout.dialog_create_shortcut, null);
		    	//frame.addView(dialogView);
		    	
		    	/* Get the title of the link and create a file name from it.  This will be the default
		    	 * file name unless the user changes it.  Note we do not include the extension
		    	 * to make it more user friendly and so that the user still has the option
		    	 * to change the type.  We will add the extension later.
		    	 * In order to create a filename we need to instantiate a specific writer.
		    	 * We arbitrarily use the URL writer - since no extension is included,
		    	 * it should not matter which one we use.
		    	 */
		    	ShortcutWriter dummyWriter = new UrlShortcutWriter();
		    	final String defaultFilename = dummyWriter.createBaseFilename(originalName);
		    	
		    	/* Get the file name text entry and set its initial value.  Note that we set the text
		    	 * so that the user can edit the text.  If we use a hint instead, when the user
		    	 * makes a change it overwrites the existing text completely.
		    	 */
		    	final EditText editFilename = (EditText)dialogView.findViewById(R.id.dialog_create_shortcut_edit_filename);
		    	editFilename.setText(defaultFilename);
		    	
		    	/* Initialize the shortcut type on the dialog box from the default
		    	 * type setting.  This way, if the user does not change the type, the default
		    	 * will be used.
		    	 */
		    	final Spinner shortcutTypeSpinner = (Spinner)dialogView.findViewById(R.id.dialog_create_shortcut_shortcut_type);
		    	String[] androidStrings = getResources().getStringArray(R.array.shortcut_type_keys);   
		    	int index = Arrays.asList(androidStrings).indexOf(defaultShortcutType);
		    	shortcutTypeSpinner.setSelection(index);
		    	
		    	// Create the dialog box
		    	dialog = new AlertDialog.Builder(this)
			        .setTitle(getString(R.string.dialog_create_shortcut_title))
			        .setIcon(R.drawable.ic_launcher)
			        .setView(dialogView)
			        .setPositiveButton(R.string.dialog_create_shortcut_positive_button, new DialogInterface.OnClickListener() {
			        	// Handler for when the user clicks the create button.
			            public void onClick(DialogInterface dialog, int which) { 
			            	/* Get the file name name that the user entered */
			            	String newFilename = editFilename.getText().toString();
			            	
			            	/* Get the key for the shortcut type.  The spinner uses the user friendly
			            	 * shortcut names, but we want the key.  We need ot look it up
			            	 * by position.
			            	 */
			            	int shortcutTypePos = shortcutTypeSpinner.getSelectedItemPosition();
			            	String[] androidStrings = getResources().getStringArray(R.array.shortcut_type_keys);  
			            	String shortcutType = androidStrings[shortcutTypePos];
			            	
			            	/* Set the mime type and instantiate the writer based on the chosen shortcut type */
					    	String mimeType = null;
					    	ShortcutWriter writer = null;
					    	if(shortcutType.equals("url")) {
					    		mimeType = "text/x-url";
					    		writer = new UrlShortcutWriter();
					    	} else if(shortcutType.equals("desktop")) {
					    		mimeType = "application/x-desktop";
					    		writer = new DesktopShortcutWriter();
					    	} else if(shortcutType.equals("webloc")) {
					    		mimeType = "application/x-webloc";
					    		writer = new WeblocBinaryShortcutWriter();
					    	} else {
					    		Log.e("WebShortcutApp", "Invalid shortcut type");
					    		return;
					    	}
			            	
					    	/* Create the full file name with the extension, and prepare
					    	 * our file.
					    	 */
					    	String fullFilename = writer.createFullFilename(newFilename);
					    	File shortcutFile = new File(shortcutDir, fullFilename);
					    	/* Assume there are no files in the folder - we only need to
					    	 * delete if there is a possibility of a name collision.
					    	 * But it doesn't hurt to do the delete if the file does not exist...
					    	 */
					    	//shortcutFile.delete();
					    	
					    	/* Create the shortcut.  Show a toast if there was an error. */
					    	try {
								writer.write(shortcutFile, originalName, url);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								Toast.makeText(thisActivity, "Error creating shortcut", Toast.LENGTH_LONG).show();
							}
					    	
					    	/* Create the intent to share the new file. */
					    	Intent sendIntent = new Intent();
					    	sendIntent.setAction(Intent.ACTION_SEND);
					    	sendIntent.setType(mimeType);
					    	String uri = "content://" + CachedFileProvider.AUTHORITY + "/shareshortcuts/" + fullFilename;
					    	sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri));
					    	sendIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

					    	/* Send the intent */
					    	try {
					    		startActivity(sendIntent);
					    		thisActivity.finish();
					    	} catch(ActivityNotFoundException e) {
					    		Toast.makeText(thisActivity, getString(R.string.msg_no_app_for_share), Toast.LENGTH_LONG).show();
					    	}
			            }
			         })
			         .setNegativeButton(R.string.dialog_create_shortcut_negative_button, new DialogInterface.OnClickListener() {
				        	// Handler for when the user clicks the create button.
				            public void onClick(DialogInterface dialog, int which) { 
				            	// If canceled, just clean up
				            	thisActivity.finish();
				            }
				         })
				     // It appears this is called when the back button is pressed while on the dialog
				     .setOnCancelListener(new DialogInterface.OnCancelListener() {                   
                            @Override
                            public void onCancel(DialogInterface dialog) 
                            {
                            	// If canceled, just clean up
        	                    thisActivity.finish();                        

                            }
                         })
			         .create();
	    	} catch(Exception e) {
	    		Log.e("WebShortcutApp", e.toString());
	    	}
	    }
	}
}
