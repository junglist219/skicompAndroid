package de.skicomp.network;

import de.skicomp.models.weather.WeatherForecast;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by benjamin.schneider on 15.05.17.
 */

public interface WeatherServiceApi {

    @GET("forecast")
    Call<WeatherForecast> getForecast(@Query("lat") double latitude,
                                      @Query("lon") double longitude,
                                      @Query("appid") String accessKey,
                                      @Query("units") String units);

}
