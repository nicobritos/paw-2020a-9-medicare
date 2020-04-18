package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.daos.ProvinceDao;
import ar.edu.itba.paw.interfaces.services.ProvinceService;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Province;
import ar.edu.itba.paw.services.generics.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ProvinceServiceImpl extends GenericServiceImpl<ProvinceDao, Province, Integer> implements ProvinceService {
    @Autowired
    private ProvinceDao repository;

    @Override
    public Set<Province> findByCountry(Country country) {
        return this.repository.findByCountry(country);
    }

    @Override
    public Set<Province> findByCountryAndName(Country country, String name) {
        return this.repository.findByCountryAndName(country, name);
    }

    @Override
    public void addLocality(Province province, Locality locality) {
        province.getLocalities().add(locality);
        this.repository.update(province);
    }

    @Override
    public void addLocalities(Province province, Set<Locality> localities) {
        province.getLocalities().addAll(localities);
        this.repository.update(province);
    }

    @Override
    protected ProvinceDao getRepository() {
        return this.repository;
    }
}
