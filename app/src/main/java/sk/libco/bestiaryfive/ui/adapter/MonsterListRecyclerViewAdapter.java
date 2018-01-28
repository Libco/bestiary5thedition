package sk.libco.bestiaryfive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sk.libco.bestiaryfive.Monster;
import sk.libco.bestiaryfive.R;
import sk.libco.bestiaryfive.ui.MonsterListFragment;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Monster} and makes a call to the
 * specified {@link sk.libco.bestiaryfive.ui.MonsterListFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MonsterListRecyclerViewAdapter extends RecyclerView.Adapter<MonsterListRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "MonsterListAdapter";

    private final List<Monster> mValues;
    private final MonsterListFragment.OnListFragmentInteractionListener mListener;

    public MonsterListRecyclerViewAdapter(List<Monster> items, MonsterListFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_monsterlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Monster m = mValues.get(position);

        holder.mItem = m;
        holder.mIdView.setText(m.name);
        holder.mContentView.setText(m.type);
        holder.mCrView.setText(m.cr + " cr");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onMonsterSelected(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mValues == null)
            return 0;
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mIdView;
        final TextView mContentView;
        final TextView mCrView;
        Monster mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.id);
            mContentView = view.findViewById(R.id.content);
            mCrView = view.findViewById(R.id.textViewCR);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
