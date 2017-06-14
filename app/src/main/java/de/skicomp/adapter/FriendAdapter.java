package de.skicomp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import de.skicomp.R;
import de.skicomp.SessionManager;
import de.skicomp.activities.BaseActivity;
import de.skicomp.enums.FriendshipStatus;
import de.skicomp.models.Friend;
import de.skicomp.network.SkiService;
import de.skicomp.utils.helper.FriendshipStatusHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by benjamin.schneider on 12.05.17.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder>{

    private final Context context;
    private final BaseActivity baseActivity;
    private final List<Friend> friendList;

    public FriendAdapter(Context context, BaseActivity baseActivity, List<Friend> friendList) {
        this.context = context;
        this.baseActivity = baseActivity;
        this.friendList = friendList;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(context).inflate(R.layout.view_friend, null);
        FriendViewHolder friendViewHolder = new FriendViewHolder(layoutView);
        return friendViewHolder;
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        final Friend friend = friendList.get(position);

        holder.tvUsername.setText(friend.getUsername());
        holder.tvFirstname.setText(friend.getFirstname());
        holder.tvLastname.setText(friend.getLastname());

        configureFriendshipStatusButton(holder, friend);

        if (position == friendList.size()) {
            holder.vDivider.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    private void configureFriendshipStatusButton(final FriendViewHolder holder, final Friend friend ) {
        holder.btFriendshipStatus.setText(FriendshipStatusHelper.getFriendshipStatusText(friend.getFriendshipStatus()));
        if (friend.getFriendshipStatus() == FriendshipStatus.ACCEPTED) {
            holder.btFriendshipStatus.setEnabled(false);
        } else {
            holder.btFriendshipStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateFriendshipStatus(holder, friend);
                }
            });
        }
        holder.btFriendshipStatus.invalidate();
    }

    private void updateFriendshipStatus(final FriendViewHolder holder, final Friend friend) {
        final FriendshipStatus updatedFriendshipStatus = FriendshipStatusHelper.getNextFriendshipStatus(friend.getFriendshipStatus());
        SkiService.getInstance().updateFriendStatus(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    friend.setFriendshipStatus(updatedFriendshipStatus);
                    configureFriendshipStatusButton(holder, friend);
                } else {
                    baseActivity.handleError(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "failure", Toast.LENGTH_SHORT).show();
            }
        }, SessionManager.getInstance().getUsername(), friend.getUsername(), updatedFriendshipStatus);
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername;
        TextView tvFirstname;
        TextView tvLastname;
        Button btFriendshipStatus;
        View vDivider;

        public FriendViewHolder(View itemView) {
            super(itemView);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            tvFirstname = (TextView) itemView.findViewById(R.id.tv_firstname);
            tvLastname = (TextView) itemView.findViewById(R.id.tv_lastname);
            btFriendshipStatus = (Button) itemView.findViewById(R.id.bt_friendship_status);
            vDivider = itemView.findViewById(R.id.v_divider);
        }
    }
}


