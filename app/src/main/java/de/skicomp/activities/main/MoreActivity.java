package de.skicomp.activities.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import de.skicomp.R;
import de.skicomp.activities.BottomNavigationActivity;
import de.skicomp.databinding.ActivityMoreBinding;

/**
 * Created by benjamin.schneider on 14.06.17.
 */

public class MoreActivity extends BottomNavigationActivity {

    private ActivityMoreBinding viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_more);
        viewBinding.setHandler(this);
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.rl_menu_more;
    }

    @Override
    public void setSelectedMenuItem() {
        viewBinding.navigation.btMenuMoreSelected.setVisibility(View.VISIBLE);
        viewBinding.navigation.btMenuMore.setImageResource(R.drawable.ic_menu_more_selected);
    }

}
