package ar.edu.itba.paw;

import ar.edu.itba.paw.interfaces.daos.CountryDao;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.services.CountryServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class ServicesTest
{
    private final static String NAME = "Argentina";
    private final static String ID = "AR";

    @InjectMocks
    private CountryServiceImpl countryService;

    @Mock
    private CountryDao countryDao;

    @Test
    public void testCreateCountry(){
        Country country = new Country();
        country.setId(ID);
        country.setName(NAME);
        country.setProvinces(Collections.emptyList());
        Mockito.when(countryDao.create(Mockito.any())).thenReturn(country);
    }
}
