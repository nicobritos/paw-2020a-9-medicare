package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.generic.GenericSearchableService;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.models.Office;

import java.util.Collection;

public interface OfficeService extends GenericSearchableService<Office, Integer> {
    Collection<Office> findByCountry(Country country);
}
