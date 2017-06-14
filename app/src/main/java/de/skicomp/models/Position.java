package de.skicomp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by benjamin.schneider on 09.05.17.
 */

public class Position {

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("timestamp")
    private long timestamp;

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
