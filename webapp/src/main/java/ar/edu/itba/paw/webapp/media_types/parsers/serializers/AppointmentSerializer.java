package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.Appointment;
import org.joda.time.DateTimeZone;
import org.json.JSONObject;

public class AppointmentSerializer extends JsonSerializer<Appointment> {
    public static final AppointmentSerializer instance = new AppointmentSerializer();

    private AppointmentSerializer() {}

    @Override
    public Object toJson(Appointment appointment) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", appointment.getId());
        jsonObject.put("status", appointment.getAppointmentStatus().toString());
        jsonObject.put("date_from", appointment.getFromDate().toDateTime(DateTimeZone.UTC).getMillis()); // Epoch millis
        jsonObject.put("message", appointment.getMessage());
        jsonObject.put("motive", appointment.getMotive());
        jsonObject.put("patientId", appointment.getPatient().getId());
        jsonObject.put("staffId", appointment.getStaff().getId());

        return jsonObject;
    }
}
