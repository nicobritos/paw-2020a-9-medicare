package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import ar.edu.itba.paw.webapp.media_types.parsers.serializers.APIErrorSerializer;
import ar.edu.itba.paw.webapp.models.error.APIError;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

public abstract class ExceptionResponseWriter {
    public static void setError(HttpServletResponse response, Status status) {
        ExceptionResponseWriter.setError(response, APIErrorFactory.buildError(status).build());
    }

    public static void setError(HttpServletResponse response, Status status, String message) {
        ExceptionResponseWriter.setError(response, new APIError(status.getStatusCode(), message));
    }

    public static void setError(HttpServletResponse response, APIError error) {
        response.setStatus(error.getCode());
        response.setContentType(ErrorMIME.ERROR);

        try {
            response.getWriter().write(APIErrorSerializer.instance.toJson(error).toString());
        } catch (IOException e) {
            // TODO: Log
        }
    }
}
