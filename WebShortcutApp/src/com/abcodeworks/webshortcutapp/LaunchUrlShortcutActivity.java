package com.abcodeworks.webshortcutapp;

import com.abcodeworks.webshortcututil.read.ShortcutReader;
import com.abcodeworks.webshortcututil.read.UrlShortcutReader;

public class LaunchUrlShortcutActivity extends LaunchShortcutActivity {
	@Override
	protected ShortcutReader getShortcutReader() {
		return new UrlShortcutReader();
	}
}
