package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Staff;
import org.joda.time.LocalDateTime;
import org.json.JSONObject;

public class AppointmentDeserializer extends JsonDeserializer<Appointment> {
    public static final AppointmentDeserializer instance = new AppointmentDeserializer();

    private AppointmentDeserializer() {}

    @Override
    public Appointment fromJson(Object o) {
        if (!(o instanceof JSONObject)) {
            throw new IllegalArgumentException();
        }

        JSONObject jsonObject = (JSONObject) o;

        Appointment appointment = new Appointment();
        appointment.setFromDate(new LocalDateTime(jsonObject.getLong("date_from"))); // epoch millis
        if (jsonObject.has("motive"))
            appointment.setMotive(jsonObject.getString("motive"));
        if (jsonObject.has("message"))
            appointment.setMotive(jsonObject.getString("message"));

        Staff staff = new Staff();
        staff.setId(jsonObject.getInt("staffId"));
        appointment.setStaff(staff);

        return appointment;
    }
}
