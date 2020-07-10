package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import ar.edu.itba.paw.webapp.models.APIError;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class JerseyExceptionMapper implements ExceptionMapper<WebApplicationException> {
    @Override
    @Produces({ErrorMIME.ERROR, MediaType.TEXT_PLAIN})
    public Response toResponse(WebApplicationException exception) {
        if (exception.getResponse() != null) {
            return Response
                    .status(exception.getResponse().getStatus())
                    .entity(!(exception.getResponse().getEntity() instanceof APIError) ? APIErrorBuilder.buildError(exception.getResponse().getStatus()) : "")
                    .build();
        } else {
            return Response
                    .status(Status.INTERNAL_SERVER_ERROR)
                    .entity(APIErrorBuilder.buildError(Status.INTERNAL_SERVER_ERROR))
                    .build();
        }
    }
}