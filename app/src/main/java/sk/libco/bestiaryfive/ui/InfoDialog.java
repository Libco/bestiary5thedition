package sk.libco.bestiaryfive.ui;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sk.libco.bestiaryfive.R;

public class InfoDialog extends DialogFragment {

    public static InfoDialog newInstance(int myIndex) {
        InfoDialog yourDialogFragment = new InfoDialog();
        return yourDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_info, null);

        TextView textViewInfo = (TextView) view.findViewById(R.id.dialogTextViewInfo);
        textViewInfo.setMovementMethod(LinkMovementMethod.getInstance());

        return view;
    }
}