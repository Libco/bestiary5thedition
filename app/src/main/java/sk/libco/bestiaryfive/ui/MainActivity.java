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

public class MainActivity extends AppCompatActivity implements MonsterListFragment.OnListFragmentInteractionListener, Bestiaries.BestiaryEvent {

    private static final String TAG = "MainActivity";

    boolean addList = false;

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

    private int selectedMonster = -1;
    private int selectedBestiary = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /****/
        bestiaries = new Bestiaries(this, this);

        /** Set toolbar **/
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (NullPointerException e){
            Log.e(TAG,"Toolbar error: " + e.toString());
        }

        /** Toolbar spinner **/
        spinner = findViewById(R.id.spinner_nav);

        //list.add("Monster Manual");
        spinnerDataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, bestiaries.spinnerList);
        spinnerDataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerDataAdapter);
        //spinner.setSelection(0, false);
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

        if (savedInstanceState != null) {
            Log.d(TAG, "Restoring Instance State");
            // Restore state members from saved instance
            selectedMonster = savedInstanceState.getInt("selected_monster");
            selectedBestiary = savedInstanceState.getInt("selected_bestiary");
            //bestiaries.setSelectedBestiary(selectedBestiary);
        }

        /** fragment **/
        //setFragment();

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        if(selectedMonster > 0) {
            Log.d(TAG,"saving selected monster: " + selectedMonster);
            savedInstanceState.putInt("selected_monster", selectedMonster);
            savedInstanceState.putInt("selected_bestiary", selectedBestiary);
        }

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    //
    @Override
    protected void onResume() {
        /*FragmentTransaction tx = this.getSupportFragmentManager().beginTransaction();
        if (this.addList) {
            MonsterListFragment list = this.monsterListFragment;
            tx.add(R.id.fragment_container, list, "list_fragment_tag");
        }

        tx.commit();*/
        super.onResume();

        //this.addList = false;

    }



    @Override
    protected void onPause() {

        /*this.addList = this.monsterListFragment != null ? this.monsterListFragment.isAdded() : false;

        if (this.addList) {
            FragmentTransaction tx = this.getSupportFragmentManager().beginTransaction();
            tx.remove(this.monsterListFragment);
            tx.commit();
        }
        this.getSupportFragmentManager().executePendingTransactions();*/
        super.onPause();

    }
    //

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

                    selectedMonster = -1;

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

                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                if (!(currentFragment instanceof BestiaryImportFragment)) {
                    BestiaryImportFragment bestiaryImportFragment = new BestiaryImportFragment();
                    bestiaryImportFragment.setBestiaries(bestiaries);
                    FragmentTransaction ft = replaceFragment(bestiaryImportFragment, false);
                    if (ft != null) {
                        ft.addToBackStack(null);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.commit();
                    }
                }

                return true;
            case R.id.help:
                Intent intent = new Intent(this, InfoActivity.class);
                startActivity(intent);
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    private void setFragment() {

        spinner.setSelection(selectedBestiary, false);

        //
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if(selectedMonster > 0) {
            setMonsterToView(bestiaries.getMonsterFromId(selectedMonster));
            return;
        }

        if(bestiaries.getBestiariesCount() > 0) {

            if (!(currentFragment instanceof MonsterListFragment)) {
                Log.d(TAG,"Showing monsterList fragment");
                if(monsterListFragment == null) {
                    monsterListFragment = new MonsterListFragment();
                }

                replaceFragment(monsterListFragment,true);
            } else {
                monsterListFragment = (MonsterListFragment) currentFragment;
            }

            if(monsterListFragment != null && bestiaries != null && bestiaries.selectedBestiary != null)
            {
                monsterListFragment.setMonsterList(bestiaries.selectedBestiary.monsters);
            }

        } else {
            if (!(currentFragment instanceof MainActivityInfoFragment)) {
                Log.d(TAG,"Showing info fragment");
                infoFragment = new MainActivityInfoFragment();
                replaceFragment(infoFragment,true);
            } else {
                infoFragment = (MainActivityInfoFragment) currentFragment;
            }
        }
    }

    private FragmentTransaction replaceFragment(Fragment newFragment, boolean commit) {

        //Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if(newFragment.isAdded() /*|| (currentFragment != null && currentFragment.getClass().equals(newFragment.getClass()))*/)
        {
            return null;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, newFragment, null);
        if(commit)
            ft.commit();

        return ft;

    }

    private void selectBestiary(int position) {
        Log.d(TAG,"Setting bestiary to position: " + position);
        selectedMonster = -1;
        selectedBestiary = position;
        bestiaries.setSelectedBestiary(position);
        //selectedMonster = -1;
        setFragment();
        if(monsterListFragment != null)
            monsterListFragment.setMonsterList(bestiaries.selectedBestiary.monsters);
        //setMonsterToView(bestiaries.selectedBestiary.monsters.get(0));
    }

    private void setMonsterToView(Monster monster) {

        if(monster == null) {
            Log.d(TAG,"Monster to show is null");
            return;
        }

        selectedMonster = monster.id;
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (!(currentFragment instanceof MainActivityFragment)) {
            Log.d(TAG,"Showing monster fragment");
            if(monsterFragment == null)
                monsterFragment = new MainActivityFragment();

            FragmentTransaction ft = replaceFragment(monsterFragment, false);
            if(ft != null) {
                ft.addToBackStack(null);
                ft.commit();
            }
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

    @Override
    public void onBestiaryChange() {
        if(spinnerDataAdapter != null)
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    spinnerDataAdapter.notifyDataSetChanged();
                }
            });

        if(bestiaries != null && bestiaries.selectedBestiary == null) {
            selectBestiary(0);
        } else {
            if(monsterListFragment != null)
                monsterListFragment.setMonsterList(null);
        }

    }
}