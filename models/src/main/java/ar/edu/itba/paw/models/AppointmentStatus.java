package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Table;

@Table(name = "appointment_status", primaryKey = "appointment_status_id")
public class AppointmentStatus extends GenericModel<AppointmentStatus, String> {
    @Override
    protected boolean isSameInstance(Object o) {
        return o instanceof AppointmentStatus;
    }
}
