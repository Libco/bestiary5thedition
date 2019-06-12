package sk.libco.bestiaryfive.ui.adapter;


import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import sk.libco.bestiaryfive.Bestiaries;
import sk.libco.bestiaryfive.Bestiary;
import sk.libco.bestiaryfive.R;

/**
 * {@link RecyclerView.Adapter} that can display a {@link sk.libco.bestiaryfive.Bestiary}
 */
public class BestiaryAdapter extends RecyclerView.Adapter<BestiaryAdapter.ViewHolder> {

    private static final String TAG = "BestiaryAdapter";

    private final Bestiaries bestiaries;
    //private final BestiaryImportFragment.OnFragmentInteractionListener mListener;

    public BestiaryAdapter(Bestiaries bestiaries) {
        this.bestiaries = bestiaries;

       // mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_bestiary_import_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Bestiary b = bestiaries.getBestiariesWithoutAll().get(position);

        //holder.mItem = b;
        holder.mNameView.setText(b.name);
        //holder.mContentView.setText(m.type);
        //holder.mCrView.setText(m.cr + " cr");

        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete confirmation")
                        .setMessage("Do you really want to delete " + b.name + "?")
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                bestiaries.deleteBestiary(b.id);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return bestiaries.getBestiariesWithoutAllCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mNameView;
        final ImageButton mDeleteButton;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = view.findViewById(R.id.name);
            mDeleteButton = view.findViewById(R.id.imageButton_bestiary_delete);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
