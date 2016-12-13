package sk.libco.bestiaryfive;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.OpenableColumns;
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

public class Bestiaries {

    private static final String TAG = "Bestiaries";

    private List<Bestiary> bestiaries = null;
    public Bestiary selectedBestiary = null;

    public List<String> spinnerList = new ArrayList<>();
    private BestiaryParser bestiaryParser = new BestiaryParser();

    private Activity context = null;
    private SqlMM sql = null;

    public Bestiaries(Activity context) {
        this.context = context;
        this.sql = new SqlMM(context);

        load();
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

            sql.addBestiary(newBestiary);

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

            new DownloadFileFromURL().execute("https://raw.githubusercontent.com/Libco/bestiary5thedition/master/files/5e-SRD-Monsters.json");

        }

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

    //TODO:deprecated propably
    public Monster getMonsterFromName(String name) {
        if(selectedBestiary != null) {
            for (Monster m : selectedBestiary.monsters) {
                if (m.name.equals(name))
                    return m;
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

    //

    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            String strFileContents = "";
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    //publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to string
                    strFileContents += new String(data, 0, count);
                }

                // closing streams
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            BestiaryParserJson bestiaryParserJson = new BestiaryParserJson();
            Bestiary newBestiary = bestiaryParserJson.parse("5e SRD",strFileContents);


            sql.addBestiary(newBestiary);
            load();

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            //pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
           // dismissDialog(progress_bar_type);

        }

    }

}
