package de.skicomp.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import de.skicomp.R;
import de.skicomp.data.manager.SkiAreaManager;
import de.skicomp.events.UpdatedFavoriteSkiAreasEvent;
import de.skicomp.models.SkiArea;
import de.skicomp.utils.Utils;
import de.skicomp.utils.helper.SkiAreaHelper;

/**
 * Created by benjamin.schneider on 14.06.17.
 */

public class SkiAreaAdapter extends RecyclerView.Adapter<SkiAreaAdapter.SkiAreaViewHolder> {

    private Context context;
    private SkiAreaAdapterListener listener;
    private List<SkiArea> skiAreaList;
    private RecyclerView recyclerView;

    public interface SkiAreaAdapterListener {
        void onSkiAreaSelected(SkiArea skiArea);
    }

    public SkiAreaAdapter(Context context, SkiAreaAdapterListener listener, List<SkiArea> skiAreaList) {
        this.context = context;
        this.listener = listener;
        this.skiAreaList = skiAreaList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public SkiAreaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(context).inflate(R.layout.view_skiarea, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        return new SkiAreaViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(final SkiAreaViewHolder holder, final int position) {
        final SkiArea skiArea = skiAreaList.get(position);

        holder.tvSkiAreaName.setText(skiArea.getName());
        holder.tvSkiAreaState.setText(skiArea.getState());

        String previousFirstLetter = position != 0 ? skiAreaList.get(position - 1).getName().substring(0, 1) : null;
        String firstLetter = skiAreaList.get(position).getName().substring(0, 1);
        String nextFirstLetter = position != skiAreaList.size() - 1 ? skiAreaList.get(position + 1).getName().substring(0, 1) : null;

        if (!firstLetter.equalsIgnoreCase(previousFirstLetter)) {
            holder.tvHeader.setText(firstLetter);
            holder.tvHeader.setVisibility(View.VISIBLE);
        } else {
            holder.tvHeader.setVisibility(View.GONE);
        }

        if (!firstLetter.equalsIgnoreCase(nextFirstLetter)) {
            holder.rlSkiAreaBackground.setBackgroundResource(R.drawable.background_light_gray_rounded_corners_bottom);
            holder.vDivider.getLayoutParams().height = (int) context.getResources().getDimension(R.dimen.default_padding);
        } else {
            holder.rlSkiAreaBackground.setBackgroundResource(R.drawable.background_light_gray);
            int pxHeight = Utils.dpToPx(context, 1);
            holder.vDivider.getLayoutParams().height = pxHeight;
        }

        holder.ivFavorite.setImageResource(SkiAreaHelper.favoritesContainsSkiArea(skiArea) ? R.drawable.ic_favorite_selected_accent : R.drawable.ic_favorite_accent);
        holder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SkiAreaHelper.favoritesContainsSkiArea(skiArea)) {
                    SkiAreaManager.getInstance().addSkiAreaToFavorites(skiArea);
                    holder.ivFavorite.setImageResource(R.drawable.ic_favorite_selected);
                    Utils.showSnackbar(recyclerView, R.string.favorites_added, Snackbar.LENGTH_SHORT);

                } else {
                    SkiAreaManager.getInstance().removeSkiAreaFromFavorites(skiArea);
                    holder.ivFavorite.setImageResource(R.drawable.ic_favorite);
                    Utils.showSnackbar(recyclerView, R.string.favorites_removed, Snackbar.LENGTH_SHORT);
                }

                holder.ivFavorite.invalidate();
                EventBus.getDefault().post(new UpdatedFavoriteSkiAreasEvent());
            }
        });

        holder.llSkiAreaInfo.setOnClickListener(new View.OnClickListener() {
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

        TextView tvHeader;
        RelativeLayout rlSkiAreaBackground;
        LinearLayout llSkiAreaInfo;
        TextView tvSkiAreaName;
        TextView tvSkiAreaState;
        View vDivider;
        ImageView ivFavorite;

        SkiAreaViewHolder(View itemView) {
            super(itemView);
            tvHeader = (TextView) itemView.findViewById(R.id.tv_header);
            rlSkiAreaBackground = (RelativeLayout) itemView.findViewById(R.id.rl_skiarea);
            llSkiAreaInfo = (LinearLayout) itemView.findViewById(R.id.ll_skiarea_info);
            tvSkiAreaName = (TextView) itemView.findViewById(R.id.tv_skiarea_name);
            tvSkiAreaState = (TextView) itemView.findViewById(R.id.tv_skiarea_state);
            vDivider = itemView.findViewById(R.id.v_divider);
            ivFavorite = (ImageView) itemView.findViewById(R.id.iv_favorite);
        }
    }

}

