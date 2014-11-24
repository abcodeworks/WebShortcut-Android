package com.abcodeworks.webshortcutapp;

import com.abcodeworks.webshortcututil.read.ShortcutReader;
import com.abcodeworks.webshortcututil.read.WeblocShortcutReader;

public class LaunchWeblocShortcutActivity extends LaunchShortcutActivity {
	@Override
	protected ShortcutReader getShortcutReader() {
		return new WeblocShortcutReader();
	}
}
