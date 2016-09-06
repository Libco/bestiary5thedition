package sk.libco.bestiaryfive;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Bestiaries {

    private static final String TAG = "Bestiaries";

    private List<Bestiary> bestiaries = new ArrayList<>();
    public Bestiary selectedBestiary = null;

    public List<String> spinnerList = new ArrayList<>();
    private BestiaryParser bestiaryParser = new BestiaryParser();

    private Activity context = null;

    public Bestiaries(Activity context) {
        this.context = context;
    }


    public int importBestiary(Uri uri) {

        int i = 0;
        InputStream is = null;
        Bestiary newBestiary = new Bestiary();
        try {
            newBestiary.name = getFileName(uri);
            newBestiary.uri = uri;
            is = context.getContentResolver().openInputStream(uri);
            newBestiary.monsters = bestiaryParser.parse(is);

        } catch (Exception e) {
            //log the exception
            Log.e(TAG, e.toString());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }

        if(newBestiary.monsters != null) {
            Log.d(TAG, "Parsed " + newBestiary.monsters.size() + " monsters to " + newBestiary.name);
            bestiaries.add(newBestiary);
            spinnerList.add(newBestiary.name);
            i = newBestiary.monsters.size();
        } else {
            Log.d(TAG,"PARSING ERROR.");
        }

        //
        if(selectedBestiary == null) {
            if(bestiaries.size() > 0) {
                selectedBestiary = bestiaries.get(0);
            }
        }

        save();
        return i;
    }

    public void save() {

        Set<String> bSet = new HashSet<>();

        for(Bestiary b:bestiaries) {
            bSet.add(b.uri.toString());
        }

        SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(context.getString(R.string.bestiaries_save_string),bSet);
        editor.apply();
        Log.d(TAG,"Saving " + bestiaries.size() + " bestiaries.");
    }

    public int load() {

        SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);
        Set<String> bSet = sharedPref.getStringSet(context.getString(R.string.bestiaries_save_string), null);

        if(bSet == null) {
            Log.d(TAG,"nothing to load");
            return -1;
        }

        Log.d(TAG,"Loading " + bSet.size() + " bestiaries.");
        int i = 0;
        for(String uri:bSet) {
            i += importBestiary(Uri.parse(uri));
        }
        return i;
    }

    /****/

    public void setSelectedBestiary(int position) {
        if(bestiaries.size() > position && position >= 0) {
            selectedBestiary = bestiaries.get(position);
            Log.d(TAG,"bestiary set to: " + selectedBestiary.name);
        }
    }

    private String getFileName(Uri uri) {

        String displayName = "Unknown name";
        // The query, since it only applies to a single document, will only return
        // one row. There's no need to filter, sort, or select fields, since we want
        // all fields for one document.
        Cursor cursor = context.getContentResolver()
                .query(uri, null, null, null, null, null);

        try {
            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (cursor != null && cursor.moveToFirst()) {

                // Note it's called "Display Name".  This is
                // provider-specific, and might not necessarily be the file name.
                displayName = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                int pos = displayName.lastIndexOf(".");
                if (pos > 0) {
                    displayName = displayName.substring(0, pos);
                }
                Log.d(TAG, "Display Name: " + displayName);
            }
        } finally {
            cursor.close();
        }
        return displayName;
    }

    public int getBestiariesCount() {
        return bestiaries.size();
    }



}
