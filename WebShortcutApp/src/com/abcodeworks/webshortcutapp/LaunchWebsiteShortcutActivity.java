package com.abcodeworks.webshortcutapp;

import com.abcodeworks.webshortcututil.read.ShortcutReader;
import com.abcodeworks.webshortcututil.read.WebsiteShortcutReader;

public class LaunchWebsiteShortcutActivity extends LaunchShortcutActivity {
	@Override
	protected ShortcutReader getShortcutReader() {
		return new WebsiteShortcutReader();
	}
}
