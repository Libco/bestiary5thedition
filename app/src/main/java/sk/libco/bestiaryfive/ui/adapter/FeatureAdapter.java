package sk.libco.bestiaryfive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sk.libco.bestiaryfive.Monster;
import sk.libco.bestiaryfive.R;

public class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.ViewHolder> {
    private List<Monster.Trait> traitList;

    private int type;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public FeatureAdapter(List<Monster.Trait> traitList, int type) {
        this.traitList = traitList;
        this.type = type;
    }

    public void setNewTraitList(List<Monster.Trait> traitList) {
        this.traitList = traitList;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(type, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder customViewHolder, int i) {

       // TextView featureLabel = (TextView) customViewHolder.view.findViewById(R.id.textViewLabelFeature);
       // featureLabel.setText(traitList.get(i).name);

        TextView feature = (TextView) customViewHolder.view.findViewById(R.id.textViewFeature);

        Monster.Trait trait = traitList.get(i);

        String textString = "<b>" + trait.name + "</b> ";
        //if(trait.attack != null)
         //   textString += "<i>" + trait.attack + "</i> ";

        int n = 0;
        for (String text: trait.text) {
            if(n > 0)
                textString += "<br>";
            textString += text;
            n++;
        }

        feature.setText(Html.fromHtml(textString));
    }

    @Override
    public int getItemCount() {
        return (null != traitList ? traitList.size() : 0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}