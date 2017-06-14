package de.skicomp.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.skicomp.BuildConfig;
import de.skicomp.data.serializer.WeatherForecastJsonDeserializer;
import de.skicomp.models.weather.WeatherForecast;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by benjamin.schneider on 15.05.17.
 */

public class WeatherService {

    public static final String APIURL = BuildConfig.WEATHER_API_URL;
    private static final String OPEN_WEATHER_API_KEY = "f6883b87d91328d044ef17ac8a49427c";

    private static final long CONNECTION_TIMEOUT = 5000;
    private static final long READ_TIMEOUT = 5000;

    private static WeatherService instance;

    private final WeatherServiceApi mWeatherServiceApi;

    public WeatherService() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(WeatherForecast.class, new WeatherForecastJsonDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIURL)
                .client(createOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mWeatherServiceApi = retrofit.create(WeatherServiceApi.class);
    }

    public static WeatherService createInstance() {
        synchronized (WeatherService.class) {
            if (WeatherService.instance == null) {
                WeatherService.instance = new WeatherService();
            }
            return WeatherService.instance;
        }
    }

    public static WeatherService getInstance() {
        synchronized (WeatherService.class) {
            return WeatherService.instance;
        }
    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.networkInterceptors().add(createLoggingInterceptor());
        builder.interceptors().add(createRequestInterceptor());
        builder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
        return builder.build();
    }

    private Interceptor createRequestInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder requestBuilder = chain.request().newBuilder();

                requestBuilder.addHeader("Accept", "application/json");
                requestBuilder.addHeader("Content-Type", "application/json");
                requestBuilder.addHeader("x-api-key", OPEN_WEATHER_API_KEY);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
    }

    private Interceptor createLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return httpLoggingInterceptor;
    }

    //----------------------------------------------------------------------------------------------
    // GET
    //----------------------------------------------------------------------------------------------

    public void getForecast(Callback<WeatherForecast> callback, double latitude, double longitude) {
        Call<WeatherForecast> getForecastRequest = mWeatherServiceApi.getForecast(latitude, longitude, OPEN_WEATHER_API_KEY, "metric");
        getForecastRequest.enqueue(callback);
    }

}
