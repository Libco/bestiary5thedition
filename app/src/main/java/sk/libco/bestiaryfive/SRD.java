package sk.libco.bestiaryfive;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class SRD extends Bestiary{

    static boolean downloadInProgress = false;
    SRDEvents srdEvents;

    private Context context;

    public SRD(Context context, SRDEvents srdEvents) {
        this.srdEvents = srdEvents;
        this.context = context;
        id = 0;
        name = "5e SRD";
    }

    void downloadFromWeb() {
        if (downloadInProgress) {
            return;
        }

        downloadInProgress = true;
        new DownloadFileFromURL(this, context).execute("srd_5e.json");
    }

    public interface SRDEvents {
        void srdDownloadFinished();
    }

    /**
     * Background Async Task to download file
     * */
    private static class DownloadFileFromURL extends AsyncTask<String, String, String> {

        private final SRD srd;
        private final Context context;

        DownloadFileFromURL(SRD srd, Context context) {
            this.srd = srd;
            this.context = context;
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {

            String strFileContents = "";

            try {
                InputStream stream = context.getAssets().open(f_url[0]);

                int size = stream.available();
                byte[] buffer = new byte[size];
                stream.read(buffer);
                stream.close();
                strFileContents = new String(buffer);

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            BestiaryParserJson bestiaryParserJson = new BestiaryParserJson();
            Bestiary b = bestiaryParserJson.parse("5e SRD",strFileContents);
            if(b != null) {
                srd.id = b.id;
                srd.name = b.name;
                srd.uri = b.uri;
                srd.monsters = b.monsters;
            }

            return "";
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
        protected void onPostExecute(String str) {
            // dismiss the dialog after the file was downloaded
            // dismissDialog(progress_bar_type);
            downloadInProgress = false;
            srd.srdEvents.srdDownloadFinished();
        }

    }

}
