package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.Province;
import org.json.JSONObject;

public class ProvinceSerializer extends JsonSerializer<Province> {
    public static final ProvinceSerializer instance = new ProvinceSerializer();

    private ProvinceSerializer() {}

    @Override
    public Object toJson(Province province) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", province.getId());
        jsonObject.put("name", province.getName());
        jsonObject.put("country", CountrySerializer.instance.toJson(province.getCountry()));

        return jsonObject;
    }
}
