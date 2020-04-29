package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.daos.PatientDao;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.generics.GenericSearchableServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientServiceImpl extends GenericSearchableServiceImpl<PatientDao, Patient, Integer> implements PatientService {
    @Autowired
    private PatientDao repository;

    @Override
    public Optional<Patient> findByUserAndOffice(User user, Office office) {
        return this.repository.findByUserAndOffice(user, office);
    }

    @Override
    protected PatientDao getRepository() {
        return this.repository;
    }
}
