package de.skicomp.utils.helper;

import de.skicomp.enums.FriendshipStatus;

/**
 * Created by benjamin.schneider on 12.05.17.
 */

public class FriendshipStatusHelper {

    private FriendshipStatusHelper() {
        // utility class
    }

    public static String getFriendshipStatusText(FriendshipStatus friendshipStatus) {
        switch (friendshipStatus) {
            case REQUESTED:
                return "angefragt";
            case REQUESTING:
                return "bestätigen";
            case ACCEPTED:
                return "befreundet";
            case DEFAULT:
            default:
                return "hinzufügen";
        }
    }

    public static FriendshipStatus getNextFriendshipStatus(FriendshipStatus friendshipStatus) {
        switch (friendshipStatus) {
            case DEFAULT:
                return FriendshipStatus.PENDING;
            case PENDING:
            case REQUESTING:
                return FriendshipStatus.ACCEPTED;
            case REQUESTED:
            case ACCEPTED:
            default:
                return FriendshipStatus.DEFAULT;

        }
    }
}
