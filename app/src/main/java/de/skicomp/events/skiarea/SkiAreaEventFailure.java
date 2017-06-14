package de.skicomp.events.skiarea;

import retrofit2.Response;

/**
 * Created by benjamin.schneider on 26.05.17.
 */

public class SkiAreaEventFailure extends SkiAreaEvent {

    private Response response;

    public SkiAreaEventFailure(Response response) {
        super();
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

}
