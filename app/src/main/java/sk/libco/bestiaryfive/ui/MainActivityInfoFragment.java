package sk.libco.bestiaryfive.ui;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import sk.libco.bestiaryfive.R;

/**
 * Fragment with info on how to download monsters
 */
public class MainActivityInfoFragment extends Fragment {

    public MainActivityInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_nothing_loaded, container, false);

        TextView textViewInfo = view.findViewById(R.id.textViewInfo);
        textViewInfo.setMovementMethod(LinkMovementMethod.getInstance());
        return view;
    }
}
