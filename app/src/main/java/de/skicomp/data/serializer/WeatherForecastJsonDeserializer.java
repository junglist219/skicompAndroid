package de.skicomp.data.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import de.skicomp.models.weather.WeatherCondition;
import de.skicomp.models.weather.WeatherForecast;

/**
 * Created by benjamin.schneider on 15.05.17.
 */

public class WeatherForecastJsonDeserializer implements JsonDeserializer<WeatherForecast>, JsonSerializer<WeatherForecast> {

    @Override
    public WeatherForecast deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();

        WeatherForecast weatherForecast = gson.fromJson(json, WeatherForecast.class);
        JsonArray jaWeatherConditions = json.getAsJsonObject().get("list").getAsJsonArray();

        for (int i = 0; i < jaWeatherConditions.size(); i++) {
            WeatherCondition weatherCondition = weatherForecast.getWeatherConditionList().get(i);

            JsonObject joWeatherCondition = jaWeatherConditions.get(i).getAsJsonObject();

            JsonObject joWeatherConditionMain = joWeatherCondition.get("main").getAsJsonObject();
            JsonObject joWeatherConditionWeather = joWeatherCondition.get("weather").getAsJsonArray().get(0).getAsJsonObject();
            JsonObject joWeatherConditionWind = joWeatherCondition.get("wind").getAsJsonObject();

            weatherCondition.setTemp(joWeatherConditionMain.get("temp").getAsDouble());
            weatherCondition.setTempMin(joWeatherConditionMain.get("temp_min").getAsDouble());
            weatherCondition.setTempMax(joWeatherConditionMain.get("temp_max").getAsDouble());
            weatherCondition.setHumidity(joWeatherConditionMain.get("humidity").getAsDouble());

            weatherCondition.setWeatherID(joWeatherConditionWeather.get("id").getAsInt());
            weatherCondition.setWeatherTitle(joWeatherConditionWeather.get("main").getAsString());
            weatherCondition.setWeatherDescription(joWeatherConditionWeather.get("description").getAsString());
            weatherCondition.setWeatherIcon(joWeatherConditionWeather.get("icon").getAsString());

            weatherCondition.setWindSpeed(joWeatherConditionWind.get("speed").getAsDouble());
            weatherCondition.setWindDegrees(joWeatherConditionWind.get("deg").getAsDouble());
        }

        JsonObject joWeatherForecastCity = json.getAsJsonObject().get("city").getAsJsonObject();
        weatherForecast.setCityName(joWeatherForecastCity.get("name").getAsString());


        return weatherForecast;
    }

    @Override
    public JsonElement serialize(WeatherForecast src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }
}
