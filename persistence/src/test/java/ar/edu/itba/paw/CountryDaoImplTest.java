package ar.edu.itba.paw;

import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.models.Province;
import ar.edu.itba.paw.persistence.CountryDaoImpl;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder;
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
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:sql/schema.sql")
@ContextConfiguration(classes = TestConfig.class)
public class CountryDaoImplTest
{
    private static final String COUNTRY = "Argentina";
    private static final String COUNTRY_ID = "AR";
    private static final String PROVINCE = "Buenos Aires";

    private static final String COUNTRIES_TABLE = "system_country";
    private static final String PROVINCE_TABLE = "system_province";

    private CountryDaoImpl countryDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private SimpleJdbcInsert provinceJdbcInsert;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp(){
        this.countryDao = new CountryDaoImpl(this.ds);
        this.jdbcTemplate = new JdbcTemplate(this.ds);
        this.jdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(COUNTRIES_TABLE);
        this.provinceJdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(PROVINCE_TABLE)
                .usingGeneratedKeyColumns("province_id");

    }

    private void cleanAllTables(){
        this.jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
    }

    private Country countryModel(){
        Country c = new Country();
        c.setName(COUNTRY);
        c.setId(COUNTRY_ID);
        c.setProvinces(Collections.emptyList());
        return c;
    }

    private Province provinceModel() {
        Province p = new Province();
        p.setId(0);
        p.setName(PROVINCE);
        return p;
    }

    private void insertCountry() {
        Map<String, Object> map = new HashMap<>();
        map.put("country_id", COUNTRY_ID);
        map.put("name", COUNTRY);
        jdbcInsert.execute(map);
    }

    private void insertAnotherCountry() {
        Map<String, Object> map = new HashMap<>();
        map.put("country_id", "BR");
        map.put("name", "Brasil");
        jdbcInsert.execute(map);
    }

    @Test
    public void testCreateCountry()
    {
        // 1. Precondiciones
        cleanAllTables();
        Country c = countryModel();

        // 2. Ejercitar
        Country country = this.countryDao.create(c);

        // 3. Postcondiciones
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, COUNTRIES_TABLE));
        assertEquals(COUNTRY, country.getName());
        assertEquals(COUNTRY_ID, country.getId());
        assertTrue(country.getProvinces().isEmpty());
    }

    @Test
    public void testFindCountryById()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();

        // 2. Ejercitar
        Optional<Country> country = this.countryDao.findById(COUNTRY_ID);

        // 3. Postcondiciones
        assertTrue(country.isPresent());
        assertEquals(COUNTRY, country.get().getName());
    }

    @Test
    public void testFindCountryByIdDoesntExist()
    {
        // 1. Precondiciones
        cleanAllTables();
        
        // 2. Ejercitar
        Optional<Country> country = this.countryDao.findById(COUNTRY_ID);

        // 3. Postcondiciones
        assertFalse(country.isPresent());
    }

    @Test
    public void testFindCountryByField()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();

        // 2. Ejercitar
        List<Country> countries = this.countryDao.findByField("name", COUNTRY);

        // 3. Postcondiciones
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
        assertEquals(COUNTRY_ID, countries.get(0).getId());
    }

    @Test
    public void testFindCountryByFieldDoesntExist()
    {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        List<Country> countries = this.countryDao.findByField("name", COUNTRY);

        // 3. Postcondiciones
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

    @Test
    public void testFindCountryByFieldOp()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();

        // 2. Ejercitar
        List<Country> countries = this.countryDao.findByField("name", JDBCWhereClauseBuilder.Operation.EQ, COUNTRY);

        // 3. Postcondiciones
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
        assertEquals(COUNTRY_ID, countries.get(0).getId());
    }

    @Test
    public void testFindCountryByFieldOpDoesntExist()
    {
        // 1. Precondiciones
        cleanAllTables();// 2. Ejercitar
        List<Country> countries = this.countryDao.findByField("name", JDBCWhereClauseBuilder.Operation.EQ, COUNTRY);

        // 3. Postcondiciones
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

    @Test
    public void testFindCountryByIds()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        insertAnotherCountry();

        // 2. Ejercitar
        Collection<Country> countries = this.countryDao.findByIds(Arrays.asList(COUNTRY_ID, "BR"));

        // 3. Postcondiciones
        assertNotNull(countries);
        assertEquals(2, countries.size());
        for (Country c : countries){
            assertTrue(c.getId().equals(COUNTRY_ID) || c.getId().equals("BR"));
        }
    }

    @Test
    public void testFindCountryByIdsDoesntExist()
    {
        // 1. Precondiciones
        cleanAllTables();// 2. Ejercitar
        Collection<Country> countries = this.countryDao.findByIds(Arrays.asList(COUNTRY, "BR"));

        // 3. Postcondiciones
        assertNotNull(countries);
        assertTrue(countries.isEmpty());

    }

    @Test
    public void testFindCountryByName()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();

        // 2. Ejercitar
        Collection<Country> countries = this.countryDao.findByName(COUNTRY);

        // 3. Postcondiciones
        assertNotNull(countries);
        assertEquals(1, countries.size());
        for (Country c : countries){
            assertEquals(COUNTRY, c.getName());
        }
    }

    @Test
    public void testFindCountryByNameDoesntExist()
    {
        // 1. Precondiciones
        cleanAllTables();// 2. Ejercitar
        Collection<Country> countries = this.countryDao.findByName(COUNTRY);

        // 3. Postcondiciones
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

    @Test
    public void testCountryList()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        insertAnotherCountry();

        // 2. Ejercitar
        Collection<Country> countries = this.countryDao.list();

        // 3. Postcondiciones
        assertNotNull(countries);
        assertEquals(2, countries.size());
    }

    @Test
    public void testCountryEmptyList()
    {
        // 1. Precondiciones
        cleanAllTables();// 2. Ejercitar
        Collection<Country> countries = this.countryDao.list();

        // 3. Postcondiciones
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

    @Test
    public void testCountryUpdate()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        Country c = new Country();
        c.setId("AR");
        c.setName("Armenia");
        c.setProvinces(Collections.emptyList());

        // 2. Ejercitar
        this.countryDao.update(c);

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, COUNTRIES_TABLE));
        assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, COUNTRIES_TABLE, "name = 'Armenia'"));
        assertEquals(0,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, COUNTRIES_TABLE, "name = '"+ COUNTRY +"'"));

    }

    @Test
    public void testCountryUpdateProvinces()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        insertAnotherCountry();
        Map<String, Object> provinceMap = new HashMap<>();
        provinceMap.put("name", PROVINCE);
        provinceMap.put("country_id", "BR");
        provinceJdbcInsert.execute(provinceMap);

        Country c = new Country();
        c.setId(COUNTRY_ID);
        c.setName(COUNTRY);
        c.setProvinces(Collections.singletonList(provinceModel()));

        // 2. Ejercitar
        this.countryDao.update(c);

        // 3. Postcondiciones
        assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, COUNTRIES_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, PROVINCE_TABLE, "country_id = '"+ COUNTRY_ID +"'"));
    }

    @Test
    public void testCountryRemoveById()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();

        // 2. Ejercitar
        this.countryDao.remove(COUNTRY_ID);

        // 3. Postcondiciones
        assertEquals(0,JdbcTestUtils.countRowsInTable(jdbcTemplate, COUNTRIES_TABLE));
    }

    @Test
    public void testCountryRemoveByModel()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        Country c = countryModel();

        // 2. Ejercitar
        this.countryDao.remove(c);

        // 3. Postcondiciones
        assertEquals(0,JdbcTestUtils.countRowsInTable(jdbcTemplate, COUNTRIES_TABLE));
    }
}
