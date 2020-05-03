package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.generic.GenericSearchableService;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface PatientService extends GenericSearchableService<Patient, Integer> {
    Optional<Patient> findByUserAndOffice(User user, Office office);

    Optional<Patient> findByUser(User user);
}
