package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.generic.GenericSearchableService;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.models.Province;

import java.util.Collection;

public interface CountryService extends GenericSearchableService<Country, String> {
    void addProvince(Country country, Province province);

    void addProvinces(Country country, Collection<Province> provinces);
}
