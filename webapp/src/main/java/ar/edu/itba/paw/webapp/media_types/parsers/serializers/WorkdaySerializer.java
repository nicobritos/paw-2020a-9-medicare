package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import ar.edu.itba.paw.models.Workday;
import org.json.JSONObject;

public class WorkdaySerializer extends JsonSerializer<Workday> {
    public static final WorkdaySerializer instance = new WorkdaySerializer();

    private WorkdaySerializer() {}

    @Override
    public Object toJson(Workday workday) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", workday.getId());
        jsonObject.put("start", this.timeToJson(workday.getStartHour(), workday.getStartMinute()));
        jsonObject.put("end", this.timeToJson(workday.getEndHour(), workday.getEndMinute()));
        jsonObject.put("day", workday.getDay().name());
        jsonObject.put("staffId", workday.getStaff().getId());

        return jsonObject;
    }

    private JSONObject timeToJson(int hour, int minute) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("hour", hour);
        jsonObject.put("minute", minute);

        return jsonObject;
    }
}
