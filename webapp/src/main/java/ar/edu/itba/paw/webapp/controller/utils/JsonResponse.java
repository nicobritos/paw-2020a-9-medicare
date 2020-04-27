package ar.edu.itba.paw.webapp.controller.utils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class JsonResponse {
    private JsonResponseStatus status;
    private Object data = "";

    public JsonResponseStatus getStatus() {
        return this.status;
    }

    public void setStatus(JsonResponseStatus status) {
        this.status = status;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
        this.status = new JsonResponseStatus();
    }

    public static class JsonResponseStatus {
        private List<String> messages;
        private boolean error;

        public JsonResponseStatus(String message) {
            this.messages = new LinkedList<>();
            this.messages.add(message);
            this.error = true;
        }

        public JsonResponseStatus(Collection<String> messages) {
            this.messages = new LinkedList<>(messages);
            this.error = true;
        }

        public JsonResponseStatus() {
            this.messages = new LinkedList<>();
        }

        public boolean isError() {
            return this.error;
        }

        public List<String> getMessages() {
            return this.messages;
        }
    }
}
