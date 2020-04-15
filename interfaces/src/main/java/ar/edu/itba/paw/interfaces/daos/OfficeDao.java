package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.interfaces.daos.generic.GenericSearchableDao;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Province;

import java.util.Collection;

public interface OfficeDao extends GenericSearchableDao<Office, Integer> {
    Collection<Office> findByCountry(Country country);

    Collection<Office> findByProvince(Province province);

    Collection<Office> findByLocality(Locality locality);
}
