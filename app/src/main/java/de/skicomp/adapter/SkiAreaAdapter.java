package de.skicomp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import de.skicomp.R;
import de.skicomp.models.SkiArea;
import de.skicomp.views.fastscroll.FastScrollRecyclerView;

/**
 * Created by benjamin.schneider on 14.06.17.
 */

public class SkiAreaAdapter extends RecyclerView.Adapter<SkiAreaAdapter.SkiAreaViewHolder> implements FastScrollRecyclerView.SectionedAdapter {

    private Context context;
    private SkiAreaAdapterListener listener;
    private List<SkiArea> skiAreaList;

    @NonNull
    @Override
    public String getSectionName(int position) {
        return String.valueOf(skiAreaList.get(position).getName().charAt(0));
    }

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

        if (position == 0) {
            holder.rlSkiAreaBackground.setBackgroundResource(R.drawable.background_light_gray_rounded_corners_top);
            holder.vDivider.setVisibility(View.VISIBLE);
        } else if (position == skiAreaList.size() - 1) {
            holder.rlSkiAreaBackground.setBackgroundResource(R.drawable.background_light_gray_rounded_corners_bottom);
            holder.vDivider.setVisibility(View.GONE);
        } else {
            holder.rlSkiAreaBackground.setBackgroundResource(R.drawable.background_light_gray);
            holder.vDivider.setVisibility(View.VISIBLE);
        }

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

        RelativeLayout rlSkiAreaBackground;
        TextView tvSkiAreaName;
        TextView tvSkiAreaState;
        View vDivider;

        SkiAreaViewHolder(View itemView) {
            super(itemView);
            rlSkiAreaBackground = (RelativeLayout) itemView.findViewById(R.id.rl_skiarea);
            tvSkiAreaName = (TextView) itemView.findViewById(R.id.tv_skiarea_name);
            tvSkiAreaState = (TextView) itemView.findViewById(R.id.tv_skiarea_state);
            vDivider = itemView.findViewById(R.id.v_divider);
        }
    }

}

