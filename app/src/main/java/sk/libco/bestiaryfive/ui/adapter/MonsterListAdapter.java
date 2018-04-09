package sk.libco.bestiaryfive.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sk.libco.bestiaryfive.databinding.FragmentMonsterlistBinding;
import sk.libco.bestiaryfive.ui.adapter.viewholder.MonsterViewHolder;
import sk.libco.bestiaryfive.ui.models.MonsterViewModel;

public class MonsterListAdapter extends RecyclerView.Adapter<MonsterViewHolder> implements Filterable {

    private final Listener mListener;
    private final LayoutInflater _inflater;
    protected List<MonsterViewModel> list;
    protected Context context;
    private List<MonsterViewModel> originalList;
    private SortedBy sortedBy = SortedBy.NAME;

    public MonsterListAdapter(Context context,
                              LayoutInflater inflater,
                              List<MonsterViewModel> list,
                              Listener listener) {
        this.originalList = list;
        this.list = list;
        this.context = context;
        mListener = listener;
        _inflater = inflater;
        this.setHasStableIds(true);
    }

    @NonNull
    @Override
    public MonsterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final FragmentMonsterlistBinding binding = FragmentMonsterlistBinding.inflate(_inflater, parent, false);
        return new MonsterViewHolder(binding, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MonsterViewHolder holder, int position) {
        final MonsterViewModel model = list.get(position);
        holder.performBind(model);
    }

    @Override
    public long getItemId(int position) {
        final MonsterViewModel model = list.get(position);
        return model.m.id;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public SortedBy getSortedBy() {
        return sortedBy;
    }

    public void sortBy(SortedBy rsort) {
        //this.list.(MonsterViewModel.Comparators.CR);

        SortedBy sortTo = SortedBy.NAME;

        if (rsort != null) {
            sortTo = rsort;
        } else {
            if (sortedBy == SortedBy.NAME) {
                sortTo = SortedBy.CR;

            } else if (sortedBy == SortedBy.CR) {
                sortTo = SortedBy.NAME;

            }
        }

        //save to sharedpreferences
        SharedPreferences settings = context.getSharedPreferences("mm", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("msortby", sortTo.name());
        editor.apply();

        switch (sortTo) {
            case NAME:
                Collections.sort(list, MonsterViewModel.Comparators.NAME);
                sortedBy = SortedBy.NAME;
                break;
            case CR:
                Collections.sort(list, MonsterViewModel.Comparators.CR);
                sortedBy = SortedBy.CR;
                break;
        }

        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<MonsterViewModel>) results.values;
                MonsterListAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<MonsterViewModel> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = originalList;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    private List<MonsterViewModel> getFilteredResults(String constraint) {
        List<MonsterViewModel> results = new ArrayList<>();

        for (MonsterViewModel item : originalList) {
            if (item.m.name.toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }

    public enum SortedBy {
        NAME,
        CR
    }

    public interface Listener {
        void onMonsterViewModelClicked(MonsterViewModel model);
    }

}