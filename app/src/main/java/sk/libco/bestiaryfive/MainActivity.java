package sk.libco.bestiaryfive;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    private Bestiaries bestiaries;
    private Monster selectedMonster = null;

    Spinner spinner;
    ArrayAdapter<String> spinnerDataAdapter;
    LinearLayout linearLayoutLegendaryActions;

    //recyclerviews
    private FeatureAdapter mAdapterFeatures;
    private FeatureAdapter mAdapterFeats;
    private FeatureAdapter mAdapterActions;
    private FeatureAdapter mAdapterLegendaryActions;

    List<Monster.Trait> features = new ArrayList<>();
    List<Monster.Trait> traits = new ArrayList<>();
    List<Monster.Trait> actions = new ArrayList<>();
    List<Monster.Trait> legendaryActions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*****/
        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.activity_main_scroll_view);
        scrollView.setVisibility(View.GONE);

        TextView textViewInfo = (TextView) findViewById(R.id.textViewInfo);
        textViewInfo.setMovementMethod(LinkMovementMethod.getInstance());
        textViewInfo.setVisibility(View.VISIBLE);

        /****/
        bestiaries = new Bestiaries(this);

        /** Set toolbar **/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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

        /**
         * Features Recycler view
         */
        RecyclerView mRecyclerViewFeatures;
        RecyclerView.LayoutManager mLayoutManagerFeatures;

        mRecyclerViewFeatures = (RecyclerView) findViewById(R.id.recyclerViewFeatures);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerViewFeatures.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManagerFeatures  = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRecyclerViewFeatures.setLayoutManager(mLayoutManagerFeatures);

        // specify an adapter (see also next example)
        mAdapterFeatures = new FeatureAdapter(features, R.layout.feature_adapter_view);
        mRecyclerViewFeatures.setAdapter(mAdapterFeatures);

        /**
         * Feats Recycler view
         */
        RecyclerView mRecyclerViewFeats;
        RecyclerView.LayoutManager mLayoutManagerFeats;

        mRecyclerViewFeats = (RecyclerView) findViewById(R.id.recyclerViewFeats);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerViewFeats.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManagerFeats  = new LinearLayoutManager(this);
        mRecyclerViewFeats.setNestedScrollingEnabled(false);
        mRecyclerViewFeats.setLayoutManager(mLayoutManagerFeats);

        // specify an adapter (see also next example)
        mAdapterFeats = new FeatureAdapter(traits, R.layout.trait_adapter_view);
        mRecyclerViewFeats.setAdapter(mAdapterFeats);

        /**
         * Actions Recycler view
         */
        RecyclerView mRecyclerViewActions;
        RecyclerView.LayoutManager mLayoutManagerActions;

        mRecyclerViewActions = (RecyclerView) findViewById(R.id.recyclerViewActions);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerViewActions.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManagerActions = new LinearLayoutManager(this);
        mRecyclerViewActions.setNestedScrollingEnabled(false);
        mRecyclerViewActions.setLayoutManager(mLayoutManagerActions);

        // specify an adapter (see also next example)
        mAdapterActions = new FeatureAdapter(actions, R.layout.trait_adapter_view);
        mRecyclerViewActions.setAdapter(mAdapterActions);

        /**
         * Legendary Actions Recycler view
         */
        RecyclerView mRecyclerViewLegendaryActions;
        RecyclerView.LayoutManager mLayoutManagerLegendaryActions;

        mRecyclerViewLegendaryActions = (RecyclerView) findViewById(R.id.recyclerViewLegendaryActions);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerViewLegendaryActions.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManagerLegendaryActions = new LinearLayoutManager(this);
        mRecyclerViewLegendaryActions.setNestedScrollingEnabled(false);
        mRecyclerViewLegendaryActions.setLayoutManager(mLayoutManagerLegendaryActions);

        // specify an adapter (see also next example)
        mAdapterLegendaryActions = new FeatureAdapter(legendaryActions, R.layout.trait_adapter_view);
        mRecyclerViewLegendaryActions.setAdapter(mAdapterLegendaryActions);

        /****/
        linearLayoutLegendaryActions = (LinearLayout) findViewById(R.id.linearViewLegendary);

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
                intent.setType("text/xml");
                startActivityForResult(intent, 42);
                return true;
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

                TextView textViewInfo = (TextView) findViewById(R.id.textViewInfo);
                textViewInfo.setVisibility(View.GONE);

                NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.activity_main_scroll_view);
                scrollView.setVisibility(View.VISIBLE);

                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.activity_main), "Loaded " + result + " monsters.", Snackbar.LENGTH_LONG);

                snackbar.show();
            }

        }
    }

    private void selectBestiary(int position) {
        Log.d(TAG,"Setting bestiary to position: " + position);
        bestiaries.setSelectedBestiary(position);
        setMonsterToView(bestiaries.selectedBestiary.monsters.get(0));
    }

    private void setMonsterToView(Monster monster) {

        if(selectedMonster == monster)
            return;

        Log.d(TAG,"setting monster to: " + monster.name);

        setTextView(R.id.textViewName, monster.name);
        setTextView(R.id.textViewType, monster.getTypeString());
        setTextView(R.id.textViewAC, monster.ac);
        setTextView(R.id.textViewHP, monster.hp);
        setTextView(R.id.textViewSpeed, monster.speed);
        //stats
        setTextView(R.id.textViewSTR, monster.str);
        setTextView(R.id.textViewDEX, monster.dex);
        setTextView(R.id.textViewCON, monster.con);
        setTextView(R.id.textViewINT, monster.inteligence);
        setTextView(R.id.textViewWIS, monster.wis);
        setTextView(R.id.textViewCHA, monster.cha);
        //immunities,languages, etc...
        mAdapterFeatures.setNewTraitList(monster.getFeatures());

        //feats
        mAdapterFeats.setNewTraitList(monster.traits);

        //actions
        mAdapterActions.setNewTraitList(monster.actions);

        //legendary actions
        if(monster.legendaryActions.size() > 0) {
            mAdapterLegendaryActions.setNewTraitList(monster.legendaryActions);
            linearLayoutLegendaryActions.setVisibility(View.VISIBLE);
        } else {
            linearLayoutLegendaryActions.setVisibility(View.GONE);
        }

        selectedMonster = monster;
    }

    private void setTextView(int textView, String text) {
        TextView textViewType = (TextView) findViewById(textView);
        textViewType.setText(text);
    }


}
