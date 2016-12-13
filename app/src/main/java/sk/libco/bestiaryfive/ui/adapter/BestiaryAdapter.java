package sk.libco.bestiaryfive.ui.adapter;


import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import sk.libco.bestiaryfive.Bestiaries;
import sk.libco.bestiaryfive.Bestiary;
import sk.libco.bestiaryfive.Monster;
import sk.libco.bestiaryfive.R;
import sk.libco.bestiaryfive.ui.BestiaryImportFragment;
import sk.libco.bestiaryfive.ui.MonsterListFragment;

import java.util.List;

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

        final Bestiary b = bestiaries.getBestiaries().get(position);

        //holder.mItem = b;
        holder.mNameView.setText(b.name);
        //holder.mContentView.setText(m.type);
        //holder.mCrView.setText(m.cr + " cr");

        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             bestiaries.deleteBestiary(b.id);
             notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
       return bestiaries.getBestiariesCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final ImageButton mDeleteButton;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name);
            mDeleteButton = (ImageButton) view.findViewById(R.id.imageButton_bestiary_delete);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
