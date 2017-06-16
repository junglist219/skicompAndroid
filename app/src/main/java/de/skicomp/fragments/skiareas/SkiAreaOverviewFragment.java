package de.skicomp.fragments.skiareas;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.skicomp.R;
import de.skicomp.adapter.SkiAreaAdapter;
import de.skicomp.data.manager.SkiAreaManager;
import de.skicomp.databinding.FragmentSkiareaOverviewBinding;
import de.skicomp.models.SkiArea;
import de.skicomp.utils.helper.SkiAreaHelper;

/**
 * Created by benjamin.schneider on 27.05.17.
 */

public class SkiAreaOverviewFragment extends Fragment implements SkiAreaAdapter.SkiAreaAdapterListener {

    public static final String TAG = SkiAreaOverviewFragment.class.getSimpleName();

    public static final String KEY_SKIAREAS_COUNTRY = "keySkiAreasCountry";

    private FragmentSkiareaOverviewBinding viewBinding;
    private List<SkiArea> skiAreaList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_skiarea_overview, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String countryName = bundle.getString(KEY_SKIAREAS_COUNTRY);
            skiAreaList = SkiAreaHelper.filterSkiAreasByCountry(SkiAreaManager.getInstance().getSkiAreas(), countryName);

            initToolbar(countryName);
            initRecyclerView();
        }

        return viewBinding.getRoot();
    }

    private void initToolbar(String countryName) {
        ((AppCompatActivity) getActivity()).setSupportActionBar(viewBinding.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_bottom);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewBinding.toolbarTitle.setText(countryName);
    }

    private void initRecyclerView() {
        viewBinding.rvSkiareasOverview.setLayoutManager(new LinearLayoutManager(getContext()));
        viewBinding.rvSkiareasOverview.setAdapter(new SkiAreaAdapter(getContext(), this, skiAreaList));
    }

    @Override
    public void onSkiAreaSelected(SkiArea skiArea) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(SkiAreaFragment.KEY_SKIAREA, skiArea);
        SkiAreaFragment skiAreaFragment = new SkiAreaFragment();
        skiAreaFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.bottom_sheet_slide_in, R.anim.bottom_sheet_slide_out, R.anim.bottom_sheet_slide_in, R.anim.bottom_sheet_slide_out)
                .add(R.id.fl_container, skiAreaFragment)
                .addToBackStack(SkiAreaFragment.TAG)
                .commit();
    }
}
