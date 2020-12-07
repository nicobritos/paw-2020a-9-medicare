package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.webapp.media_types.ErrorMIME;
import ar.edu.itba.paw.webapp.media_types.parsers.serializers.APIErrorSerializer;
import ar.edu.itba.paw.webapp.models.APIError;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

public abstract class ExceptionResponseWriter {
    public static void setError(HttpServletResponse response, Status status) {
        response.setStatus(status.getStatusCode());
        response.setContentType(ErrorMIME.ERROR);

        APIError apiError = APIErrorBuilder.buildError(status);
        try {
            response.getWriter().write(APIErrorSerializer.instance.toJson(apiError).toString());
        } catch (IOException e) {
            // TODO: Log
        }
    }

    public static void setError(HttpServletResponse response, Status status, String message) {
        response.setStatus(status.getStatusCode());
        response.setContentType(ErrorMIME.ERROR);

        try {
            response.getWriter().write(APIErrorSerializer.instance.toJson(new APIError(status.getStatusCode(), message)).toString());
        } catch (IOException e) {
            // TODO: Log
        }
    }
}
