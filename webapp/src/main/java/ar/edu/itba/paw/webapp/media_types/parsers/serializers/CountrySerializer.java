package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.Country;
import org.json.JSONObject;

public class CountrySerializer extends JsonSerializer<Country> {
    public static final CountrySerializer instance = new CountrySerializer();

    private CountrySerializer() {}

    @Override
    public Object toJson(Country country) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", country.getId());
        jsonObject.put("name", country.getName());

        return jsonObject;
    }
}
