package sk.libco.bestiaryfive.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import sk.libco.bestiaryfive.Monster;
import sk.libco.bestiaryfive.R;
import sk.libco.bestiaryfive.databinding.FragmentMonsterlistListBinding;
import sk.libco.bestiaryfive.ui.adapter.MonsterListAdapter;
import sk.libco.bestiaryfive.ui.models.MonsterViewModel;

public class MonsterListFragment extends Fragment implements MonsterListAdapter.Listener {

    private static final String TAG = "MonsterListFragment";

    private OnListFragmentInteractionListener mListener;

    private List<MonsterViewModel> monsterList = new ArrayList<>();
    private MonsterListAdapter mAdapter;
    private LayoutInflater mInflater;

    private FragmentMonsterlistListBinding mBinding;

    // Save state
    private Parcelable recyclerViewState;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MonsterListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_monsterlist_list, container, false);
        mInflater = inflater;
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_monsterlist_list, container, false);//DataBindingUtil.setContentView(getActivity(), R.layout.fragment_monsterlist_list);

        mAdapter = new MonsterListAdapter(getContext(), inflater, monsterList, this);

        mBinding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.list.setAdapter(mAdapter);
        mBinding.list.setNestedScrollingEnabled(false);

        loadSharedProperties();

        //
        setSortIcon();
        mBinding.sortBestiary.setOnClickListener(v -> {
            mAdapter.sortBy(null);
            setSortIcon();
        });

        //
        // Set cut corner background for API 23+
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            view.findViewById(R.id.monster_list_scroll_view).setBackground(getContext().getDrawable(R.drawable.shr_product_grid_background_shape));
//        }
        //


        return mBinding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.d(TAG, "onAtack monsterlistfragment");
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
            setMonsterList(mListener.getMonsterList());
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setMonsterList(List<Monster> newMonsterList) {

        if(newMonsterList == null) {
            monsterList.clear();
        } else {
            monsterList.clear();
            for (Monster m : newMonsterList) {
                MonsterViewModel monsterViewModel = new MonsterViewModel(m);
                monsterList.add(monsterViewModel);
            }
        }
        //Log.d(TAG,"setMonsterList(); size: " + monsterList.size());

        if (mAdapter != null) {
            mAdapter = new MonsterListAdapter(this.getContext(), mInflater, monsterList, this);
            mBinding.list.setAdapter(mAdapter);
            loadSharedProperties();
        }
    }

    public boolean changeFilterType(String f, boolean add) {

        if (mAdapter != null) {
            return mAdapter.changeFilterByType(f, add);
        }

        return false;
    }

    public List<String> getCurrentFilterType() {
        return null;
    }

    private void loadSharedProperties() {
        try {
            Activity activity = getActivity();
            if (activity != null) {
                SharedPreferences settings = activity.getSharedPreferences("mm", Activity.MODE_PRIVATE);
                String sortBy = settings.getString("msortby", "NAME");
                mAdapter.sortBy(MonsterListAdapter.SortedBy.valueOf(sortBy));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void search(String string) {
        if (mAdapter != null) {
            mAdapter.getFilter().filter(string);
        }
    }

    @Override
    public void onMonsterViewModelClicked(MonsterViewModel model) {
        mListener.onMonsterSelected(model.m);
        recyclerViewState = mBinding.list.getLayoutManager().onSaveInstanceState();
    }

    public interface OnListFragmentInteractionListener {
        void onMonsterSelected(Monster monster);
        List<Monster> getMonsterList();
    }

    //
    private void setSortIcon() {
        if (mAdapter != null) {
            if (mAdapter.getSortedBy() == MonsterListAdapter.SortedBy.CR) {
                mBinding.sortBestiary.setImageResource(R.drawable.ic_sort_az);
            } else {
                mBinding.sortBestiary.setImageResource(R.drawable.ic_sort_cr);
            }
        }
    }

}
