package ar.edu.itba.paw.models;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDateTime;

import java.util.Calendar;

public enum WorkdayDay {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(0);

    private int dow;

    WorkdayDay(int dow) {
        this.dow = dow;
    }

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

    public static WorkdayDay from(LocalDateTime localDate) {
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

    public int toInteger() {
        return this.dow;
    }
}
