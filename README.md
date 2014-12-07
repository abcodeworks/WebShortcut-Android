WebShortcut (Android App)
=========================

Summary
-------
An Android app that allows you to launch and create web/internet shortcuts.

Websites
--------
Android Marketplace: http://play.google.com/store/apps/details?id=com.abcodeworks.webshortcutapp<br/>
Main Website: http://beckus.github.io/WebShortcutUtil/<br/>
Source Code: https://github.com/abcodeworks/WebShortcut-Android/<br/>

Building
--------
The root folder is a workspace which contains two projects:
one which contains the main Android app, and the other
which contains the unit tester.  The projects were created using
Eclipse ADT.  They should be built and tested using the Eclipse ADT.
The root folder structure is not standard (usually the root is a project,
not a workspace) but it seemed best given that we need two projects.

Testing
-------
I wrote limited automated unit test cases in the
WebShortcutAppTest project.  This testing should be improved.
These tests should be manually performed:
- Change the settings.  Make sure the activities are enabled/disabled
  as appropriate.
- Make sure the links on the settings page work.
- Try viewing URL, Desktop, and Webloc shortcuts.
- Enable the "treat all files as shortcuts".
  Retry viewing URL, Desktop, and Webloc shortcuts.
- Try creating URL, Desktop, and Webloc shortcuts.
- Try hitting the back button at various points in the
  shortcut creation dialog sequence.
- Try hitting the cancel button on the shortcut creation dialog.
- Try changing the file name and shortcut type on the create shortcut dialog.
- Make sure default create shortcut type setting works.

Generate APK (Deployment)
-------------------------
Use the File | Export... option and choose "Export Android Application".

Notes
------
- Per the Android guidelines, there should be no debug logging.
  Since this is a small project, I chose to just comment them out.

Libraries and Licenses
----------------------
This project is released under GPLv3 (this is driven by the fact
that it uses icons released under GPLv3).

The icon was built using icons available on Wikimedia Commons.
See the icon folder for details and licenses.

The following source code is used:
-   CachedFileProvider - http://stephendnicholas.com/archives/974 - MIT License

The following library is used:
-   com.abcodworks.webshortcututil - Apache License 2.0

webshortcutuil includes the following libraries:
-   com.dd.plist - http://code.google.com/p/plist/ - MIT License
-   com.beetstra.jutf7 http://jutf7.sourceforge.net/ - MIT License

All licenses can be found in the root folder.

Author
------
Andre Beckus
 
