package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.webapp.media_types.ErrorMIME;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotAcceptableExceptionMapper implements ExceptionMapper<MissingAcceptsException> {
    @Override
    public Response toResponse(MissingAcceptsException exception) {
        return Response
                .status(Status.NOT_ACCEPTABLE)
                .entity(APIErrorBuilder.buildError(Status.NOT_ACCEPTABLE))
                .type(ErrorMIME.ERROR)
                .build();
    }
}