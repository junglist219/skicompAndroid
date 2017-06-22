package de.skicomp.activities.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import de.skicomp.R;
import de.skicomp.activities.BottomNavigationActivity;
import de.skicomp.databinding.ActivityFriendsBinding;

/**
 * Created by benjamin.schneider on 22.06.17.
 */

public class FriendsActivity extends BottomNavigationActivity {

    private ActivityFriendsBinding viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_friends);
        viewBinding.setHandler(this);
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
}
