package ar.edu.itba.paw;

import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.persistence.CountryDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:sql/schema.sql")
@ContextConfiguration(classes = TestConfig.class)
public class CountryDaoImplTest
{
    private static final String NAME = "testName";
    private static final String STREET = "Av 9 de Julio";
    private static final String PROVINCE = "Buenos Aires";
    private static final String PHONE = "1234567890";
    private static final String EMAIL = "test@test.com";
    private static final int STREET_NUMBER = 123;
    private static final String COUNTRY = "Argentina";
    private static final String COUNTRY_ID = "AR";

    private static final String COUNTRIES_TABLE = "system_country";

    private CountryDaoImpl countryDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp(){
        this.countryDao = new CountryDaoImpl(this.ds);
        this.jdbcTemplate = new JdbcTemplate(this.ds);
        this.jdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(COUNTRIES_TABLE);

    }

    @Test
    public void testCreateCountry()
    {
        // 1. Precondiciones
        JdbcTestUtils.deleteFromTables(this.jdbcTemplate, COUNTRIES_TABLE);
        Country c = new Country();
        c.setName(COUNTRY);
        c.setId(COUNTRY_ID);
        c.setProvinces(Collections.emptyList());

        // 2. Ejercitar
        Country country = this.countryDao.create(c);

        // 3. Postcondiciones
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, COUNTRIES_TABLE));
    }
}
