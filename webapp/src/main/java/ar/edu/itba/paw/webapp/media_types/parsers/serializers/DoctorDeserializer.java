package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorSpecialty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collection;
import java.util.LinkedList;

public class DoctorDeserializer extends JsonDeserializer<Doctor> {
    public static final DoctorDeserializer instance = new DoctorDeserializer();

    private DoctorDeserializer() {}

    @Override
    public Doctor fromJson(JsonNode o) {
        if (!(o instanceof ObjectNode)) {
            throw new IllegalArgumentException();
        }

        ObjectNode jsonObject = (ObjectNode) o;

        Doctor doctor = new Doctor();

        JsonNode node = jsonObject.get("phone");
        if (node != null && !node.isNull()) {
            doctor.setPhone(jsonObject.get("phone").asText());
        }
        node = jsonObject.get("email");
        if (node != null && !node.isNull()) {
            doctor.setEmail(jsonObject.get("email").asText());
        }

        ArrayNode jsonSpecialtiesIds = (ArrayNode) jsonObject.get("specialtyIds");
        Collection<DoctorSpecialty> doctorSpecialties = new LinkedList<>();
        for (JsonNode o1 : jsonSpecialtiesIds) {
            if (!o1.isInt())
                throw new IllegalArgumentException();

            DoctorSpecialty doctorSpecialty = new DoctorSpecialty();
            doctorSpecialty.setId(o1.asInt());
            doctorSpecialties.add(doctorSpecialty);
        }
        doctor.setDoctorSpecialties(doctorSpecialties);

        return doctor;
    }
}
