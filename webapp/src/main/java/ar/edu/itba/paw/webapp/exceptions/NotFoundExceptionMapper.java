package ar.edu.itba.paw.webapp.exceptions;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(NotFoundException exception) {
        return Response.status(exception.getResponse() != null ? exception.getResponse().getStatus() : Status.NOT_FOUND.getStatusCode()).entity("").build();
    }
}