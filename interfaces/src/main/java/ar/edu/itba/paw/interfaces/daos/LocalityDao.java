package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.interfaces.daos.generic.GenericSearchableDao;
import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Province;

import java.util.Set;

public interface LocalityDao extends GenericSearchableDao<Locality, Integer> {
    Set<Locality> findByProvince(Province province);

    /**
     * Returns Provinces with a name similar to the one provided filtered out by Country.
     * The search is not case-sensitive nor exact
     * @param name the province's name
     * @return a collection of matched provinces
     */
    Set<Locality> findByProvinceAndName(Province province, String name);
}
