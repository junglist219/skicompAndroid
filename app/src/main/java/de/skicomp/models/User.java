package de.skicomp.models;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by benjamin.schneider on 09.05.17.
 */
@DatabaseTable(tableName = "user")
public class User {

    @DatabaseField(id = true)
    @SerializedName("id")
    private int id;

    @DatabaseField
    @SerializedName("username")
    private String username;

    @DatabaseField
    @SerializedName("firstname")
    private String firstname;

    @DatabaseField
    @SerializedName("lastname")
    private String lastname;

    @DatabaseField
    @SerializedName("email")
    private String email;

    @DatabaseField
    @SerializedName("password")
    private String password;

    @DatabaseField
    @SerializedName("city")
    private String city;

    @DatabaseField
    @SerializedName("country")
    private String country;

    @DatabaseField
    @SerializedName("tracking")
    private boolean trackingEnabled;

    public User() {
        // default constructor
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isTrackingEnabled() {
        return trackingEnabled;
    }

    public void setTrackingEnabled(boolean trackingEnabled) {
        this.trackingEnabled = trackingEnabled;
    }
}
