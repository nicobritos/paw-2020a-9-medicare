package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.daos.StaffSpecialtyDao;
import ar.edu.itba.paw.interfaces.services.StaffSpecialtyService;
import ar.edu.itba.paw.models.StaffSpecialty;
import ar.edu.itba.paw.services.generics.GenericSearchableServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaffSpecialtyServiceImpl extends GenericSearchableServiceImpl<StaffSpecialtyDao, StaffSpecialty,Integer> implements StaffSpecialtyService {
    @Autowired
    public StaffSpecialtyServiceImpl(StaffSpecialtyDao repository) {
        super(repository);
    }
}
