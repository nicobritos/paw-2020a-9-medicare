package ar.edu.itba.paw.webapp.models;

import java.util.Collection;
import java.util.Collections;

public class APIError extends APIBaseError {
    private final Collection<APISubError> subErrors;

    public APIError(int code, String message) {
        this(code, message, Collections.emptyList());
    }

    public APIError(int code, String message, Collection<APISubError> subErrors) {
        super(code, message);
        this.subErrors = subErrors;
    }

    public Collection<APISubError> getSubErrors() {
        return this.subErrors;
    }
}
