package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.Locality;
import org.json.JSONObject;

public class LocalitySerializer extends JsonSerializer<Locality> {
    public static final LocalitySerializer instance = new LocalitySerializer();

    private LocalitySerializer() {}

    @Override
    public Object toJson(Locality locality) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", locality.getId());
        jsonObject.put("name", locality.getName());
        jsonObject.put("province", ProvinceSerializer.instance.toJson(locality.getProvince()));

        return jsonObject;
    }
}
