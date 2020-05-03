package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.interfaces.daos.generic.GenericSearchableDao;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface PatientDao extends GenericSearchableDao<Patient, Integer> {
    Optional<Patient> findByUserAndOffice(User user, Office office);

    Optional<Patient> findByUser(User user);
}
