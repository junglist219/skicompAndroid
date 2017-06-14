package de.skicomp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.skicomp.R;
import de.skicomp.models.SkiArea;

/**
 * Created by benjamin.schneider on 14.06.17.
 */

public class SkiAreaAdapter extends RecyclerView.Adapter<SkiAreaAdapter.SkiAreaViewHolder> {

    private Context context;
    private SkiAreaAdapterListener listener;
    private List<SkiArea> skiAreaList;

    public interface SkiAreaAdapterListener {
        void onSkiAreaSelected(SkiArea skiArea);
    }

    public SkiAreaAdapter(Context context, SkiAreaAdapterListener listener, List<SkiArea> skiAreaList) {
        this.context = context;
        this.listener = listener;
        this.skiAreaList = skiAreaList;
    }

    @Override
    public SkiAreaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(context).inflate(R.layout.view_skiarea, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        SkiAreaViewHolder skiAreaViewHolder = new SkiAreaViewHolder(layoutView);
        return skiAreaViewHolder;
    }

    @Override
    public void onBindViewHolder(SkiAreaViewHolder holder, int position) {
        final SkiArea skiArea = skiAreaList.get(position);

        holder.tvSkiAreaName.setText(skiArea.getName());
        holder.tvSkiAreaState.setText(skiArea.getState());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSkiAreaSelected(skiArea);
            }
        });
    }

    @Override
    public int getItemCount() {
        return skiAreaList.size();
    }

    class SkiAreaViewHolder extends RecyclerView.ViewHolder {

        TextView tvSkiAreaName;
        TextView tvSkiAreaState;

        SkiAreaViewHolder(View itemView) {
            super(itemView);
            tvSkiAreaName = (TextView) itemView.findViewById(R.id.tv_skiarea_name);
            tvSkiAreaState = (TextView) itemView.findViewById(R.id.tv_skiarea_state);
        }
    }

}

