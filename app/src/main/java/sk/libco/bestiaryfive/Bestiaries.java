package sk.libco.bestiaryfive;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.OpenableColumns;
import android.support.design.widget.Snackbar;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Bestiaries implements SRD.SRDEvents {

    private static final String TAG = "Bestiaries";

    private List<Bestiary> bestiaries = null;
    public Bestiary selectedBestiary = null;

    public List<String> spinnerList = new ArrayList<>();
    private BestiaryParser bestiaryParser = new BestiaryParser();

    private Activity context = null;
    private SqlMM sql = null;

    private BestiaryEvent bestiaryEvent;

    public Bestiaries(Activity context, BestiaryEvent bestiaryEvent) {
        this.context = context;
        this.sql = new SqlMM(context);
        this.bestiaryEvent = bestiaryEvent;

        load();
    }

    public int importBestiary(Uri uri) {

        int i = 0;
        InputStream is = null;
        Bestiary newBestiary = new Bestiary();
        try {
            newBestiary.name = getFileName(uri);

            showSnackbar("Importing " + newBestiary.name);

            newBestiary.uri = uri;
            is = context.getContentResolver().openInputStream(uri);
            newBestiary.monsters = bestiaryParser.parse(is);

            sql.addBestiary(newBestiary);

            showSnackbar("Imported " + newBestiary.monsters.size() + " monsters");

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

        load();

        return i;
    }

    public void deleteBestiary(int idToDelete) {
        sql.deleteBestiary(idToDelete);
        load();
    }

    private void load() {
        bestiaries = sql.getAllBestiaries();

        spinnerList.clear();
        for(Bestiary b:bestiaries) {
            spinnerList.add(b.name);
        }

        //
        if(selectedBestiary == null) {
            if(bestiaries.size() > 0) {
                selectedBestiary = bestiaries.get(0);
            }
        }

        if(bestiaries.size() == 0) {
            //load srd from web

            showSnackbar("Downloading 5e SRD");

            SRD srd = new SRD(this);
            srd.downloadFromWeb();
            bestiaries.add(srd);
        }

        bestiaryEvent.onBestiaryChange();
    }

    /****/

    public void loadMonsterDetails(Monster monster) {
        sql.loadMonsterDetails(monster);
    }

    public void setSelectedBestiary(int position) {
        if(bestiaries.size() > position && position >= 0) {
            selectedBestiary = bestiaries.get(position);
            Log.d(TAG,"bestiary set to: " + selectedBestiary.name);
        }
    }

   /* public void setSelectedBestiaryById(int id) {
       for(Bestiary b:bestiaries) {
           if(b.id == id) {
               selectedBestiary = b;
               Log.d(TAG, "bestiary set to: " + selectedBestiary.name);
               return;
           }
       }
        if(bestiaries.size() > 0) {
            selectedBestiary = bestiaries.get(0);
            Log.d(TAG,"Id of bestiary not found. bestiary set to: " + selectedBestiary.name);
        }
    }
*/
    public Monster getMonsterFromId(int id) {
        for(Bestiary b: bestiaries) {
            for (Monster m : b.monsters) {
                if (m.id == id) {
                    return m;
                }
            }
        }

        return null;
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

    public List<Bestiary> getBestiaries() {
        return bestiaries;
    }

    public void srdDownloadFinished() {

        for (Bestiary b:bestiaries) {
            if(b.id == 0) {

                if(b.monsters.size() > 0) {
                    showSnackbar("Downloaded " + b.monsters.size() + " 5e SRD monsters");
                    sql.addBestiary(b);
                } else {
                    showSnackbar("Error occurred while downloading 5e SRD");
                }
                break;
            }
        }

        spinnerList.clear();
        for(Bestiary b:bestiaries) {
            spinnerList.add(b.name);
        }
        if(selectedBestiary == null) {
            if(bestiaries.size() > 0) {
                selectedBestiary = bestiaries.get(0);
            }
        }

        bestiaryEvent.onBestiaryChange();


    }

    private void showSnackbar(String message) {

        Snackbar snackbar = Snackbar
                .make(context.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    public interface BestiaryEvent {
        void onBestiaryChange();
    }

}
