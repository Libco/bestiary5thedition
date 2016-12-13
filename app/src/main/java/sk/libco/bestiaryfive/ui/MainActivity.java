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
import sk.libco.bestiaryfive.SqlMM;

public class MainActivity extends AppCompatActivity implements MonsterListFragment.OnListFragmentInteractionListener{

    private static final String TAG = "MainActivity";

    Spinner spinner;
    ArrayAdapter<String> spinnerDataAdapter;

    private MenuItem searchItem;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    private Bestiaries bestiaries;
    //private AsyncTask parseTask;

    MainActivityInfoFragment infoFragment = null; //fragment for when nothing is imported
    MainActivityFragment monsterFragment = null; //fragment for single monster
    MonsterListFragment monsterListFragment = null; //fragment for monster list

    private String selectedMonster = "";

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

      /*  if (savedInstanceState != null) {
            Log.d(TAG, "Restoring Instance State");

            // Restore state members from saved instance
            selectedMonster = savedInstanceState.getString("selected_monster");

          //  setMonsterToView(bestiaries.getMonsterFromName(selectedMonster));
        }*/


        /** Parse monsters from file **/
        //bestiaries.load();
        //parseTask = new ParseTask().execute(Uri.EMPTY);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //check if parsing task is not running, if not make sure we have the right fragment.
        //if(parseTask != null && parseTask.getStatus() == AsyncTask.Status.FINISHED) {
        Log.d(TAG,"onStart: setting Fragment because task is finished");

        if(selectedMonster != null && !selectedMonster.isEmpty()) {
            setMonsterToView(bestiaries.getMonsterFromName(selectedMonster));
        } else
            setFragment();
        //}
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state

      /*  if(selectedMonster != null && !selectedMonster.isEmpty()) {
            Log.d(TAG,"saving selected monster: " + selectedMonster);
            savedInstanceState.putString("selected_monster", selectedMonster);
        }

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        //
        searchItem = menu.findItem(R.id.search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    if(bestiaries.selectedBestiary == null)
                        return true;

                    setFragment();

                    monsterListFragment.search(newText);

                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if(query.isEmpty() || bestiaries.selectedBestiary == null)
                        return true;

                    for (Monster m:bestiaries.selectedBestiary.monsters) {
                        if(m.name.toLowerCase().startsWith(query.toLowerCase())) {
                            setMonsterToView(m);
                            //TODO:
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

                BestiaryImportFragment bestiaryImportFragment = new BestiaryImportFragment();
                bestiaryImportFragment.setBestiaries(bestiaries);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.replace(R.id.fragment_container, bestiaryImportFragment, null);
                ft.commit();

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

    private void setFragment() {

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if(bestiaries.getBestiariesCount() > 0) {

            if (!(currentFragment instanceof MonsterListFragment)) {
                Log.d(TAG,"Showing monsterList fragment");
                if(monsterListFragment == null) {
                    monsterListFragment = new MonsterListFragment();
                }

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, monsterListFragment, null);
                ft.commit();
            } else {
                monsterListFragment = (MonsterListFragment) currentFragment;
            }

            //monsterListFragment.setMonsterList(bestiaries.selectedBestiary.monsters);

            if (!(currentFragment instanceof MainActivityFragment)) {
                /*Log.d(TAG,"Showing monster fragment");
                monsterFragment = new MainActivityFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, monsterFragment, null);
                ft.commit();*/
            } else {
                monsterFragment = (MainActivityFragment) currentFragment;
            }
        } else {
            if (!(currentFragment instanceof MainActivityInfoFragment)) {
                Log.d(TAG,"Showing info fragment");
                infoFragment = new MainActivityInfoFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, infoFragment, null);
                ft.commit();
            } else {
                infoFragment = (MainActivityInfoFragment) currentFragment;
            }
        }
    }

    private void selectBestiary(int position) {
        Log.d(TAG,"Setting bestiary to position: " + position);
        bestiaries.setSelectedBestiary(position);
        if(monsterListFragment != null)
            monsterListFragment.setMonsterList(bestiaries.selectedBestiary.monsters);
        //setMonsterToView(bestiaries.selectedBestiary.monsters.get(0));
    }

    private void setMonsterToView(Monster monster) {

        if(monster == null) {
            Log.d(TAG,"Monster to show is null");
            return;
        }

        selectedMonster = monster.name;
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (!(currentFragment instanceof MainActivityFragment)) {
            Log.d(TAG,"Showing monster fragment");
            if(monsterFragment == null)
                monsterFragment = new MainActivityFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            //ft.setCustomAnimations(R.animator.enter_from_right,
             //       R.animator.exit_to_right);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.replace(R.id.fragment_container, monsterFragment, null);
            ft.commit();
        } else {
            monsterFragment = (MainActivityFragment) currentFragment;
        }
        bestiaries.loadMonsterDetails(monster);
        monsterFragment.setMonsterToView(monster);

    }

    /**
     * Callback from MonsterListFragment
     * @param monster Monster user clicked
     */
    @Override
    public void onMonsterSelected(Monster monster) {
        Log.d(TAG,"User clicked on: " + monster.name);
       // searchView.clearFocus();
        setMonsterToView(monster);
        searchItem.collapseActionView();
    }
}
