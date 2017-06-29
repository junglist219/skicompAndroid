package de.skicomp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import de.skicomp.R;
import de.skicomp.models.Friend;

/**
 * Created by benjamin.schneider on 28.06.17.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private final Context context;
    private final FriendListener listener;
    private final List<Friend> friendList;

    public interface FriendListener {
        void onSelectedFriend(Friend friend);
    }

    public FriendAdapter(Context context, FriendListener listener, List<Friend> friendList) {
        this.context = context;
        this.listener = listener;
        this.friendList = friendList;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(context).inflate(R.layout.view_friend, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        return new FriendAdapter.FriendViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        final Friend friend = friendList.get(position);

        holder.tvUsername.setText(friend.getUsername());
        holder.tvName.setText(friend.getFirstname().concat(", ").concat(friend.getLastname()));

        if (position == friendList.size() - 1) {
            holder.rlFriend.setBackgroundResource(R.drawable.background_light_gray_rounded_corners_bottom);
        } else {
            holder.rlFriend.setBackgroundResource(R.drawable.background_light_gray);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelectedFriend(friend);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlFriend;
        TextView tvUsername;
        TextView tvName;
        TextView btAction;
        View vDivider;

        FriendViewHolder(View itemView) {
            super(itemView);
            rlFriend = (RelativeLayout) itemView.findViewById(R.id.rl_friend);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_friend_username);
            tvName = (TextView) itemView.findViewById(R.id.tv_friend_name);
            btAction = (TextView) itemView.findViewById(R.id.bt_action);
            vDivider = itemView.findViewById(R.id.v_divider);
        }

    }
}
