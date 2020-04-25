package ar.edu.itba.paw.models;

import java.util.Objects;

public class AppointmentTimeSlot {
    private WorkdayDay day;
    private int fromHour;
    private int fromMinute;
    private int toHour;
    private int toMinute;

    public WorkdayDay getDay() {
        return this.day;
    }

    public void setDay(WorkdayDay day) {
        this.day = day;
    }

    public int getFromHour() {
        return this.fromHour;
    }

    public void setFromHour(int fromHour) {
        this.fromHour = fromHour;
    }

    public int getFromMinute() {
        return this.fromMinute;
    }

    public void setFromMinute(int fromMinute) {
        this.fromMinute = fromMinute;
    }

    public int getToHour() {
        return this.toHour;
    }

    public void setToHour(int toHour) {
        this.toHour = toHour;
    }

    public int getToMinute() {
        return this.toMinute;
    }

    public void setToMinute(int toMinute) {
        this.toMinute = toMinute;
    }

    public int getDuration() {
        return (this.toHour - this.fromHour) + (this.toMinute - this.fromMinute);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppointmentTimeSlot)) return false;
        AppointmentTimeSlot that = (AppointmentTimeSlot) o;
        return this.getFromHour() == that.getFromHour() &&
                this.getFromMinute() == that.getFromMinute() &&
                this.getToHour() == that.getToHour() &&
                this.getToMinute() == that.getToMinute() &&
                this.getDay() == that.getDay();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getDay(), this.getFromHour(), this.getFromMinute(), this.getToHour(), this.getToMinute());
    }
}
