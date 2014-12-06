package com.abcodeworks.webshortcutapp;

// Copied (with minor modifications from http://stephendnicholas.com/archives/974)
// Per comments on website, this is licensed under MIT License.
 
import java.io.File;
import java.io.FileNotFoundException;
 
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.util.Log;
 
public class CachedFileProvider extends ContentProvider {
 
    private static final String CLASS_NAME = "CachedFileProvider";
 
    // The authority is the symbolic name for the provider class
    // We should reference the strings xml, but getting the context is tricky...
    public static final String AUTHORITY = "com.abcodeworks.webshortcutapp.provider";
 
    // UriMatcher used to match against incoming requests
    private UriMatcher uriMatcher;
 
    @Override
    public boolean onCreate() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
 
        // Add a URI to the matcher which will match against the form
        // 'content://com.stephendnicholas.gmailattach.provider/*'
        // and return 1 in the case that the incoming Uri matches this pattern
        uriMatcher.addURI(AUTHORITY, "shareshortcuts/*", 1);
 
        return true;
    }
 
    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode)
            throws FileNotFoundException {
 
        String LOG_TAG = CLASS_NAME + " - openFile";
 
        //Log.v(LOG_TAG,
        //        "Called with uri: '" + uri + "'." + uri.getPath()/*uri.getLastPathSegment()*/);
 
        // Check incoming Uri against the matcher
        switch (uriMatcher.match(uri)) { 
 
        // If it returns 1 - then it matches the Uri defined in onCreate
        case 1:
 
            // The desired file name is specified by the last segment of the
            // path
            // E.g.
            // 'content://com.stephendnicholas.gmailattach.provider/Test.txt'
            // Take this and build the path to the file
            String fileLocation = getContext().getCacheDir() 
                    + uri.getPath()/*uri.getLastPathSegment())*/;
            
            Log.v(LOG_TAG,
                    "File location: '" + fileLocation);
 
            // Create & return a ParcelFileDescriptor pointing to the file
            // Note: I don't care what mode they ask for - they're only getting
            // read only
            ParcelFileDescriptor pfd = ParcelFileDescriptor.open(new File(
                    fileLocation), ParcelFileDescriptor.MODE_READ_ONLY);
            return pfd;
 
            // Otherwise unrecognised Uri
        default:
            Log.v(LOG_TAG, "Unsupported uri: '" + uri + "'.");
            throw new FileNotFoundException("Unsupported uri: "
                    + uri.toString());
        }
    }
 
    // //////////////////////////////////////////////////////////////
    // Not supported / used / required for this example
    // //////////////////////////////////////////////////////////////
 
    @Override
    public int update(Uri uri, ContentValues contentvalues, String s,
            String[] as) {
        return 0;
    }
 
    @Override
    public int delete(Uri uri, String s, String[] as) {
        return 0;
    }
 
    @Override
    public Uri insert(Uri uri, ContentValues contentvalues) {
        return null;
    }
 
    /*
    @Override
    public String getType(Uri uri) {
        return null;
    }
    
    @Override
    public Cursor query(Uri uri, String[] projection, String s, String[] as1,
            String s1) {
        return null;
    }
    */
    
    /* The following code is from the comments section in:
     * http://stephendnicholas.com/archives/974(non-Javadoc)
     */
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            // If it returns 1 - then it matches the Uri defined in onCreate
            case 1:
            	// We should use the specific mime type for our shortcut.
            	// But this is easier...
                return "application/octet-stream";
                default:
            return null;
        }
    }

    @Override
    public Cursor query(Uri uri, String[] arg1, String arg2, String[] arg3, String arg4) {
        switch (uriMatcher.match(uri)) {
            // If it returns 1 - then it matches the Uri defined in onCreate
            case 1:
                MatrixCursor cursor = null;

                File file = new File( getContext().getCacheDir() + File.separator
                                      + uri.getLastPathSegment());
                if (file.exists()) {
                    cursor = new MatrixCursor(new String[] {
                        OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE });
                        cursor.addRow(new Object[] {
                        		uri.getLastPathSegment(),
                                file.length()
                        } );
                }

	            return cursor;

            default:
                return null;
        }
    }

}
