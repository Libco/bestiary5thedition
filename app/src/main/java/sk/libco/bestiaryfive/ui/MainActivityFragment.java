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

import sk.libco.bestiaryfive.Monster;
import sk.libco.bestiaryfive.R;
import sk.libco.bestiaryfive.ui.adapter.FeatureAdapter;

/**
 * Fragment for showing a single monster
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG = "MainActivityFragment";
    List<Monster.Trait> features = new ArrayList<>();
    List<Monster.Trait> traits = new ArrayList<>();
    List<Monster.Trait> actions = new ArrayList<>();
    List<Monster.Trait> legendaryActions = new ArrayList<>();
    private LinearLayout linearLayoutLegendaryActions;
    private LinearLayout linearLayoutReactions;
    private LinearLayout linearLayoutDescription;
    //recyclerviews
    private FeatureAdapter mAdapterFeatures;
    private FeatureAdapter mAdapterFeats;
    private FeatureAdapter mAdapterActions;
    private FeatureAdapter mAdapterLegendaryActions;
    private FeatureAdapter mAdapterReactions;
    //
    private Monster selectedMonster = null;
    private View view = null;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);


        //Features Recycler view
        RecyclerView mRecyclerViewFeatures;
        RecyclerView.LayoutManager mLayoutManagerFeatures;

        mRecyclerViewFeatures = view.findViewById(R.id.recyclerViewFeatures);

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

        //Feats Recycler view
        RecyclerView mRecyclerViewFeats;
        RecyclerView.LayoutManager mLayoutManagerFeats;

        mRecyclerViewFeats = view.findViewById(R.id.recyclerViewFeats);

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


        //Actions Recycler view
        RecyclerView mRecyclerViewActions;
        RecyclerView.LayoutManager mLayoutManagerActions;

        mRecyclerViewActions = view.findViewById(R.id.recyclerViewActions);

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


        //Reactions Recycler view
        RecyclerView mRecyclerViewReactions;
        RecyclerView.LayoutManager mLayoutManagerReactions;

        mRecyclerViewReactions = view.findViewById(R.id.recyclerViewReactions);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerViewReactions.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManagerReactions = new LinearLayoutManager(getContext());
        mRecyclerViewReactions.setNestedScrollingEnabled(false);
        mRecyclerViewReactions.setLayoutManager(mLayoutManagerReactions);

        // specify an adapter (see also next example)
        mAdapterReactions = new FeatureAdapter(null, R.layout.trait_adapter_view);
        mRecyclerViewReactions.setAdapter(mAdapterReactions);


        //Legendary Actions Recycler view
        RecyclerView mRecyclerViewLegendaryActions;
        RecyclerView.LayoutManager mLayoutManagerLegendaryActions;

        mRecyclerViewLegendaryActions = view.findViewById(R.id.recyclerViewLegendaryActions);

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

        //
        linearLayoutLegendaryActions = view.findViewById(R.id.linearViewLegendary);
        linearLayoutReactions = view.findViewById(R.id.linearViewReactions);
        linearLayoutDescription = view.findViewById(R.id.linearViewDescription);

        //
        Log.d(TAG,"onCreateView initialized");
        setMonsterToView(selectedMonster);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }

    public void setMonsterToView(Monster monster) {

        if(monster == null) {
            Log.d(TAG,"Monster is null - cannot set");
            return;
        }

        if(view == null) {
            selectedMonster = monster;
            Log.d(TAG,"UI is not initialized, saving " + monster.name + " to show later");
            return;
        }

        Log.d(TAG,"setting monster to: " + monster.name);

        setTextView(R.id.textViewName, monster.name);
        setTextView(R.id.textViewType, monster.getTypeString());
        setTextView(R.id.textViewAC, monster.ac);
        setTextView(R.id.textViewHP, monster.hp);
        setTextView(R.id.textViewSpeed, monster.speed);
        //stats
        setTextView(R.id.textViewSTR, Monster.getAbilityWithBonus(monster.str));
        setTextView(R.id.textViewDEX, Monster.getAbilityWithBonus(monster.dex));
        setTextView(R.id.textViewCON, Monster.getAbilityWithBonus(monster.con));
        setTextView(R.id.textViewINT, Monster.getAbilityWithBonus(monster.inteligence));
        setTextView(R.id.textViewWIS, Monster.getAbilityWithBonus(monster.wis));
        setTextView(R.id.textViewCHA, Monster.getAbilityWithBonus(monster.cha));
        //immunities,languages, etc...
        mAdapterFeatures.setNewTraitList(monster.getFeatures());

        //feats
        mAdapterFeats.setNewTraitList(monster.traits);

        //actions
        mAdapterActions.setNewTraitList(monster.actions);

        //reactions
        setNewTraitListIfVisible(monster.reactions ,mAdapterReactions, linearLayoutReactions);

        //legendary actions
        setNewTraitListIfVisible(monster.legendaryActions, mAdapterLegendaryActions, linearLayoutLegendaryActions);

        //description
        setTextViewIfVisible(R.id.textViewDescription,monster.description, linearLayoutDescription);

    }

    private void setNewTraitListIfVisible(List<Monster.Trait> items, FeatureAdapter featureAdapter, LinearLayout itemView) {
        if(items.size() > 0) {
            featureAdapter.setNewTraitList(items);
            itemView.setVisibility(View.VISIBLE);
        } else {
            itemView.setVisibility(View.GONE);
        }
    }

    private void setTextView(int textView, String text) {
        if(view != null) {
            TextView textViewType = view.findViewById(textView);
            textViewType.setText(text);
        }
    }

    private void setTextViewIfVisible(int textView, String text, LinearLayout itemView) {
        if(view != null && text != null && !text.isEmpty()) {
            TextView textViewType = view.findViewById(textView);
            textViewType.setText(text);
            itemView.setVisibility(View.VISIBLE);
        } else {
            itemView.setVisibility(View.GONE);
        }
    }

}
