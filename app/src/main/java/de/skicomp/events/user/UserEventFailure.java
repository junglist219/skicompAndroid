package de.skicomp.events.user;

import retrofit2.Response;

/**
 * Created by benjamin.schneider on 18.05.17.
 */

public class UserEventFailure extends UserEvent {

    private Response response;

    public UserEventFailure(Response response) {
        super();
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
