package de.skicomp.activities.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import de.skicomp.R;
import de.skicomp.activities.BottomNavigationActivity;
import de.skicomp.adapter.SkiAreaCountryAdapter;
import de.skicomp.data.manager.SkiAreaManager;
import de.skicomp.databinding.ActivitySkiareasBinding;
import de.skicomp.enums.SkiAreaCountry;
import de.skicomp.events.skiarea.SkiAreaEvent;
import de.skicomp.events.skiarea.SkiAreaEventFailure;
import de.skicomp.events.skiarea.SkiAreaEventSuccess;
import de.skicomp.fragments.skiareas.SkiAreaOverviewFragment;
import de.skicomp.models.SkiArea;
import de.skicomp.utils.ProgressDialogManager;
import de.skicomp.utils.helper.SkiAreaHelper;
import de.skicomp.utils.viewutils.ItemOffsetDecoration;
import retrofit2.Response;

/**
 * Created by benjamin.schneider on 14.06.17.
 */

public class SkiAreasActivity extends BottomNavigationActivity implements SkiAreaCountryAdapter.SkiAreaCountryAdapterListener {

    private ActivitySkiareasBinding viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_skiareas);
        viewBinding.setHandler(this);
        viewBinding.rvSkiareasCountryOverview.setAdapter(new SkiAreaCountryAdapter(getApplicationContext(), this, new ArrayList<SkiAreaCountry>()));


        List<SkiArea> skiAreaList = SkiAreaManager.getInstance().getSkiAreas();
        if (skiAreaList == null || skiAreaList.isEmpty()) {
            SkiAreaManager.getInstance().loadSkiAreas();
        } else {
            showSkiAreaCountryOverview();
        }

        /*
        RecyclerView rvSkiAreas = (RecyclerView) findViewById(R.id.rv_skiareas_overview);
        rvSkiAreas.setHasFixedSize(true);

        StaggeredGridLayoutManager stGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvSkiAreas.setLayoutManager(stGridLayoutManager);

        ItemOffsetDecoration itemOffsetDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.rv_skiareas_overview_item_offset);
        rvSkiAreas.addItemDecoration(itemOffsetDecoration);

        if (SessionManager.getInstance().getSkiAreas() != null) {
            SkiAreaAdapter skiAreaAdapter = new SkiAreaAdapter(getApplicationContext(), this, SessionManager.getInstance().getSkiAreas());
            rvSkiAreas.setAdapter(skiAreaAdapter);
        }
        */
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.rl_menu_skiareas;
    }

    @Override
    public void setSelectedMenuItem() {
        viewBinding.navigation.btMenuSkiareasSelected.setVisibility(View.VISIBLE);
        viewBinding.navigation.btMenuSkiareas.setImageResource(R.drawable.ic_menu_skiareas_selected);
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

    private void showSkiAreaCountryOverview() {
        List<SkiAreaCountry> skiAreaCountryList = SkiAreaHelper.filterSkiAreaCountries(SkiAreaManager.getInstance().getSkiAreas());

        StaggeredGridLayoutManager stGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        viewBinding.rvSkiareasCountryOverview.setLayoutManager(stGridLayoutManager);
        viewBinding.rvSkiareasCountryOverview.setHasFixedSize(true);

        ItemOffsetDecoration itemOffsetDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.rv_skiareas_overview_item_offset);
        viewBinding.rvSkiareasCountryOverview.addItemDecoration(itemOffsetDecoration);

        if (skiAreaCountryList != null && !skiAreaCountryList.isEmpty()) {
            SkiAreaCountryAdapter skiAreaCountryAdapter = new SkiAreaCountryAdapter(getApplicationContext(), this, skiAreaCountryList);
            viewBinding.rvSkiareasCountryOverview.setAdapter(skiAreaCountryAdapter);
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onSkiAreaEvent(SkiAreaEvent event) {
        ProgressDialogManager.getInstance().stopProgressDialog();
        if (event instanceof SkiAreaEventSuccess) {
            Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
            showSkiAreaCountryOverview();
        } else {
            Response response = ((SkiAreaEventFailure) event).getResponse();
            super.handleError(response);
        }
    }

    @Override
    public void onSkiAreaCountrySelected(String country) {
        Bundle bundle = new Bundle();
        bundle.putString(SkiAreaOverviewFragment.KEY_SKIAREAS_COUNTRY, country);
        SkiAreaOverviewFragment skiAreaOverviewFragment = new SkiAreaOverviewFragment();
        skiAreaOverviewFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_container, skiAreaOverviewFragment)
                .addToBackStack(SkiAreaOverviewFragment.TAG)
                .commit();
    }

}
