package sk.libco.bestiaryfive.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import sk.libco.bestiaryfive.Bestiaries;
import sk.libco.bestiaryfive.R;
import sk.libco.bestiaryfive.ui.adapter.BestiaryAdapter;
import sk.libco.bestiaryfive.ui.adapter.MonsterListRecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BestiaryImportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class BestiaryImportFragment extends Fragment {

    private static final String TAG = "BestiaryImportFragment";

    BestiaryAdapter recyclerViewAdapter;

    private Bestiaries bestiaries;
    //private OnFragmentInteractionListener mListener;

    public BestiaryImportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bestiary_import, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.bestiariesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        if(this.bestiaries != null) {
            recyclerViewAdapter = new BestiaryAdapter(bestiaries);
            recyclerView.setAdapter(recyclerViewAdapter);
        }

        FloatingActionButton importButton = (FloatingActionButton) view.findViewById(R.id.button_import_bestiary);
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "add new bestiary");
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                startActivityForResult(intent, 42);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

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

    }

    public void setBestiaries(Bestiaries bestiaries) {
        this.bestiaries = bestiaries;

        if(this.getView() != null) {
            RecyclerView recyclerView = (RecyclerView) this.getView().findViewById(R.id.bestiariesList);
            recyclerViewAdapter = new BestiaryAdapter(bestiaries);
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
      /*  if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
   /* public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/

    private class ParseTask extends AsyncTask<Uri, Integer, Integer> {
        protected Integer doInBackground(Uri... uris) {

            int monstersParsed = 0;

            for (Uri uri : uris) {
                monstersParsed += bestiaries.importBestiary(uri);
            }

            return monstersParsed;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Integer result) {

            if(bestiaries.selectedBestiary != null && result > 0) {
                try {
                    Spinner spinner = (Spinner) getView().findViewById(R.id.spinner_nav);
                    ArrayAdapter<String> spinnerDataAdapter = (ArrayAdapter<String>) spinner.getAdapter();

                    spinnerDataAdapter.notifyDataSetChanged();
                    spinner.setSelection(spinnerDataAdapter.getPosition(bestiaries.selectedBestiary.name));

                    //if(bestiaries.selectedBestiary.monsters.size()>0)
                    //    setMonsterToView(bestiaries.selectedBestiary.monsters.get(0));

                    //Snackbar snackbar = Snackbar
                    //        .make(getView().findViewById(R.id.activity_main), "Loaded " + result + " monsters.", Snackbar.LENGTH_LONG);

                    //snackbar.show();
                } catch (Exception e) {
                    Log.e(TAG,e.toString());
                }
            }

            //Log.d(TAG,"ParsingTask onPostExecute(): setting Fragment.");
            //setFragment();

        }
    }

}
