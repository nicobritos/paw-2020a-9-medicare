package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.interfaces.daos.generic.GenericDao;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.Workday;

import java.util.Set;

public interface WorkdayDao extends GenericDao<Workday, Integer> {
    Set<Workday> findByStaff(Staff staff);
}
