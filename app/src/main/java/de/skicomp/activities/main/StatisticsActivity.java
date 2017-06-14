package de.skicomp.activities.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import de.skicomp.R;
import de.skicomp.activities.BottomNavigationActivity;
import de.skicomp.databinding.ActivityStatisticsBinding;

/**
 * Created by benjamin.schneider on 14.06.17.
 */

public class StatisticsActivity extends BottomNavigationActivity {

    private ActivityStatisticsBinding viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_statistics);
        viewBinding.setHandler(this);
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.rl_menu_statistics;
    }

    @Override
    public void setSelectedMenuItem() {
        viewBinding.navigation.btMenuStatisticsSelected.setVisibility(View.VISIBLE);
        viewBinding.navigation.btMenuStatistics.setImageResource(R.drawable.ic_menu_statistics_selected);
    }

}
