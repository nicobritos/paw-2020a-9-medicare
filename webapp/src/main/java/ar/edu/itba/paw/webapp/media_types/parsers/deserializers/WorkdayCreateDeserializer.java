package ar.edu.itba.paw.webapp.media_types.parsers.deserializers;

import ar.edu.itba.paw.models.Workday;
import ar.edu.itba.paw.models.WorkdayDay;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class WorkdayCreateDeserializer extends JsonDeserializer<Workday> {
    public static final WorkdayCreateDeserializer instance = new WorkdayCreateDeserializer();

    private static final int MIN_HOUR = 0;
    private static final int MAX_HOUR = 23;
    private static final int MIN_MINUTE = 0;
    private static final int MAX_MINUTE = 59;

    private WorkdayCreateDeserializer() {}

    @Override
    public Workday fromJson(JsonNode o) {
        if (!(o instanceof ObjectNode)) {
            throw new IllegalArgumentException();
        }

        ObjectNode jsonObject = (ObjectNode) o;

        Workday workday = new Workday();
        JsonNode node = jsonObject.get("day");
        if (node != null && !node.isNull()) {
            try {
                workday.setDay(WorkdayDay.valueOf(node.asText()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException();
        }

        int value;
        ObjectNode startTime = (ObjectNode) jsonObject.get("start");
        ObjectNode endTime = (ObjectNode) jsonObject.get("end");

        node = startTime.get("hour");
        if (node == null)
            throw new IllegalArgumentException();
        value = node.asInt();
        if (value < MIN_HOUR || value > MAX_HOUR)
            throw new IllegalArgumentException();
        workday.setStartHour(value);

        node = startTime.get("minute");
        if (node == null)
            throw new IllegalArgumentException();
        value = node.asInt();
        if (value < MIN_MINUTE || value > MAX_MINUTE)
            throw new IllegalArgumentException();
        workday.setStartMinute(value);

        node = endTime.get("hour");
        if (node == null)
            throw new IllegalArgumentException();
        value = node.asInt();
        if (value < MIN_HOUR || value > MAX_HOUR)
            throw new IllegalArgumentException();
        workday.setEndHour(value);

        node = endTime.get("minute");
        if (node == null)
            throw new IllegalArgumentException();
        value = node.asInt();
        if (value < MIN_MINUTE || value > MAX_MINUTE)
            throw new IllegalArgumentException();
        workday.setEndMinute(value);

        return workday;
    }
}
