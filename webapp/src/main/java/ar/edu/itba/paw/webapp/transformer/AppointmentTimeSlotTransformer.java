package ar.edu.itba.paw.webapp.transformer;

import ar.edu.itba.paw.models.AppointmentTimeSlot;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class AppointmentTimeSlotTransformer extends GenericTransformer<AppointmentTimeSlot> {
    @Override
    public Map<String, ?> transform(AppointmentTimeSlot timeSlot) {
        Map<String, Object> map = new HashMap<>();
        map.put("hour", timeSlot.getFromHour());
        map.put("minute", timeSlot.getFromMinute());
        map.put("duration", timeSlot.getDuration());
        return map;
    }

    @Override
    public Collection<Map<String, ?>> transform(Collection<AppointmentTimeSlot> appointmentTimeSlots) {
        Collection<Map<String, ?>> transformed = new LinkedList<>();

        Map<LocalDate, List<Map<String, ?>>> transformedTimeSlotsPerDay = new HashMap<>();
        for (AppointmentTimeSlot appointmentTimeSlot : appointmentTimeSlots) {
            List<Map<String, ?>> transformedTimeSlots = transformedTimeSlotsPerDay.computeIfAbsent(appointmentTimeSlot.getDate(), k -> new LinkedList<>());
            transformedTimeSlots.add(this.transform(appointmentTimeSlot));
        }

        for (Map.Entry<LocalDate, List<Map<String, ?>>> dateList : transformedTimeSlotsPerDay.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", this.transformDate(dateList.getKey()));
            map.put("timeslots", dateList.getValue());
            transformed.add(map);
        }

        return transformed;
    }

    private Map<String, ?> transformDate(LocalDate date) {
        Map<String, Object> map = new HashMap<>();
        map.put("year", date.getYear());
        map.put("month", date.getMonthValue());
        map.put("day", date.getDayOfMonth());
        return map;
    }
}
