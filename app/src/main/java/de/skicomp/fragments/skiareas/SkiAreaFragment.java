package de.skicomp.fragments.skiareas;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.skicomp.R;
import de.skicomp.models.SkiArea;

/**
 * Created by benjamin.schneider on 12.05.17.
 */

public class SkiAreaFragment extends Fragment {

    public static final String TAG = SkiAreaFragment.class.getSimpleName();

    public static final String KEY_SKIAREA = "keySkiArea";

    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.expanded_toolbar_image) ImageView ivExpandedToolbarImage;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.iv_skiarea_logo) ImageView ivSkiAreaLogo;

    @BindView(R.id.tv_skiarea_country) TextView tvSkiAreaCountry;
    @BindView(R.id.tv_skiarea_state) TextView tvSkiAreaState;
    @BindView(R.id.tv_skiarea_description) TextView tvSkiAreaDescription;

    @BindView(R.id.tv_skiarea_min_height) TextView tvSkiAreaMinHeight;
    @BindView(R.id.tv_skiarea_max_height) TextView tvSkiAreaMaxHeight;

    @BindView(R.id.tv_skiarea_slopes_easy) TextView tvSkiAreaEasy;
    @BindView(R.id.tv_skiarea_slopes_moderate) TextView tvSkiAreaModerate;
    @BindView(R.id.tv_skiarea_slopes_expert) TextView tvSkiAreaExpert;
    @BindView(R.id.tv_skiarea_slopes_freeride) TextView tvSkiAreaFreeride;

    @BindView(R.id.tv_skiarea_drag_lifts) TextView tvSkiAreaDragLifts;
    @BindView(R.id.tv_skiarea_chair_lifts) TextView tvSkiAreaChairLifts;
    @BindView(R.id.tv_skiarea_gondola_lifts) TextView tvSkiAreaGondolaLifts;
    @BindView(R.id.tv_skiarea_aerial_tramways) TextView tvSkiAreaAerialTramways;
    @BindView(R.id.tv_skiarea_railways) TextView tvSkiAreaRailways;

    @BindView(R.id.bt_skiarea_weather) Button btSkiAreaWeather;

    private Unbinder unbinder;

    private SkiArea skiArea;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_skiarea, container, false);
        unbinder = ButterKnife.bind(this, inflatedView);

        return inflatedView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            skiArea = (SkiArea) bundle.getSerializable(KEY_SKIAREA);

            setToolbar();
            setToolbarBackButton();
            initializeViews();
            requestWeather();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setToolbar() {
        collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(getContext(), android.R.color.white));
        collapsingToolbar.setTitle(skiArea.getName());

        Glide.with(getContext())
                .load(skiArea.getImageUrlOverview())
                .into(ivExpandedToolbarImage);
    }

    private void setToolbarBackButton() {
        final Drawable upArrow = ContextCompat.getDrawable(getContext(), R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(getContext(), android.R.color.white), PorterDuff.Mode.SRC_ATOP);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeViews() {
        Glide.with(getContext()).load(skiArea.getImageUrlLogo()).into(ivSkiAreaLogo);
        tvSkiAreaCountry.setText(skiArea.getCountry());
        tvSkiAreaState.setText(skiArea.getState());
        tvSkiAreaDescription.setText(skiArea.getDescription());

        tvSkiAreaMinHeight.setText(skiArea.getMinHeight() + "m");
        tvSkiAreaMaxHeight.setText(skiArea.getMaxHeight() + "m");

        tvSkiAreaEasy.setText(String.format(getContext().getString(R.string.skiarea_slopes_easy), skiArea.getSlopesEasy()));
        tvSkiAreaModerate.setText(String.format(getContext().getString(R.string.skiarea_slopes_moderate), skiArea.getSlopesModerate()));
        tvSkiAreaExpert.setText(String.format(getContext().getString(R.string.skiarea_slopes_expert), skiArea.getSlopesExpert()));
        tvSkiAreaFreeride.setText(String.format(getContext().getString(R.string.skiarea_slopes_freeride), skiArea.getSlopesFreeride()));

        tvSkiAreaDragLifts.setText(String.valueOf(skiArea.getDragLifts()));
        tvSkiAreaChairLifts.setText(String.valueOf(skiArea.getChairLifts()));
        tvSkiAreaGondolaLifts.setText(String.valueOf(skiArea.getGondolaLifts()));
        tvSkiAreaAerialTramways.setText(String.valueOf(skiArea.getAerialTramways()));
        tvSkiAreaRailways.setText(String.valueOf(skiArea.getRailways()));
    }

    private void requestWeather() {
        LatLng northWest = new LatLng(skiArea.getBoundingBoxNorth(), skiArea.getBoundingBoxWest());
        LatLng southEast = new LatLng(skiArea.getBoundingBoxSouth(), skiArea.getBoundingBoxEast());

        LatLngBounds skiAreaBounds = LatLngBounds.builder().include(northWest).include(southEast).build();
        LatLng skiAreaCenter = skiAreaBounds.getCenter();
//        WeatherService.getInstance().getForecast(new Callback<WeatherForecast>() {
//            @Override
//            public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    ((BaseActivity) getActivity()).handleError(response);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<WeatherForecast> call, Throwable t) {
//                Toast.makeText(getContext(), "failure", Toast.LENGTH_SHORT).show();
//            }
//        }, skiAreaCenter.latitude, skiAreaCenter.longitude);
    }
}
