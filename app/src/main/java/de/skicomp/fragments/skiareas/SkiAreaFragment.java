package de.skicomp.fragments.skiareas;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import de.skicomp.R;
import de.skicomp.activities.BaseActivity;
import de.skicomp.databinding.FragmentSkiareaBinding;
import de.skicomp.models.SkiArea;
import de.skicomp.models.weather.WeatherForecast;
import de.skicomp.network.WeatherService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by benjamin.schneider on 12.05.17.
 */

public class SkiAreaFragment extends Fragment {

    public static final String TAG = SkiAreaFragment.class.getSimpleName();

    public static final String KEY_SKIAREA = "keySkiArea";

    private SkiArea skiArea;
    private FragmentSkiareaBinding viewBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_skiarea, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            skiArea = (SkiArea) bundle.getSerializable(KEY_SKIAREA);
            viewBinding.setSkiArea(skiArea);
            viewBinding.skiareaSlopes.scSlopes.setSkiArea(skiArea);
            viewBinding.skiareaSlopes.scSlopes.invalidate();

//            requestWeather();
        }

        ((AppCompatActivity) getActivity()).setSupportActionBar(viewBinding.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return viewBinding.getRoot();
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
