package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.DoctorSpecialty;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.models.DoctorSignUp;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collection;
import java.util.LinkedList;

public class UserDoctorDeserializer extends UserCreateDeserializer<DoctorSignUp> {
    public static final UserDoctorDeserializer instance = new UserDoctorDeserializer();

    private UserDoctorDeserializer() {}

    @Override
    public DoctorSignUp fromJson(JsonNode o) {
        if (!(o instanceof ObjectNode)) {
            throw new IllegalArgumentException();
        }

        ObjectNode jsonObject = (ObjectNode) o;
        JsonNode officeNode = jsonObject.get("office");
        if (!officeNode.isObject())
            throw new IllegalArgumentException();

        ObjectNode officeObject = (ObjectNode) officeNode;

        User user = this.getUser(jsonObject);

        DoctorSignUp doctorSignUp = new DoctorSignUp();
        doctorSignUp.setUser(user);
        doctorSignUp.setRegistrationNumber(this.getIntegerNonNull(jsonObject, "registrationNumber"));

        Locality locality = new Locality();
        locality.setId(this.getIntegerNonNull(officeObject, "localityId"));

        Office office = new Office();
        office.setStreet(this.getStringNonNull(officeObject, "street", s -> s.length() >= 2));
        office.setPhone(this.getStringNull(officeObject, "phone"));
        office.setEmail(this.getStringNull(officeObject, "email"));
        office.setName(this.getStringNull(officeObject, "name"));

        ArrayNode jsonSpecialtiesIds = (ArrayNode) jsonObject.get("specialtyIds");
        Collection<DoctorSpecialty> doctorSpecialties = new LinkedList<>();
        for (JsonNode o1 : jsonSpecialtiesIds) {
            if (!o1.isInt())
                throw new IllegalArgumentException();

            DoctorSpecialty doctorSpecialty = new DoctorSpecialty();
            doctorSpecialty.setId(o1.asInt());
            doctorSpecialties.add(doctorSpecialty);
        }
        doctorSignUp.setDoctorSpecialties(doctorSpecialties);

        return doctorSignUp;
    }
}
