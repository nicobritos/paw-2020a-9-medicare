package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.StaffSpecialty;
import org.json.JSONObject;

public class StaffSpecialtySerializer extends JsonSerializer<StaffSpecialty> {
    public static final StaffSpecialtySerializer instance = new StaffSpecialtySerializer();

    private StaffSpecialtySerializer() {}

    @Override
    public Object toJson(StaffSpecialty staffSpecialty) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", staffSpecialty.getId());
        jsonObject.put("name", staffSpecialty.getName());

        return jsonObject;
    }
}
