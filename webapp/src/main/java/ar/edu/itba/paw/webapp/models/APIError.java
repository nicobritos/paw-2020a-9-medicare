package ar.edu.itba.paw.webapp.models;

public class APIError {
    private int code;
    private String message;

    public APIError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
