package com.abcodeworks.webshortcutapp;

import com.abcodeworks.webshortcututil.read.DesktopShortcutReader;
import com.abcodeworks.webshortcututil.read.ShortcutReader;

public class LaunchDesktopShortcutActivity extends LaunchShortcutActivity {
	@Override
	protected ShortcutReader getShortcutReader() {
		return new DesktopShortcutReader();
	}
}
