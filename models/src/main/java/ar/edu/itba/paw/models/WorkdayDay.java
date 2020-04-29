package ar.edu.itba.paw.models;

import java.time.DayOfWeek;
import java.time.LocalDate;
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

    public static WorkdayDay from(LocalDate localDate) {
        if (localDate.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            return MONDAY;
        } else if (localDate.getDayOfWeek().equals(DayOfWeek.TUESDAY)) {
            return TUESDAY;
        } else if (localDate.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)) {
            return WEDNESDAY;
        } else if (localDate.getDayOfWeek().equals(DayOfWeek.THURSDAY)) {
            return THURSDAY;
        } else if (localDate.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
            return FRIDAY;
        } else if (localDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
            return SATURDAY;
        } else if (localDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            return SUNDAY;
        }
        return null;
    }
}
