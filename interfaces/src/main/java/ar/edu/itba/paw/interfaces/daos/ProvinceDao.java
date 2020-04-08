package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.interfaces.daos.generic.GenericDao;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.models.Province;

import java.util.Collection;

public interface ProvinceDao extends GenericDao<Province, Integer> {
    Collection<Province> getByCountry(Country country);

    /**
     * Returns Provinces with a name similar to the one provided filtered out by Country.
     * The search is not case-sensitive nor exact
     * @param name the province's name
     * @return a collection of matched provinces
     */
    Collection<Province> getByCountryAndName(Country country, String name);
}
