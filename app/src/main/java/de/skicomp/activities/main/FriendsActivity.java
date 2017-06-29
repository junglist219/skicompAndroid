package de.skicomp.activities.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import de.skicomp.R;
import de.skicomp.activities.BottomNavigationActivity;
import de.skicomp.adapter.FriendAdapter;
import de.skicomp.data.manager.FriendManager;
import de.skicomp.databinding.ActivityFriendsBinding;
import de.skicomp.events.friend.FriendEvent;
import de.skicomp.events.friend.FriendEventFailure;
import de.skicomp.events.friend.FriendEventSuccess;
import de.skicomp.fragments.friends.FriendFragment;
import de.skicomp.fragments.friends.SearchFriendFragment;
import de.skicomp.models.Friend;
import de.skicomp.utils.ProgressDialogManager;
import de.skicomp.utils.helper.FriendHelper;
import retrofit2.Response;

/**
 * Created by benjamin.schneider on 22.06.17.
 */

public class FriendsActivity extends BottomNavigationActivity implements FriendAdapter.FriendListener {

    private ActivityFriendsBinding viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_friends);
        viewBinding.setHandler(this);
        viewBinding.setFriendHandler(this);

        List<Friend> friendList = FriendManager.getInstance().getFriends();
        if (friendList == null) {
//            ProgressDialogManager.getInstance().startProgressDialog(getApplicationContext());
            FriendManager.getInstance().loadFriends();
        } else {
            showFriends();
            FriendManager.getInstance().loadFriends();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void showFriends() {
        List<Friend> friends = FriendHelper.getFriends();
        List<Friend> friendsRequesting = FriendHelper.getFriendsRequesting();
        List<Friend> friendsRequested = FriendHelper.getFriendsRequested();

        initFriends(friends);
        initFriendsRequesting(friendsRequesting);
        initFriendsRequested(friendsRequested);
    }

    private void initFriendsRequesting(List<Friend> friendsRequesting) {
        if (friendsRequesting == null || friendsRequesting.isEmpty()) {
            viewBinding.rlFriendsRequesting.setVisibility(View.GONE);
            return;
        }

        FriendAdapter friendsRequestingAdapter = new FriendAdapter(getApplicationContext(), this, friendsRequesting);
        viewBinding.rvFriendsRequesting.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        viewBinding.rvFriendsRequesting.setAdapter(friendsRequestingAdapter);
        viewBinding.rlFriendsRequesting.setVisibility(View.VISIBLE);
    }

    private void initFriends(List<Friend> friends) {
        if (friends == null || friends.isEmpty()) {
            viewBinding.rvFriends.setVisibility(View.GONE);
            viewBinding.llNoFriends.setVisibility(View.VISIBLE);
            return;
        }

        FriendAdapter friendAdapter = new FriendAdapter(getApplicationContext(), this, friends);
        viewBinding.rvFriends.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        viewBinding.rvFriends.setAdapter(friendAdapter);
        viewBinding.rvFriends.setVisibility(View.VISIBLE);
        viewBinding.llNoFriends.setVisibility(View.GONE);
    }

    private void initFriendsRequested(List<Friend> friendsRequested) {
        if (friendsRequested == null || friendsRequested.isEmpty()) {
            viewBinding.rlFriendsRequested.setVisibility(View.GONE);
            return;
        }

        FriendAdapter friendsRequestedAdapter = new FriendAdapter(getApplicationContext(), this, friendsRequested);
        viewBinding.rvFriendsRequested.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        viewBinding.rvFriendsRequested.setAdapter(friendsRequestedAdapter);
        viewBinding.rlFriendsRequested.setVisibility(View.VISIBLE);
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.rl_menu_friends;
    }

    @Override
    public void setSelectedMenuItem() {
        viewBinding.navigation.btMenuFriendsSelected.setVisibility(View.VISIBLE);
        viewBinding.navigation.btMenuFriends.setImageResource(R.drawable.ic_menu_friends_selected);
    }

    @SuppressWarnings("unused")
    public void onClickSearchFriends(View view) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.bottom_sheet_slide_in, R.anim.bottom_sheet_slide_out, R.anim.bottom_sheet_slide_in, R.anim.bottom_sheet_slide_out)
                .add(R.id.fl_container, new SearchFriendFragment())
                .addToBackStack(SearchFriendFragment.TAG)
                .commit();
    }

    @Override
    public void onSelectedFriend(Friend friend) {
        FriendFragment friendFragment = new FriendFragment();
        friendFragment.setFriend(friend);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.bottom_sheet_slide_in, R.anim.bottom_sheet_slide_out, R.anim.bottom_sheet_slide_in, R.anim.bottom_sheet_slide_out)
                .add(R.id.fl_container, friendFragment)
                .addToBackStack(FriendFragment.TAG)
                .commit();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onFriendEvent(FriendEvent event) {
        ProgressDialogManager.getInstance().stopProgressDialog();
        if (event instanceof FriendEventSuccess) {
            showFriends();
        } else {
            Response response = ((FriendEventFailure) event).getResponse();
            super.handleError(response);
        }
    }
}
