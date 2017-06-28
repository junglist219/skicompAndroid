package de.skicomp.events.friend;

import de.skicomp.models.Friend;

/**
 * Created by benjamin.schneider on 28.06.17.
 */

public class FriendUpdatedEventSuccess extends FriendUpdatedEvent {

    private final Friend friend;

    public FriendUpdatedEventSuccess(Friend friend) {
        super();
        this.friend = friend;
    }

    public Friend getFriend() {
        return friend;
    }

}
