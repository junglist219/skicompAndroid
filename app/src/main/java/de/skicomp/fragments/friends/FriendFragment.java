package de.skicomp.fragments.friends;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import de.skicomp.R;
import de.skicomp.activities.BaseActivity;
import de.skicomp.data.manager.FriendManager;
import de.skicomp.databinding.FragmentFriendBinding;
import de.skicomp.enums.FriendshipStatus;
import de.skicomp.events.friend.FriendUpdatedEvent;
import de.skicomp.events.friend.FriendUpdatedEventFailure;
import de.skicomp.events.friend.FriendUpdatedEventSuccess;
import de.skicomp.models.Friend;
import de.skicomp.utils.helper.FriendshipStatusHelper;

/**
 * Created by benjamin.schneider on 29.06.17.
 */

public class FriendFragment extends Fragment {

    public static final String TAG = FriendFragment.class.getSimpleName();

    private FragmentFriendBinding viewBinding;

    private Friend friend;

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend, container, false);
        viewBinding.setFriend(friend);
        viewBinding.setHandler(this);

        initToolbar();

        return viewBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(viewBinding.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @SuppressWarnings("unused")
    public void onClickFriendAction(View view) {
        FriendshipStatus nextFriendshipStatus = FriendshipStatusHelper.getNextFriendshipStatus(friend.getFriendshipStatus());
        FriendManager.getInstance().updateFriendship(friend, nextFriendshipStatus);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onFriendUpdatedEvent(FriendUpdatedEvent event) {
        if (event instanceof FriendUpdatedEventSuccess) {
            viewBinding.setFriend(((FriendUpdatedEventSuccess) event).getFriend());
        } else {
            ((BaseActivity) getActivity()).handleError(((FriendUpdatedEventFailure) event).getResponse());
        }
    }
}
