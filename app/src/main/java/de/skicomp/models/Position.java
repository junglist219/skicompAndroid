package de.skicomp.models;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by benjamin.schneider on 09.05.17.
 */

public class Position {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    @SerializedName("latitude")
    private double latitude;

    @DatabaseField
    @SerializedName("longitude")
    private double longitude;

    @DatabaseField
    @SerializedName("timestamp")
    private long timestamp;

    public Position() {
        // default constructor
    }

    public Position(double latitude, double longitude, long timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
