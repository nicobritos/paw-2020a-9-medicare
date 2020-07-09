package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.Workday;
import ar.edu.itba.paw.models.WorkdayDay;
import org.json.JSONObject;

public class WorkdayDeserializer extends JsonDeserializer<Workday> {
    public static final WorkdayDeserializer instance = new WorkdayDeserializer();

    private static final int MIN_HOUR = 0;
    private static final int MAX_HOUR = 23;
    private static final int MIN_MINUTE = 0;
    private static final int MAX_MINUTE = 59;

    private WorkdayDeserializer() {}

    @Override
    public Workday fromJson(Object o) {
        if (!(o instanceof JSONObject)) {
            throw new IllegalArgumentException();
        }

        JSONObject jsonObject = (JSONObject) o;

        Workday workday = new Workday();
        workday.setDay(WorkdayDay.valueOf(jsonObject.getString("day")));

        int v;
        JSONObject startTime = jsonObject.getJSONObject("start");
        JSONObject endTime = jsonObject.getJSONObject("end");

        v = startTime.getInt("hour");
        if (v < MIN_HOUR || v > MAX_HOUR)
            throw new IllegalArgumentException();
        workday.setStartHour(v);

        v = startTime.getInt("minute");
        if (v < MIN_MINUTE || v > MAX_MINUTE)
            throw new IllegalArgumentException();
        workday.setStartMinute(v);

        v = endTime.getInt("hour");
        if (v < MIN_HOUR || v > MAX_HOUR)
            throw new IllegalArgumentException();
        workday.setEndHour(v);

        v = endTime.getInt("minute");
        if (v < MIN_MINUTE || v > MAX_MINUTE)
            throw new IllegalArgumentException();
        workday.setEndMinute(v);

        return workday;
    }
}
