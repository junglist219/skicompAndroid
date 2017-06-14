package de.skicomp.models.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by benjamin.schneider on 15.05.17.
 */

public class WeatherForecast {

    @SerializedName("list")
    private List<WeatherCondition> weatherConditionList;

    private String cityName;

    public List<WeatherCondition> getWeatherConditionList() {
        return weatherConditionList;
    }

    public void setWeatherConditionList(List<WeatherCondition> weatherConditionList) {
        this.weatherConditionList = weatherConditionList;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
