package de.skicomp.models;

import com.google.gson.annotations.SerializedName;

import de.skicomp.enums.FriendshipStatus;

/**
 * Created by benjamin.schneider on 09.05.17.
 */

public class Friend {

    @SerializedName("username")
    private String username;

    @SerializedName("firstname")
    private String firstname;

    @SerializedName("lastname")
    private String lastname;

    @SerializedName("friendship_status")
    private FriendshipStatus friendshipStatus;

    @SerializedName("position")
    private Position position;

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
}
