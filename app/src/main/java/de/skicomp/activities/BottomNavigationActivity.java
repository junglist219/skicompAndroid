package de.skicomp.activities;

import android.content.Intent;
import android.view.View;

import de.skicomp.R;
import de.skicomp.activities.main.AccountActivity;
import de.skicomp.activities.main.MapActivity;
import de.skicomp.activities.main.MoreActivity;
import de.skicomp.activities.main.SkiAreasActivity;
import de.skicomp.activities.main.StatisticsActivity;

/**
 * Created by benjamin.schneider on 14.06.17.
 */

public abstract class BottomNavigationActivity extends BaseActivity {

    public abstract int getNavigationMenuItemId();
    public abstract void setSelectedMenuItem();

    @Override
    protected void onStart() {
        super.onStart();
        setSelectedMenuItem();
    }

    @Override
    public void onPause() {
        super.onPause();
        // remove inter-activity transition to avoid tossing on tapping bottom navigation items
        overridePendingTransition(0, 0);
    }

    @SuppressWarnings("unused")
    public void onClickMenuItem(View view) {
        if (view.getId() == getNavigationMenuItemId()) {
            return;
        }

        Intent intent = null;
        switch (view.getId()) {
            case R.id.rl_menu_profile:
                intent = new Intent(this, AccountActivity.class);
                break;
            case R.id.rl_menu_skiareas:
                intent = new Intent(this, SkiAreasActivity.class);
                break;
            case R.id.rl_menu_map:
                intent = new Intent(this, MapActivity.class);
                break;
            case R.id.rl_menu_statistics:
                intent = new Intent(this, StatisticsActivity.class);
                break;
            case R.id.rl_menu_more:
                intent = new Intent(this, MoreActivity.class);
                break;
            default:
                break;
        }

        if (intent != null) {
            startActivity(intent);
            finish();
        }
    }

}
