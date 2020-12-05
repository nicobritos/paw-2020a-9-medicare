package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.webapp.models.APIError;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class APIErrorSerializer extends JsonSerializer<APIError> {
    public static final APIErrorSerializer instance = new APIErrorSerializer();

    private APIErrorSerializer() {}

    @Override
    public JsonNode toJson(APIError apiError) {
        ObjectNode jsonObject = JsonNodeFactory.instance.objectNode();

        jsonObject.put("code", apiError.getCode());
        jsonObject.put("message", apiError.getMessage());

        return jsonObject;
    }
}
