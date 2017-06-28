package de.skicomp.events.friend;

import retrofit2.Response;

/**
 * Created by benjamin.schneider on 28.06.17.
 */

public class FriendEventFailure extends FriendEvent {

    private Response response;

    public FriendEventFailure(Response response) {
        super();
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

}
