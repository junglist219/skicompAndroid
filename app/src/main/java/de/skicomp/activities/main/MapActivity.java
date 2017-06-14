package de.skicomp.activities.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import de.skicomp.R;
import de.skicomp.activities.BottomNavigationActivity;
import de.skicomp.databinding.ActivityMapBinding;

/**
 * Created by benjamin.schneider on 14.06.17.
 */

public class MapActivity extends BottomNavigationActivity {

    private ActivityMapBinding viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_map);
        viewBinding.setHandler(this);
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.rl_menu_map;
    }

    @Override
    public void setSelectedMenuItem() {
        viewBinding.navigation.btMenuMapSelected.setVisibility(View.VISIBLE);
        viewBinding.navigation.btMenuMap.setImageResource(R.drawable.ic_menu_map_selected);
    }

}
