package ar.edu.itba.paw.webapp.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class JerseyExceptionMapper implements ExceptionMapper<WebApplicationException> {
    @Override
    public Response toResponse(WebApplicationException exception) {
        return Response.status(exception.getResponse() != null ? exception.getResponse().getStatus() : Status.INTERNAL_SERVER_ERROR.getStatusCode()).entity("").build();
    }
}