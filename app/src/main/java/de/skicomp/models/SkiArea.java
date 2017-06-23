package de.skicomp.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by benjamin.schneider on 09.05.17.
 */
@DatabaseTable(tableName = "skiarea")
public class SkiArea extends BaseObservable implements Serializable {

    @DatabaseField(id = true)
    @SerializedName("id")
    private int id;

    @DatabaseField
    @SerializedName("name")
    private String name;

    @DatabaseField
    @SerializedName("country")
    private String country;

    @DatabaseField
    @SerializedName("state")
    private String state;

    @DatabaseField
    @SerializedName("min_height")
    private int minHeight;

    @DatabaseField
    @SerializedName("max_height")
    private int maxHeight;

    @DatabaseField
    @SerializedName("slopes_easy")
    private int slopesEasy;

    @DatabaseField
    @SerializedName("slopes_moderate")
    private int slopesModerate;

    @DatabaseField
    @SerializedName("slopes_expert")
    private int slopesExpert;

    @DatabaseField
    @SerializedName("slopes_freeride")
    private int slopesFreeride;

    @DatabaseField
    @SerializedName("drag_lifts")
    private int dragLifts;

    @DatabaseField
    @SerializedName("chair_lifts")
    private int chairLifts;

    @DatabaseField
    @SerializedName("gondola_lifts")
    private int gondolaLifts;

    @DatabaseField
    @SerializedName("aerial_tramways")
    private int aerialTramways;

    @DatabaseField
    @SerializedName("railways")
    private int railways;

    @DatabaseField
    @SerializedName("bounding_box_west")
    private double boundingBoxWest;

    @DatabaseField
    @SerializedName("bounding_box_south")
    private double boundingBoxSouth;

    @DatabaseField
    @SerializedName("bounding_box_east")
    private double boundingBoxEast;

    @DatabaseField
    @SerializedName("bounding_box_north")
    private double boundingBoxNorth;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getSlopesEasy() {
        return slopesEasy;
    }

    public int getSlopesModerate() {
        return slopesModerate;
    }

    public int getSlopesExpert() {
        return slopesExpert;
    }

    public int getSlopesFreeride() {
        return slopesFreeride;
    }

    public int getDragLifts() {
        return dragLifts;
    }

    public int getChairLifts() {
        return chairLifts;
    }

    public int getGondolaLifts() {
        return gondolaLifts;
    }

    public int getAerialTramways() {
        return aerialTramways;
    }

    public int getRailways() {
        return railways;
    }

    public double getBoundingBoxWest() {
        return boundingBoxWest;
    }

    public double getBoundingBoxSouth() {
        return boundingBoxSouth;
    }

    public double getBoundingBoxEast() {
        return boundingBoxEast;
    }

    public double getBoundingBoxNorth() {
        return boundingBoxNorth;
    }

    @Bindable
    public String getDisplayMinHeight() {
        return concatWithM(minHeight);
    }

    @Bindable
    public String getDisplayMaxHeight() {
        return concatWithM(maxHeight);
    }

    @Bindable
    public String getDisplaySlopesEasy() {
        return concatWithKM(slopesEasy);
    }

    @Bindable
    public String getDisplaySlopesModerate() {
        return concatWithKM(slopesModerate);
    }

    @Bindable
    public String getDisplaySlopesExpert() {
        return concatWithKM(slopesExpert);
    }

    @Bindable
    public String getDisplaySlopesFreeride() {
        return concatWithKM(slopesFreeride);
    }

    @Bindable
    public String getSlopesLength() {
        int slopesLength = slopesEasy + slopesModerate + slopesExpert + slopesFreeride;
        return String.valueOf(slopesLength).concat("km");
    }

    private String concatWithM(int intValue) {
        return String.valueOf(intValue).concat(" m");
    }

    private String concatWithKM(int intValue) {
        return String.valueOf(intValue).concat(" km");
    }
}