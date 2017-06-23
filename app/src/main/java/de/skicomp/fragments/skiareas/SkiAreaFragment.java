package de.skicomp.fragments.skiareas;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.greenrobot.eventbus.EventBus;

import de.skicomp.R;
import de.skicomp.activities.BaseActivity;
import de.skicomp.data.manager.SkiAreaManager;
import de.skicomp.databinding.FragmentSkiareaBinding;
import de.skicomp.events.UpdatedFavoriteSkiAreasEvent;
import de.skicomp.models.SkiArea;
import de.skicomp.models.weather.WeatherForecast;
import de.skicomp.network.WeatherService;
import de.skicomp.utils.Utils;
import de.skicomp.utils.helper.SkiAreaHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by benjamin.schneider on 12.05.17.
 */

public class SkiAreaFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {

    public static final String TAG = SkiAreaFragment.class.getSimpleName();

    public static final String KEY_SKIAREA = "keySkiArea";

    private SkiArea skiArea;
    private FragmentSkiareaBinding viewBinding;

    // toolbar collapsed -> show title
    boolean isShow = false;
    int scrollRange = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_skiarea, container, false);
        viewBinding.setHandler(this);
        viewBinding.appBarLayout.addOnOffsetChangedListener(this);

        initToolbar();

        Bundle bundle = getArguments();
        if (bundle != null) {
            skiArea = (SkiArea) bundle.getSerializable(KEY_SKIAREA);

            viewBinding.setSkiArea(skiArea);
            viewBinding.skiareaSlopes.scSlopes.setSkiArea(skiArea);
            viewBinding.skiareaSlopes.scSlopes.invalidate();
            viewBinding.ivFavorite.setImageResource(SkiAreaHelper.favoritesContainsSkiArea(skiArea) ? R.drawable.ic_favorite_selected : R.drawable.ic_favorite);
        }

        return viewBinding.getRoot();
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(viewBinding.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (scrollRange == -1) {
            scrollRange = appBarLayout.getTotalScrollRange();
        }
        if (scrollRange + verticalOffset == 0) {
            viewBinding.collapsingToolbarLayout.setTitle(skiArea.getName());
            isShow = true;
        } else if(isShow) {
            viewBinding.collapsingToolbarLayout.setTitle(" ");
            isShow = false;
        }
    }

    @SuppressWarnings("unused")
    public void onClickFavorite(View view) {
        if (!SkiAreaHelper.favoritesContainsSkiArea(skiArea)) {
            SkiAreaManager.getInstance().addSkiAreaToFavorites(skiArea);
            viewBinding.ivFavorite.setImageResource(R.drawable.ic_favorite_selected);
            Utils.showSnackbar(viewBinding.getRoot(), R.string.favorites_added, Snackbar.LENGTH_SHORT);
        } else {
            SkiAreaManager.getInstance().removeSkiAreaFromFavorites(skiArea);
            viewBinding.ivFavorite.setImageResource(R.drawable.ic_favorite);
            Utils.showSnackbar(viewBinding.getRoot(), R.string.favorites_removed, Snackbar.LENGTH_SHORT);
        }
        viewBinding.ivFavorite.invalidate();
        EventBus.getDefault().post(new UpdatedFavoriteSkiAreasEvent());
    }

    private void requestWeather() {
        LatLng northWest = new LatLng(skiArea.getBoundingBoxNorth(), skiArea.getBoundingBoxWest());
        LatLng southEast = new LatLng(skiArea.getBoundingBoxSouth(), skiArea.getBoundingBoxEast());

        LatLngBounds skiAreaBounds = LatLngBounds.builder().include(northWest).include(southEast).build();
        LatLng skiAreaCenter = skiAreaBounds.getCenter();
        WeatherService.getInstance().getForecast(new Callback<WeatherForecast>() {
            @Override
            public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();

                } else {
                    ((BaseActivity) getActivity()).handleError(response);
                }
            }

            @Override
            public void onFailure(Call<WeatherForecast> call, Throwable t) {
                Toast.makeText(getContext(), "failure", Toast.LENGTH_SHORT).show();
            }
        }, skiAreaCenter.latitude, skiAreaCenter.longitude);
    }
}
