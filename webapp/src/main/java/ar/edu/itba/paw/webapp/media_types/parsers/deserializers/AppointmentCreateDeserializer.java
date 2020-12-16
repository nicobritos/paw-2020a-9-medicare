package ar.edu.itba.paw.webapp.media_types.parsers.deserializers;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.webapp.exceptions.UnprocessableEntityException;
import ar.edu.itba.paw.webapp.models.error.ErrorConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.joda.time.LocalDateTime;

import javax.ws.rs.BadRequestException;

public class AppointmentCreateDeserializer extends JsonDeserializer<Appointment> {
    public static final AppointmentCreateDeserializer instance = new AppointmentCreateDeserializer();

    private AppointmentCreateDeserializer() {}

    @Override
    public Appointment fromJson(JsonNode o) {
        if (!(o instanceof ObjectNode)) {
            throw new BadRequestException();
        }

        ObjectNode jsonObject = (ObjectNode) o;
        JsonNode node;

        Appointment appointment = new Appointment();

        node = jsonObject.get("date_from");
        if (node == null) {
            throw UnprocessableEntityException
                    .build()
                    .withReason(ErrorConstants.APPOINTMENT_CREATE_MISSING_DATE_FROM)
                    .getError();
        } else if (node.isNull() || !node.isLong()) {
            throw UnprocessableEntityException
                    .build()
                    .withReason(ErrorConstants.APPOINTMENT_CREATE_INVALID_DATE_FROM)
                    .getError();
        }

        try {
            appointment.setFromDate(new LocalDateTime(node.asLong())); // epoch millis
        } catch (Exception e) {
            throw UnprocessableEntityException
                    .build()
                    .withReason(ErrorConstants.APPOINTMENT_CREATE_INVALID_DATE_FROM)
                    .getError();
        }

        String s = this.getStringNull(jsonObject, "motive", ErrorConstants.APPOINTMENT_CREATE_INVALID_MOTIVE);
        if (s != null) appointment.setMotive(s);

        s = this.getStringNull(jsonObject, "message", ErrorConstants.APPOINTMENT_CREATE_INVALID_MESSAGE);
        if (s != null) appointment.setMessage(s);

        Doctor doctor = new Doctor();

        doctor.setId(this.getIntegerNonNull(
                jsonObject,
                "doctorId",
                ErrorConstants.APPOINTMENT_CREATE_MISSING_DOCTOR_ID,
                ErrorConstants.APPOINTMENT_CREATE_INVALID_DOCTOR_ID
        ));
        appointment.setDoctor(doctor);

        return appointment;
    }
}
