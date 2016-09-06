package sk.libco.bestiaryfive.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sk.libco.bestiaryfive.ui.adapter.FeatureAdapter;
import sk.libco.bestiaryfive.Monster;
import sk.libco.bestiaryfive.R;

/**
 * Fragment for showing a single monster
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG = "MainActivityFragment";

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

    //
    private Monster selectedMonster = null;
    private boolean initialized = false;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);


        /**
         * Features Recycler view
         */
        RecyclerView mRecyclerViewFeatures;
        RecyclerView.LayoutManager mLayoutManagerFeatures;

        mRecyclerViewFeatures = (RecyclerView) view.findViewById(R.id.recyclerViewFeatures);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerViewFeatures.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManagerFeatures  = new LinearLayoutManager(getContext()) {
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

        mRecyclerViewFeats = (RecyclerView) view.findViewById(R.id.recyclerViewFeats);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerViewFeats.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManagerFeats  = new LinearLayoutManager(getContext());
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

        mRecyclerViewActions = (RecyclerView) view.findViewById(R.id.recyclerViewActions);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerViewActions.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManagerActions = new LinearLayoutManager(getContext());
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

        mRecyclerViewLegendaryActions = (RecyclerView) view.findViewById(R.id.recyclerViewLegendaryActions);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerViewLegendaryActions.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManagerLegendaryActions = new LinearLayoutManager(getContext());
        mRecyclerViewLegendaryActions.setNestedScrollingEnabled(false);
        mRecyclerViewLegendaryActions.setLayoutManager(mLayoutManagerLegendaryActions);

        // specify an adapter (see also next example)
        mAdapterLegendaryActions = new FeatureAdapter(legendaryActions, R.layout.trait_adapter_view);
        mRecyclerViewLegendaryActions.setAdapter(mAdapterLegendaryActions);

        /****/
        linearLayoutLegendaryActions = (LinearLayout) view.findViewById(R.id.linearViewLegendary);

        /****/
        initialized = true;

        return view;
    }

    public void setMonsterToView(Monster monster) {

        if(!initialized || selectedMonster == monster)
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
        try {
            TextView textViewType = (TextView) getView().findViewById(textView);
            textViewType.setText(text);
        } catch (Exception e) {
            Log.e(TAG,e.toString());
        }
    }

}
