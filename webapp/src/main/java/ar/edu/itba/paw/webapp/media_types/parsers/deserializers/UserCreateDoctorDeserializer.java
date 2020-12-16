package ar.edu.itba.paw.webapp.media_types.parsers.deserializers;

import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.models.DoctorSignUp;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class UserCreateDoctorDeserializer extends UserCreateDeserializer<DoctorSignUp> {
    public static final UserCreateDoctorDeserializer instance = new UserCreateDoctorDeserializer();

    private UserCreateDoctorDeserializer() {}

    @Override
    public DoctorSignUp fromJson(JsonNode o) {
        if (!(o instanceof ObjectNode)) {
            throw new IllegalArgumentException();
        }

        ObjectNode jsonObject = (ObjectNode) o;

        User user = this.getUser(jsonObject);

        DoctorSignUp doctorSignUp = new DoctorSignUp();
        doctorSignUp.setUser(user);

        if (jsonObject.has("registrationNumber"))
            doctorSignUp.setRegistrationNumber(this.getIntegerNonNull(jsonObject, "registrationNumber"));

        Locality locality = new Locality();
        locality.setId(this.getIntegerNonNull(jsonObject, "localityId"));

        return doctorSignUp;
    }
}
