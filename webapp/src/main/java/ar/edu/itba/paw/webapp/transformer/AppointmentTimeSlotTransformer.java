package ar.edu.itba.paw.webapp.transformer;

import ar.edu.itba.paw.models.AppointmentTimeSlot;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AppointmentTimeSlotTransformer extends GenericTransformer<AppointmentTimeSlot> {
    @Override
    public Map<String, ?> transform(AppointmentTimeSlot timeSlot) {
        Map<String, Object> map = new HashMap<>();
        map.put("day", timeSlot.getDay());
        map.put("hour", timeSlot.getFromHour());
        map.put("minute", timeSlot.getFromMinute());
        map.put("duration", timeSlot.getDuration());
        return map;
    }
}
