package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.daos.OfficeDao;
import ar.edu.itba.paw.interfaces.services.OfficeService;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.services.generics.GenericSearchableServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class OfficeServiceImpl extends GenericSearchableServiceImpl<OfficeDao, Office, Integer> implements OfficeService {
    public OfficeServiceImpl(OfficeDao officeDao) {
        super(officeDao);
    }

    @Override
    public Collection<Office> findByCountry(Country country) {
        return this.repository.findByCountry(country);
    }
}
