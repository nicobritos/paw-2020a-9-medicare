package ar.edu.itba.paw.webapp.media_types.parsers.deserializers;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Doctor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.joda.time.LocalDateTime;

public class AppointmentCreateDeserializer extends JsonDeserializer<Appointment> {
    public static final AppointmentCreateDeserializer instance = new AppointmentCreateDeserializer();

    private AppointmentCreateDeserializer() {}

    @Override
    public Appointment fromJson(JsonNode o) {
        if (!(o instanceof ObjectNode)) {
            throw new IllegalArgumentException();
        }

        ObjectNode jsonObject = (ObjectNode) o;
        JsonNode node;

        Appointment appointment = new Appointment();

        node = jsonObject.get("date_from");
        if (node == null || node.isNull())
            throw new IllegalArgumentException();
        appointment.setFromDate(new LocalDateTime(node.asLong())); // epoch millis

        node = jsonObject.get("motive");
        if (node != null && !node.isNull()) {
            appointment.setMotive(node.asText());
        }
        node = jsonObject.get("message");
        if (node != null && !node.isNull()) {
            appointment.setMotive(node.asText());
        }

        Doctor doctor = new Doctor();

        node = jsonObject.get("doctorId");
        if (node == null || node.isNull())
            throw new IllegalArgumentException();
        doctor.setId(node.asInt());
        appointment.setDoctor(doctor);

        return appointment;
    }
}
