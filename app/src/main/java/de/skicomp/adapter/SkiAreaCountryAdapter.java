package de.skicomp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.skicomp.R;
import de.skicomp.enums.SkiAreaCountry;

/**
 * Created by benjamin.schneider on 14.06.17.
 */

public class SkiAreaCountryAdapter extends RecyclerView.Adapter<SkiAreaCountryAdapter.SkiAreaCountryViewHolder> {

    private Context context;
    private SkiAreaCountryAdapterListener listener;
    private List<SkiAreaCountry> skiAreaCountryList;

    public interface SkiAreaCountryAdapterListener {
        void onSkiAreaCountrySelected(String country);
    }

    public SkiAreaCountryAdapter(Context context, SkiAreaCountryAdapterListener listener, List<SkiAreaCountry> skiAreaCountryList) {
        this.context = context;
        this.listener = listener;
        this.skiAreaCountryList = skiAreaCountryList;
    }

    @Override
    public SkiAreaCountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(context).inflate(R.layout.view_skiarea_country, null);
        SkiAreaCountryAdapter.SkiAreaCountryViewHolder skiAreaCountryViewHolder = new SkiAreaCountryAdapter.SkiAreaCountryViewHolder(layoutView);
        return skiAreaCountryViewHolder;
    }

    @Override
    public void onBindViewHolder(SkiAreaCountryViewHolder holder, int position) {
        final SkiAreaCountry country = skiAreaCountryList.get(position);
        final String countryName = country.toString().equals("ÖSTERREICH") ? "oesterreich" : country.toString();

        int countryDrawableID = context.getResources().getIdentifier("image_".concat(countryName.toLowerCase()), "drawable", context.getPackageName());
        holder.ivSkiArea.setImageResource(countryDrawableID);
        holder.tvSkiAreaName.setText(country.toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSkiAreaCountrySelected(country.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return skiAreaCountryList.size();
    }

    class SkiAreaCountryViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivSkiArea;
        private TextView tvSkiAreaName;

        SkiAreaCountryViewHolder(View itemView) {
            super(itemView);
            ivSkiArea = (ImageView) itemView.findViewById(R.id.iv_skiarea);
            tvSkiAreaName = (TextView) itemView.findViewById(R.id.tv_skiarea_name);
        }
    }

}
