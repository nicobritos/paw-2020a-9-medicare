package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.daos.ProvinceDao;
import ar.edu.itba.paw.interfaces.services.ProvinceService;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.models.Province;
import ar.edu.itba.paw.services.generics.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ProvinceServiceImpl extends GenericServiceImpl<ProvinceDao, Province, Integer> implements ProvinceService {
    @Autowired
    public ProvinceServiceImpl(ProvinceDao repository) {
        super(repository);
    }

    @Override
    public Collection<Province> findByCountry(Country country) {
        return this.repository.findByCountry(country);
    }

    @Override
    public Collection<Province> findByCountryAndName(Country country, String name) {
        return this.repository.findByCountryAndName(country, name);
    }
}
