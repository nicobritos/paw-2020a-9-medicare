package ar.edu.itba.paw.webapp.transformer;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentTimeSlot;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AppointmentTimeSlotTransformer extends GenericTransformer<AppointmentTimeSlot> {
    @Override
    public Map<String, ?> transform(AppointmentTimeSlot timeSlot) {
        Map<String, Object> map = new HashMap<>();
        map.put("hour", timeSlot.getDate().getHourOfDay());
        map.put("minute", timeSlot.getDate().getMinuteOfDay());
        map.put("duration", Appointment.DURATION);
        return map;
    }

    @Override
    public Collection<Map<String, ?>> transform(Collection<AppointmentTimeSlot> appointmentTimeSlots) {
        Collection<Map<String, ?>> transformed = new LinkedList<>();

        Map<DateTime, List<Map<String, ?>>> transformedTimeSlotsPerDay = new HashMap<>();
        for (AppointmentTimeSlot appointmentTimeSlot : appointmentTimeSlots) {
            List<Map<String, ?>> transformedTimeSlots = transformedTimeSlotsPerDay.computeIfAbsent(appointmentTimeSlot.getDate(), k -> new LinkedList<>());
            transformedTimeSlots.add(this.transform(appointmentTimeSlot));
        }

        for (Map.Entry<DateTime, List<Map<String, ?>>> dateList : transformedTimeSlotsPerDay.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", this.transformDate(dateList.getKey()));
            map.put("timeslots", dateList.getValue());
            transformed.add(map);
        }

        return transformed;
    }

    private Map<String, ?> transformDate(DateTime date) {
        Map<String, Object> map = new HashMap<>();
        map.put("year", date.getYear());
        map.put("month", date.getMonthOfYear());
        map.put("day", date.getDayOfMonth());
        return map;
    }
}
