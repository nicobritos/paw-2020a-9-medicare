package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Staff;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.joda.time.LocalDateTime;

public class AppointmentDeserializer extends JsonDeserializer<Appointment> {
    public static final AppointmentDeserializer instance = new AppointmentDeserializer();

    private AppointmentDeserializer() {}

    @Override
    public Appointment fromJson(JsonNode o) {
        if (!(o instanceof ObjectNode)) {
            throw new IllegalArgumentException();
        }

        ObjectNode jsonObject = (ObjectNode) o;
        JsonNode node;

        Appointment appointment = new Appointment();

        node = jsonObject.get("date_from");
        if (node.isNull())
            throw new IllegalArgumentException();
        appointment.setFromDate(new LocalDateTime(node.asLong())); // epoch millis

        node = jsonObject.get("motive");
        if (!node.isNull()) {
            appointment.setMotive(node.asText());
        }
        node = jsonObject.get("message");
        if (!node.isNull()) {
            appointment.setMotive(node.asText());
        }

        Staff staff = new Staff();

        node = jsonObject.get("staffId");
        if (node.isNull())
            throw new IllegalArgumentException();
        staff.setId(node.asInt());
        appointment.setStaff(staff);

        return appointment;
    }
}
