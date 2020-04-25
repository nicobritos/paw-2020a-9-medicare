package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.interfaces.daos.generic.GenericDao;
import ar.edu.itba.paw.models.AppointmentTimeSlot;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.Workday;
import ar.edu.itba.paw.models.WorkdayDay;

import java.util.List;

public interface WorkdayDao extends GenericDao<Workday, Integer> {
    List<Workday> findByStaff(Staff staff);

    List<Workday> findByStaff(Staff staff, WorkdayDay day);

    boolean isStaffWorking(Staff staff, AppointmentTimeSlot timeSlot);
}
