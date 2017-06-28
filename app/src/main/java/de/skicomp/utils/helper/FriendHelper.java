package de.skicomp.utils.helper;

import java.util.ArrayList;
import java.util.List;

import de.skicomp.data.manager.FriendManager;
import de.skicomp.enums.FriendshipStatus;
import de.skicomp.models.Friend;

/**
 * Created by benjamin.schneider on 28.06.17.
 */

public class FriendHelper {

    private FriendHelper() {
        // utility class
    }

    public static List<Friend> getFriends() {
        List<Friend> allFriends = FriendManager.getInstance().getFriends();
        List<Friend> friends = new ArrayList<>();

        for (Friend friend : allFriends) {
            if (friend.getFriendshipStatus() == FriendshipStatus.ACCEPTED) {
                friends.add(friend);
            }
        }
        return friends;
    }

    public static List<Friend> getFriendsRequesting() {
        List<Friend> allFriends = FriendManager.getInstance().getFriends();
        List<Friend> friendsRequesting = new ArrayList<>();

        for (Friend friend : allFriends) {
            if (friend.getFriendshipStatus() == FriendshipStatus.REQUESTING) {
                friendsRequesting.add(friend);
            }
        }
        return friendsRequesting;
    }

    public static List<Friend> getFriendsRequested() {
        List<Friend> allFriends = FriendManager.getInstance().getFriends();
        List<Friend> friendsRequested = new ArrayList<>();

        for (Friend friend : allFriends) {
            if (friend.getFriendshipStatus() == FriendshipStatus.REQUESTED) {
                friendsRequested.add(friend);
            }
        }
        return friendsRequested;
    }

}
