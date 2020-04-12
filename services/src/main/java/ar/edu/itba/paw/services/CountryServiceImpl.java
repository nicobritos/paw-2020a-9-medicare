package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.daos.CountryDao;
import ar.edu.itba.paw.interfaces.services.CountryService;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.services.generics.GenericSearchableListableServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountryServiceImpl extends GenericSearchableListableServiceImpl<Country,String> implements CountryService {

    @Autowired
    CountryDao repository;
}
