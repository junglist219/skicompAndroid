package de.skicomp.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.skicomp.enums.FriendshipStatus;

/**
 * Created by benjamin.schneider on 09.05.17.
 */
@DatabaseTable(tableName = "friend")
public class Friend extends BaseObservable {

    @DatabaseField(id = true)
    @SerializedName("username")
    private String username;

    @DatabaseField
    @SerializedName("firstname")
    private String firstname;

    @DatabaseField
    @SerializedName("lastname")
    private String lastname;

    @DatabaseField
    @SerializedName("friendship_status")
    private FriendshipStatus friendshipStatus;

    @DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true)
    @SerializedName("position")
    private Position position;

    public Friend() {
        // default constructor
    }

    public Friend(String username, String firstname, String lastname, FriendshipStatus friendshipStatus, Position position) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.friendshipStatus = friendshipStatus;
        this.position = position;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public FriendshipStatus getFriendshipStatus() {
        return friendshipStatus;
    }

    public Position getPosition() {
        return position;
    }

    public void setFriendshipStatus(FriendshipStatus friendshipStatus) {
        this.friendshipStatus = friendshipStatus;
    }

    @Bindable
    public String getFriendStatusDisplayText() {
        switch (friendshipStatus) {
            case DEFAULT:
                return "HINZUFÜGEN";
            case REQUESTED:
                return "ANFRAGE ZURÜCKZIEHEN";
            case REQUESTING:
                return "AKZEPTIEREN";
            case ACCEPTED:
                return "ENTFERNEN";
            default:
                return "HINZUFÜGEN";
        }
    }
}
