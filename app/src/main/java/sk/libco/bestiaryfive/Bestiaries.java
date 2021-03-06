package sk.libco.bestiaryfive;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.OpenableColumns;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Bestiaries implements SRD.SRDEvents {

    private static final String TAG = "Bestiaries";
    public Bestiary selectedBestiary = null;
    public List<String> spinnerList = new ArrayList<>();
    private List<Bestiary> bestiaries = null;
    private BestiaryParser bestiaryParser = new BestiaryParser();

    private Activity context = null;
    private SqlMM sql = null;

    private BestiaryEvent bestiaryEvent;

    public Bestiaries(Activity context, BestiaryEvent bestiaryEvent) {
        this.context = context;
        this.sql = SqlMM.getInstance(context);
        this.bestiaryEvent = bestiaryEvent;

        load();
    }

    int importBestiary(Uri uri) {

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

            showSnackbar("Imported " + newBestiary.monsters.getMonsters().size() + " monsters");

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

            SRD srd = new SRD(context, this);
            srd.downloadFromWeb();
            bestiaries.add(srd);
        }

        bestiaryEvent.onBestiaryChange();
    }

    /****/

    public void loadMonsterDetails(Monster monster) {
        sql.loadMonsterDetails(monster);
    }

    public int setSelectedBestiary(int position) {
        if(bestiaries.size() > position && position >= 0) {
            selectedBestiary = bestiaries.get(position);
            if (selectedBestiary.id >= 0 && selectedBestiary.monsters.getMonsters().size() == 0) {
                selectedBestiary.monsters = sql.getMonstersForBestiary(selectedBestiary.id);
            }
            Log.d(TAG,"bestiary set to: " + selectedBestiary.name);
            bestiaryEvent.onSelectedBestiaryChange();
            return position;
        } else {
            return 0;
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
            for (Monster m : b.monsters.getMonsters()) {
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

    public int getBestiariesWithoutAllCount() {
        int count = 0;
        if (bestiaries != null) {
            for (Bestiary b : bestiaries) {
                if (b.id >= 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public List<Bestiary> getBestiaries() {
        return bestiaries;
    }

    public List<Bestiary> getBestiariesWithoutAll() {

        List<Bestiary> bList = new ArrayList<>();
        if (bestiaries != null) {
            for (Bestiary b : bestiaries) {
                if (b.id >= 0) {
                    bList.add(b);
                }
            }
        }

        return bList;
    }

    public void srdDownloadFinished() {

        for (Bestiary b:bestiaries) {
            if(b.id == 0) {

                if(b.monsters.getMonsters().size() > 0) {
                    showSnackbar("Downloaded " + b.monsters.getMonsters().size() + " 5e SRD monsters");
                    sql.addBestiary(b);
                } else {
                    showSnackbar("Error occurred while downloading 5e SRD");
                }
                break;
            }
        }

        load();
    }

    private void showSnackbar(String message) {

        Snackbar snackbar = Snackbar
                .make(context.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    public interface BestiaryEvent {
        void onBestiaryChange();
        void onSelectedBestiaryChange();
    }


    //

    public static class ParseTask extends AsyncTask<Uri, Integer, Integer> {

        private final Bestiaries bestiaries;

        public ParseTask(Bestiaries bestiaries) {
            this.bestiaries = bestiaries;
        }

        protected Integer doInBackground(Uri... uris) {

            int monstersParsed = 0;

            for (Uri uri : uris) {
                monstersParsed += bestiaries.importBestiary(uri);
            }

            return monstersParsed;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Integer result) {
            bestiaries.load();
        }
    }

}
