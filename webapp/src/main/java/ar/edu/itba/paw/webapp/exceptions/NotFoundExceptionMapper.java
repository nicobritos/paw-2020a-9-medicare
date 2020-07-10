package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import ar.edu.itba.paw.webapp.models.APIError;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(NotFoundException exception) {
        if (exception.getResponse() != null) {
            Object entity;
            String mediaType;
            if (!(exception.getResponse().getEntity() instanceof APIError)) {
                entity = APIErrorBuilder.buildError(exception.getResponse().getStatus());
                mediaType = ErrorMIME.ERROR;
            } else {
                entity = "";
                mediaType = MediaType.TEXT_PLAIN;
            }

            return Response
                    .status(exception.getResponse().getStatus())
                    .entity(entity)
                    .type(mediaType)
                    .build();
        } else {
            return Response
                    .status(Status.NOT_FOUND)
                    .entity(APIErrorBuilder.buildError(Status.NOT_FOUND))
                    .type(ErrorMIME.ERROR)
                    .build();
        }
    }
}