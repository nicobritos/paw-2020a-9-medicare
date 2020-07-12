package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.models.PatientSignUp;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class UserPatientDeserializer extends UserCreateDeserializer<PatientSignUp> {
    public static final UserPatientDeserializer instance = new UserPatientDeserializer();

    private UserPatientDeserializer() {}

    @Override
    public PatientSignUp fromJson(JsonNode o) {
        if (!(o instanceof ObjectNode)) {
            throw new IllegalArgumentException();
        }

        User user = this.getUser((ObjectNode) o);

        PatientSignUp patientSignUp = new PatientSignUp();
        patientSignUp.setUser(user);

        return patientSignUp;
    }
}
