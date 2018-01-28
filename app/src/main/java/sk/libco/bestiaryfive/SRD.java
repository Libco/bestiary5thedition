package sk.libco.bestiaryfive;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class SRD extends Bestiary{

    static boolean downloadInProgress = false;
    SRDEvents srdEvents;

    public SRD(SRDEvents srdEvents) {
        this.srdEvents = srdEvents;
        id = 0;
        name = "5e SRD";
    }

    void downloadFromWeb() {
        if (downloadInProgress) {
            return;
        }

        downloadInProgress = true;
        new DownloadFileFromURL(this).execute("https://raw.githubusercontent.com/Libco/bestiary5thedition/master/files/5e-SRD-Monsters.json");
    }

    public interface SRDEvents {
        void srdDownloadFinished();
    }

    /**
     * Background Async Task to download file
     * */
    private static class DownloadFileFromURL extends AsyncTask<String, String, String> {

        private final SRD srd;

        DownloadFileFromURL(SRD srd) {
            this.srd = srd;
        }

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

                //  0-100%
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
