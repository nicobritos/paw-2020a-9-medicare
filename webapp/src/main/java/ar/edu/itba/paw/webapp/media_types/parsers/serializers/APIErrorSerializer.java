package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.webapp.models.APIError;
import org.json.JSONObject;

public class APIErrorSerializer extends JsonSerializer<APIError> {
    public static final APIErrorSerializer instance = new APIErrorSerializer();

    private APIErrorSerializer() {}

    @Override
    public Object toJson(APIError apiError) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("code", apiError.getCode());
        jsonObject.put("message", apiError.getMessage());

        return jsonObject;
    }
}
