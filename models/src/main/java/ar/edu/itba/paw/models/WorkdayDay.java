package ar.edu.itba.paw.models;

import java.util.Calendar;

public enum WorkdayDay {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    public static WorkdayDay fromCalendar(Calendar calendar) {
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY: return MONDAY;
            case Calendar.TUESDAY: return TUESDAY;
            case Calendar.WEDNESDAY: return WEDNESDAY;
            case Calendar.THURSDAY: return THURSDAY;
            case Calendar.FRIDAY: return FRIDAY;
            case Calendar.SATURDAY: return SATURDAY;
            case Calendar.SUNDAY: return SUNDAY;
            default: return null;
        }
    }
}
