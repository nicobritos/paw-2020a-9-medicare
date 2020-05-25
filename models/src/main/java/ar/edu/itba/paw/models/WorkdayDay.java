package ar.edu.itba.paw.models;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.Calendar;

public enum WorkdayDay {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    public static WorkdayDay from(Calendar calendar) {
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return MONDAY;
            case Calendar.TUESDAY:
                return TUESDAY;
            case Calendar.WEDNESDAY:
                return WEDNESDAY;
            case Calendar.THURSDAY:
                return THURSDAY;
            case Calendar.FRIDAY:
                return FRIDAY;
            case Calendar.SATURDAY:
                return SATURDAY;
            case Calendar.SUNDAY:
                return SUNDAY;
            default:
                return null;
        }
    }

    public static WorkdayDay from(DateTime localDate) {
        if (localDate.getDayOfWeek() == DateTimeConstants.MONDAY) {
            return MONDAY;
        } else if (localDate.getDayOfWeek() == DateTimeConstants.TUESDAY) {
            return TUESDAY;
        } else if (localDate.getDayOfWeek() == DateTimeConstants.WEDNESDAY) {
            return WEDNESDAY;
        } else if (localDate.getDayOfWeek() == DateTimeConstants.THURSDAY) {
            return THURSDAY;
        } else if (localDate.getDayOfWeek() == DateTimeConstants.FRIDAY) {
            return FRIDAY;
        } else if (localDate.getDayOfWeek() == DateTimeConstants.SATURDAY) {
            return SATURDAY;
        } else if (localDate.getDayOfWeek() == DateTimeConstants.SUNDAY) {
            return SUNDAY;
        }
        return null;
    }
}
