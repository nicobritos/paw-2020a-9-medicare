package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.webapp.models.APIError;

import javax.ws.rs.core.Response.Status;

public abstract class APIErrorBuilder {
    public static APIError buildError(Status status) {
        return new APIError(
                status.getStatusCode(),
                status.toString()
        );
    }

    public static APIError buildError(int code) {
        Status status = Status.fromStatusCode(code);
        if (status == null) {
            return new APIError(code, "");
        } else {
            return buildError(status);
        }
    }
}
