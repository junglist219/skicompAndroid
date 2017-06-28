package de.skicomp.events.friend;

import retrofit2.Response;

/**
 * Created by benjamin.schneider on 28.06.17.
 */

public class FriendUpdatedEventFailure extends FriendUpdatedEvent {

    private Response response;

    public FriendUpdatedEventFailure(Response response) {
        super();
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

}
