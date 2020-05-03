package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.interfaces.daos.generic.GenericSearchableDao;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Optional;

public interface PatientDao extends GenericSearchableDao<Patient, Integer> {
    Optional<Patient> findByUserAndOffice(User user, Office office);

    List<Patient> findByUser(User user);

    void setUser(Patient patient, User user);

    void setOffice(Patient patient, Office office);
}
