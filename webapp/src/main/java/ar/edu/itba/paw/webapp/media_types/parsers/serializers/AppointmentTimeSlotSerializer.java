package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentTimeSlot;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class AppointmentTimeSlotSerializer extends JsonSerializer<AppointmentTimeSlot> {
    public static final AppointmentTimeSlotSerializer instance = new AppointmentTimeSlotSerializer();

    private AppointmentTimeSlotSerializer() {}

    @Override
    public Object toJson(AppointmentTimeSlot timeSlot) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("hour", timeSlot.getDate().getHourOfDay());
        jsonObject.put("minute", timeSlot.getDate().getMinuteOfHour());
        jsonObject.put("duration", Appointment.DURATION);

        return jsonObject;
    }

    @Override
    public JSONArray toJsonArray(Collection<AppointmentTimeSlot> appointmentTimeSlots) {
        JSONArray jsonArray = new JSONArray();

        Map<LocalDate, List<Object>> timeSlotsPerDay = new HashMap<>();
        for (AppointmentTimeSlot appointmentTimeSlot : appointmentTimeSlots) {
            List<Object> transformedTimeSlots = timeSlotsPerDay.computeIfAbsent(appointmentTimeSlot.getDate().toLocalDate(), k -> new LinkedList<>());
            transformedTimeSlots.add(this.toJson(appointmentTimeSlot));
        }

        for (Map.Entry<LocalDate, List<Object>> dateList : timeSlotsPerDay.entrySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("date", this.transformDate(dateList.getKey()));
            jsonObject.put("timeslots", dateList.getValue());
            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }

    private JSONObject transformDate(LocalDate date) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("year", date.getYear());
        jsonObject.put("month", date.getMonthOfYear());
        jsonObject.put("day", date.getDayOfMonth());

        return jsonObject;
    }
}
