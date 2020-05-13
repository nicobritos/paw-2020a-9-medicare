package ar.edu.itba.paw;

import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.persistence.OfficeDaoImpl;
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
public class OfficeDaoImplTest
{
    private static final String NAME = "Hospital Nacional";
    public static final String NAME_2 = NAME + "_1";
    private static final String STREET = "Av 9 de Julio";
    private static final String PROVINCE = "Buenos Aires";
    private static final String LOCALITY = "Capital Federal";
    private static final String PHONE = "1234567890";
    private static final String EMAIL = "test@test.com";
    private static final int STREET_NUMBER = 123;
    private static final String COUNTRY = "Argentina";
    private static final String COUNTRY_ID = "AR";

    private static final String OFFICE_TABLE = "office";
    private static final String LOCALITY_TABLE = "system_locality";
    private static final String PROVINCE_TABLE = "system_province";
    private static final String COUNTRY_TABLE = "system_country";

    private OfficeDaoImpl officeDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert officeJdbcInsert;
    private SimpleJdbcInsert localityJdbcInsert;
    private SimpleJdbcInsert provinceJdbcInsert;
    private SimpleJdbcInsert countryJdbcInsert;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp(){
        this.officeDao = new OfficeDaoImpl(this.ds);
        this.jdbcTemplate = new JdbcTemplate(this.ds);
        this.officeJdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(OFFICE_TABLE)
                .usingGeneratedKeyColumns("office_id");
        this.localityJdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(LOCALITY_TABLE)
                .usingGeneratedKeyColumns("locality_id");
        this.provinceJdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(PROVINCE_TABLE)
                .usingGeneratedKeyColumns("province_id");
        this.countryJdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(COUNTRY_TABLE);
    }
    
    private void cleanAllTables(){
        this.jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
    }

    private void insertLocality(){
        // Insertar pais
        Map<String, Object> countryMap = new HashMap<>();
        countryMap.put("name", COUNTRY);
        countryMap.put("country_id", COUNTRY_ID);
        countryJdbcInsert.execute(countryMap);

        // Insertar provincia
        Map<String, Object> provinceMap = new HashMap<>();
        provinceMap.put("name", PROVINCE);
        provinceMap.put("country_id", COUNTRY_ID);
        provinceJdbcInsert.execute(provinceMap);

        Map<String, Object> localityMap = new HashMap<>();
        localityMap.put("name", LOCALITY);
        localityMap.put("province_id", 0);
        localityJdbcInsert.execute(localityMap);
    }

    private void insertOffice(){
        insertLocality();

        Map<String, Object> officeMap = new HashMap<>();
        officeMap.put("name", NAME);
        officeMap.put("email", EMAIL);
        officeMap.put("phone", PHONE);
        officeMap.put("locality_id", 0); // Identity de HSQLDB empieza en 0
        officeMap.put("street", STREET);
        officeMap.put("street_number", STREET_NUMBER);
        officeJdbcInsert.execute(officeMap);
    }

    private void insertAnotherOffice(){
        Map<String, Object> officeMap = new HashMap<>();
        officeMap.put("name", NAME_2);
        officeMap.put("email", EMAIL);
        officeMap.put("phone", PHONE);
        officeMap.put("locality_id", 0); // Identity de HSQLDB empieza en 0
        officeMap.put("street", STREET);
        officeMap.put("street_number", STREET_NUMBER);
        officeJdbcInsert.execute(officeMap);
    }

    private Country countryModel(){
        Country c = new Country();
        c.setName(COUNTRY);
        c.setId(COUNTRY_ID);
        return c;
    }

    private Locality localityModel(){
        Locality l = new Locality();
        l.setName(LOCALITY);
        l.setId(0); // Identity de HSQLDB empieza en 0
        return l;
    }

    private Office officeModel(){
        Office o = new Office();
        o.setId(0);
        o.setName(NAME);
        o.setEmail(EMAIL);
        o.setPhone(PHONE);
        o.setLocality(localityModel());
        o.setStreet(STREET);
        return o;
    }

    @Test
    public void testCreateOffice()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertLocality();
        Office o = officeModel();

        // 2. Ejercitar
        Office office = this.officeDao.create(o);

        // 3. Postcondiciones
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, OFFICE_TABLE));
        assertEquals(NAME, office.getName());
        assertEquals(0, (int)office.getId()); // Identity de HSQLDB empieza en 0
        assertEquals(localityModel(), office.getLocality());
    }

    @Test
    public void testFindById(){
        // 1. Precondiciones
        cleanAllTables();
        insertOffice();

        // 2. Ejercitar
        Optional<Office> maybeOffice = officeDao.findById(0); // Identity de HSQLDB empieza en 0

        // 3. Postcondiciones
        assertTrue(maybeOffice.isPresent());
        assertEquals(0, (int)maybeOffice.get().getId());
        assertEquals(NAME, maybeOffice.get().getName());
    }

    @Test
    public void testFindByIdDoesntExist(){
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Optional<Office> maybeOffice = officeDao.findById(0); // Identity de HSQLDB empieza en 0

        // 3. Postcondiciones
        assertFalse(maybeOffice.isPresent());
    }

    @Test
    public void testFindByIds(){
        // 1. Precondiciones
        cleanAllTables();
        insertOffice();
        insertAnotherOffice();

        // 2. Ejercitar
        Collection<Office> offices = officeDao.findByIds(Arrays.asList(0,1,2)); // Identity de HSQLDB empieza en 0

        // 3. Postcondiciones
        assertNotNull(offices);
        assertEquals(2, offices.size());
    }

    @Test
    public void testFindByIdsDoesntExist(){
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<Office> offices = officeDao.findByIds(Arrays.asList(0,1,2)); // Identity de HSQLDB empieza en 0

        // 3. Postcondiciones
        assertNotNull(offices);
        assertTrue(offices.isEmpty());
    }

    @Test
    public void testFindByName(){
        // 1. Precondiciones
        cleanAllTables();
        insertOffice();
        insertAnotherOffice();

        // 2. Ejercitar
        Collection<Office> offices = officeDao.findByName(NAME);
        Collection<Office> offices2 = officeDao.findByName(NAME_2);

        // 3. Postcondiciones
        assertNotNull(offices);
        assertEquals(2, offices.size());
        assertEquals(1, offices2.size());
    }

    @Test
    public void testFindByNameDoesntExist(){
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<Office> offices = officeDao.findByName(NAME);

        // 3. Postcondiciones
        assertNotNull(offices);
        assertTrue(offices.isEmpty());
    }

    @Test
    public void testList(){
        // 1. Precondiciones
        // Vaciar tablas
        cleanAllTables();
        insertOffice();
        insertAnotherOffice();

        // 2. Ejercitar
        Collection<Office> offices = officeDao.list();

        // 3. Postcondiciones
        assertNotNull(offices);
        assertEquals(2, offices.size());
    }

    @Test
    public void testEmptyList(){
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<Office> offices = officeDao.list();

        // 3. Postcondiciones
        assertNotNull(offices);
        assertTrue(offices.isEmpty());
    }

    @Test
    public void testFindByCountry(){
        // 1. Precondiciones
        cleanAllTables();

        insertOffice();

        // Insertar pais 2
        Map<String, Object> countryMap = new HashMap<>();
        countryMap.put("name", COUNTRY + "_1");
        countryMap.put("country_id", "C1");
        countryJdbcInsert.execute(countryMap);

        // Insertar provincia 2
        Map<String, Object> provinceMap = new HashMap<>();
        provinceMap.put("name", PROVINCE + "_1");
        provinceMap.put("country_id", "C1");
        provinceJdbcInsert.execute(provinceMap);

        // Insertar oficina 2
        Map<String, Object> officeMap = new HashMap<>();
        officeMap.put("name", NAME + "_1");
        officeMap.put("email", EMAIL);
        officeMap.put("phone", PHONE);
        officeMap.put("province_id", 1); // Identity de HSQLDB empieza en 0
        officeMap.put("street", STREET);
        officeMap.put("street_number", STREET_NUMBER);
        officeJdbcInsert.execute(officeMap);

        Country c = countryModel();

        // 2. Ejercitar
        Collection<Office> offices = officeDao.findByCountry(c);

        // 3. Postcondiciones
        assertNotNull(offices);
        assertEquals(1, offices.size());
    }

    @Test
    public void testFindByCountryDoesntExists(){
        // 1. Precondiciones
        cleanAllTables();

        //Crear Country modelo
        Country c = countryModel();

        // 2. Ejercitar
        Collection<Office> offices = officeDao.findByCountry(c);

        // 3. Postcondiciones
        assertNotNull(offices);
        assertTrue(offices.isEmpty());
    }

    @Test
    public void testRemoveById(){
        // 1. Precondiciones
        cleanAllTables();
        insertOffice();
        insertAnotherOffice();

        // 2. Ejercitar
        officeDao.remove(0);

        // 3. Postcondiciones
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, OFFICE_TABLE));
    }

    @Test
    public void testRemoveByModel(){
        // 1. Precondiciones
        cleanAllTables();
        insertOffice();
        insertAnotherOffice();

        // 2. Ejercitar
        officeDao.remove(officeModel());

        // 3. Postcondiciones
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, OFFICE_TABLE));
    }

    @Test
    public void testUpdate(){
        // 1. Precondiciones
        cleanAllTables();
        insertOffice();

        // Modelo de la oficina a crear
        Office o = this.officeDao.findById(officeModel().getId()).get();
        o.setName(NAME + " (updated)");

        // 2. Ejercitar
        officeDao.update(o);

        // 3. Postcondiciones
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, OFFICE_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, OFFICE_TABLE, "name = '" + NAME + " (updated)'"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, OFFICE_TABLE, "name = '" + NAME + "'"));

    }
}
