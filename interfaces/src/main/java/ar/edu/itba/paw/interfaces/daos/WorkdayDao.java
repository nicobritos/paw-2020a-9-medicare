package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.interfaces.daos.generic.GenericDao;
import ar.edu.itba.paw.models.*;

import java.util.List;

public interface WorkdayDao extends GenericDao<Workday, Integer> {
    List<Workday> findByUser(User user);

    List<Workday> findByStaff(Staff staff);

    List<Workday> findByStaff(Staff staff, WorkdayDay day);

    boolean isStaffWorking(Staff staff, AppointmentTimeSlot timeSlot);

    void setStaff(Workday workday, Staff staff);
}
