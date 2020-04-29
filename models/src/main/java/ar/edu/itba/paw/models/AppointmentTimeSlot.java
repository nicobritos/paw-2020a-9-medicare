package ar.edu.itba.paw.models;

import java.util.Objects;

public class AppointmentTimeSlot {
    private String day;
    private int hour;
    private int minute;
    private int duration;

    public String getDay() {
        return this.day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getFromHour() {
        return this.hour;
    }

    public void setFromHour(int hour) {
        this.hour = hour;
    }

    public int getFromMinute() {
        return this.minute;
    }

    public void setFromMinute(int minute) {
        this.minute = minute;
    }

    public int getToHour() {
        return this.hour + Math.abs((this.minute + this.duration) / 60);
    }

    public int getToMinute() {
        return (this.minute + this.duration) % 60;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppointmentTimeSlot)) return false;
        AppointmentTimeSlot that = (AppointmentTimeSlot) o;
        return this.getToHour() == that.getToHour() &&
                this.getToMinute() == that.getToMinute() &&
                this.getDuration() == that.getDuration() &&
                Objects.equals(this.getDay(), that.getDay());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getDay(), this.getToHour(), this.getToMinute(), this.getDuration());
    }
}
