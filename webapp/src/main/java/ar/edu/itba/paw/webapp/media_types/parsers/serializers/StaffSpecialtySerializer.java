package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.StaffSpecialty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StaffSpecialtySerializer extends JsonSerializer<StaffSpecialty> {
    public static final StaffSpecialtySerializer instance = new StaffSpecialtySerializer();

    private StaffSpecialtySerializer() {}

    @Override
    public JsonNode toJson(StaffSpecialty staffSpecialty) {
        ObjectNode jsonObject = JsonNodeFactory.instance.objectNode();

        jsonObject.put("id", staffSpecialty.getId());
        jsonObject.put("name", staffSpecialty.getName());

        return jsonObject;
    }
}
