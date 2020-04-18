package ar.edu.itba.paw;

import ar.edu.itba.paw.interfaces.daos.CountryDao;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.services.CountryServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

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
    public void testCreateCountry(){ // Este test no es necesario, solo se lo dejo a modo de ejemplo de como funciona mockito
        Country country = new Country();
        country.setId(ID);
        country.setName(NAME);
        when(countryDao.create(any())).thenReturn(country);
        Country c = new Country();
        c.setId(ID);
        c.setName(NAME);

        Country ans = countryService.create(c);

        Assert.assertEquals(c.getId(), ans.getId());
    }
}
