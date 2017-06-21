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
    @SerializedName("description")
    private String description;

    @DatabaseField
    @SerializedName("image_url_overview")
    private String imageUrlOverview;

    @DatabaseField
    @SerializedName("image_url_logo")
    private String imageUrlLogo;

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

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrlOverview() {
        return imageUrlOverview;
    }

    public String getImageUrlLogo() {
        return imageUrlLogo;
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
        return String.valueOf(minHeight).concat("m");
    }

    @Bindable
    public String getDisplayMaxHeight() {
        return String.valueOf(maxHeight).concat("m");
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
        return String.valueOf(slopesExpert).concat("km");
    }

    @Bindable
    public String getDisplaySlopesFreeride() {
        return String.valueOf(slopesFreeride).concat("km");
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