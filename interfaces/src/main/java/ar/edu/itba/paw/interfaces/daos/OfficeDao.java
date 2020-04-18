package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.interfaces.daos.generic.GenericSearchableDao;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Province;

import java.util.Set;

public interface OfficeDao extends GenericSearchableDao<Office, Integer> {
    Set<Office> findByCountry(Country country);

    Set<Office> findByProvince(Province province);

    Set<Office> findByLocality(Locality locality);
}
