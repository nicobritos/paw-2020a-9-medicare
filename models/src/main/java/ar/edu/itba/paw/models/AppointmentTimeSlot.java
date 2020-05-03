package ar.edu.itba.paw.models;

import org.joda.time.DateTime;

import java.util.Objects;

public class AppointmentTimeSlot {
    private DateTime date;

    public DateTime getDate() {
        return this.date;
    }

    public DateTime getToDate() {
        return this.date.plusMinutes(Appointment.DURATION);
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppointmentTimeSlot)) return false;
        AppointmentTimeSlot that = (AppointmentTimeSlot) o;
        if (this.date == null && that.date != null) return false;
        if (this.date != null && that.date == null) return false;
        if (that.date == null) return false;
        return this.date.toInstant().getMillis() == that.date.toInstant().getMillis();
    }

    @Override
    public int hashCode() {
        if (this.date == null)
            return Objects.hashCode(null);
        return Objects.hashCode(this.date.toInstant().getMillis());
    }
}
