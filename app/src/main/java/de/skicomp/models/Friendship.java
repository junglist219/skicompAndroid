package de.skicomp.models;

import com.google.gson.annotations.SerializedName;

import de.skicomp.enums.FriendshipStatus;

/**
 * Created by benjamin.schneider on 09.05.17.
 */

public class Friendship {

    @SerializedName("from_user")
    private String fromUser;

    @SerializedName("to_user")
    private String toUser;

    @SerializedName("status")
    private FriendshipStatus friendshipStatus;

}
