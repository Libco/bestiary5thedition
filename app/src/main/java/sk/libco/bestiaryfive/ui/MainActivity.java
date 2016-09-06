package sk.libco.bestiaryfive.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import sk.libco.bestiaryfive.Bestiaries;
import sk.libco.bestiaryfive.Monster;
import sk.libco.bestiaryfive.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    Spinner spinner;
    ArrayAdapter<String> spinnerDataAdapter;

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    private Bestiaries bestiaries;

    MainActivityInfoFragment infoFragment = null; //fragment for when nothing is imported
    MainActivityFragment monsterFragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /****/
        bestiaries = new Bestiaries(this);

        /** Set toolbar **/
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (NullPointerException e){
            Log.e(TAG,"Toolbar error: " + e.toString());
        }

        /** Toolbar spinner **/
        spinner = (Spinner) findViewById(R.id.spinner_nav);

        //list.add("Monster Manual");
        spinnerDataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, bestiaries.spinnerList);
        spinnerDataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerDataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectBestiary(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        /** Parse monsters from file **/
        //bestiaries.load();
        new ParseTask().execute(Uri.EMPTY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        //
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    if(newText.isEmpty() || bestiaries.selectedBestiary == null)
                        return true;

                    for (Monster m:bestiaries.selectedBestiary.monsters) {
                        if(m.name.toLowerCase().startsWith(newText.toLowerCase())) {
                            setMonsterToView(m);
                            break;
                        }
                    }

                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if(query.isEmpty() || bestiaries.selectedBestiary == null)
                        return true;

                    for (Monster m:bestiaries.selectedBestiary.monsters) {
                        if(m.name.toLowerCase().startsWith(query.toLowerCase())) {
                            setMonsterToView(m);
                            break;
                        }
                    }

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                // Not implemented here
                return false;
            case R.id.import_file:
                Log.d(TAG, "add new bestiary");
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                startActivityForResult(intent, 42);
                return true;
            case R.id.help:
                InfoDialog infoDialog = InfoDialog.newInstance(0);
                infoDialog.show(getSupportFragmentManager(), "Info");
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 42) {
            if(resultCode == Activity.RESULT_OK){
                Uri pathUri = data.getData();
                Log.d(TAG,"ActivityResult: " + pathUri.toString());

                /** Parse monsters from file **/
                new ParseTask().execute(pathUri);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    private class ParseTask extends AsyncTask<Uri, Integer, Integer> {
        protected Integer doInBackground(Uri... uris) {

            int monstersParsed = 0;

            if(uris.length == 1 && uris[0] == Uri.EMPTY) {
                monstersParsed = bestiaries.load();
            } else {

                for (Uri uri : uris) {
                    monstersParsed += bestiaries.importBestiary(uri);
                }
            }
            return monstersParsed;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Integer result) {

            if(bestiaries.selectedBestiary != null) {
                spinnerDataAdapter.notifyDataSetChanged();
                spinner.setSelection(spinnerDataAdapter.getPosition(bestiaries.selectedBestiary.name));
                setMonsterToView(bestiaries.selectedBestiary.monsters.get(0));

                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.activity_main), "Loaded " + result + " monsters.", Snackbar.LENGTH_LONG);

                snackbar.show();
            }

            setFragment();

        }
    }

    private void setFragment() {

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if(bestiaries.getBestiariesCount() > 0) {
            if (!(currentFragment instanceof MainActivityFragment)) {
                Log.d(TAG,"Showing monster fragment");
                monsterFragment = new MainActivityFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, monsterFragment, null);
                ft.commit();
            }
        } else {
            if (!(currentFragment instanceof MainActivityInfoFragment)) {
                Log.d(TAG,"Showing info fragment");
                infoFragment = new MainActivityInfoFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, infoFragment, null);
                ft.commit();
            }
        }
    }

    private void selectBestiary(int position) {
        Log.d(TAG,"Setting bestiary to position: " + position);
        bestiaries.setSelectedBestiary(position);
        setMonsterToView(bestiaries.selectedBestiary.monsters.get(0));
    }

    private void setMonsterToView(Monster monster) {
        if(monsterFragment == null)
            return;

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof MainActivityFragment) {
            monsterFragment.setMonsterToView(monster);
        }
    }


}
