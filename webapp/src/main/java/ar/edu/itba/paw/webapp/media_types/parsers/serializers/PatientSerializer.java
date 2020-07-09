package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.Patient;
import org.json.JSONObject;

public class PatientSerializer extends JsonSerializer<Patient> {
    public static final PatientSerializer instance = new PatientSerializer();

    private PatientSerializer() {}

    @Override
    public Object toJson(Patient patient) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", patient.getId());
        jsonObject.put("user", UserSerializer.instance.toJson(patient.getUser()));
        jsonObject.put("officeId", patient.getOffice().getId());

        return jsonObject;
    }
}
