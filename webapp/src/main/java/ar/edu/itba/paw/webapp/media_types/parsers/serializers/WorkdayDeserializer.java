package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.Workday;
import ar.edu.itba.paw.models.WorkdayDay;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class WorkdayDeserializer extends JsonDeserializer<Workday> {
    public static final WorkdayDeserializer instance = new WorkdayDeserializer();

    private static final int MIN_HOUR = 0;
    private static final int MAX_HOUR = 23;
    private static final int MIN_MINUTE = 0;
    private static final int MAX_MINUTE = 59;

    private WorkdayDeserializer() {}

    @Override
    public Workday fromJson(JsonNode o) {
        if (!(o instanceof ObjectNode)) {
            throw new IllegalArgumentException();
        }

        ObjectNode jsonObject = (ObjectNode) o;
        JsonNode node = jsonObject.get("day");

        Workday workday = new Workday();
        if (node != null && !node.isNull()) {
            workday.setDay(WorkdayDay.valueOf(node.asText()));
        }

        int v;
        ObjectNode startTime = (ObjectNode) jsonObject.get("start");
        ObjectNode endTime = (ObjectNode) jsonObject.get("end");

        node = startTime.get("hour");
        if (node == null)
            throw new IllegalArgumentException();
        v = node.asInt();
        if (v < MIN_HOUR || v > MAX_HOUR)
            throw new IllegalArgumentException();
        workday.setStartHour(v);

        node = startTime.get("minute");
        if (node == null)
            throw new IllegalArgumentException();
        v = node.asInt();
        if (v < MIN_MINUTE || v > MAX_MINUTE)
            throw new IllegalArgumentException();
        workday.setStartMinute(v);

        node = endTime.get("hour");
        if (node == null)
            throw new IllegalArgumentException();
        v = node.asInt();
        if (v < MIN_HOUR || v > MAX_HOUR)
            throw new IllegalArgumentException();
        workday.setEndHour(v);

        node = endTime.get("minute");
        if (node == null)
            throw new IllegalArgumentException();
        v = node.asInt();
        if (v < MIN_MINUTE || v > MAX_MINUTE)
            throw new IllegalArgumentException();
        workday.setEndMinute(v);

        return workday;
    }
}
