package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.webapp.models.APIError;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(NotFoundException exception) {
        if (exception.getResponse() != null) {
            return Response
                    .status(exception.getResponse().getStatus())
                    .entity(!(exception.getResponse().getEntity() instanceof APIError) ? APIErrorBuilder.buildError(exception.getResponse().getStatus()) : "")
                    .build();
        } else {
            return Response
                    .status(Status.NOT_FOUND)
                    .entity(APIErrorBuilder.buildError(Status.NOT_FOUND))
                    .build();
        }
    }
}