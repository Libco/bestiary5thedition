package sk.libco.bestiaryfive.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sk.libco.bestiaryfive.Monster;
import sk.libco.bestiaryfive.R;
import sk.libco.bestiaryfive.ui.adapter.MonsterListRecyclerViewAdapter;

public class MonsterListFragment extends Fragment {

    private static final String TAG = "MonsterListFragment";

    private OnListFragmentInteractionListener mListener;

    private List<Monster> monsterList = new ArrayList<>();
    private List<Monster> monsterListFiltered = new ArrayList<>();
    private MonsterListRecyclerViewAdapter recyclerViewAdapter;

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
        View view = inflater.inflate(R.layout.fragment_monsterlist_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewAdapter = new MonsterListRecyclerViewAdapter(monsterListFiltered, mListener);
        recyclerView.setAdapter(recyclerViewAdapter);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
            monsterListFiltered.clear();
        } else {

            if (newMonsterList != monsterList)
                copyArray(newMonsterList, monsterList);

            copyArray(monsterList, monsterListFiltered);
        }

        Log.d(TAG,"setMonsterList(); size: " + monsterListFiltered.size());

        if(recyclerViewAdapter != null) {
            Activity activity = getActivity();
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    public void search(String string) {

        if(string.isEmpty())
            setMonsterList(monsterList);

        monsterListFiltered.clear();
        for (Monster m:monsterList) {
            if(m.name.toLowerCase().contains(string.toLowerCase())) {
                monsterListFiltered.add(m);
            }
        }

        if(monsterListFiltered.size() == 1) {
            mListener.onMonsterSelected(monsterListFiltered.get(0));
            return;
        }

        if(recyclerViewAdapter != null)
            recyclerViewAdapter.notifyDataSetChanged();

    }

    private void copyArray(List from, List to) {
        to.clear();
        for(Object f:from) {
            to.add(f);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onMonsterSelected(Monster monster);

        List<Monster> getMonsterList();
    }
}
