package sk.libco.bestiaryfive.ui.adapter.viewholder;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sk.libco.bestiaryfive.databinding.FragmentMonsterlistBinding;
import sk.libco.bestiaryfive.ui.adapter.MonsterListAdapter;
import sk.libco.bestiaryfive.ui.models.MonsterViewModel;

public class MonsterViewHolder extends RecyclerView.ViewHolder {

    private final FragmentMonsterlistBinding mBinding;

    public MonsterViewHolder(FragmentMonsterlistBinding binding, MonsterListAdapter.Listener listener) {
        super(binding.getRoot());
        binding.setListener(listener);

        mBinding = binding;
    }


    public void performBind(@NonNull MonsterViewModel item) {
        mBinding.setModel(item);
    }


}