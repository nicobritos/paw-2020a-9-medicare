package ar.edu.itba.paw;

import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.models.ModelMetadata;
import ar.edu.itba.paw.persistence.CountryDaoImpl;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
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
@Sql("classpath:sql/schema.sql")
@ContextConfiguration(classes = TestConfig.class)
public class CountryDaoImplTest {
    private static final String COUNTRY = "Argentina";
    private static final String COUNTRY_ID = "AR";

    private static final String COUNTRY_2 = "Brasil";
    private static final String COUNTRY_ID_2 = "BR";

    private static final String COUNTRIES_TABLE = "system_country";

    private CountryDaoImpl countryDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        this.countryDao = new CountryDaoImpl();
        this.jdbcTemplate = new JdbcTemplate(this.ds);
        this.jdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(COUNTRIES_TABLE);
    }

    /* --------------------- FUNCIONES AUXILIARES -------------------------------------------- */

    private void cleanAllTables(){
        this.jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
    }

    /** Devuelve un country con id=COUNTRY_ID y name=COUNTRY **/
    private Country countryModel(){
        Country c = new Country();
        c.setName(COUNTRY);
        c.setId(COUNTRY_ID);
        return c;
    }


    /** Inserta en la db el pais con id=COUNTRY_ID y name=COUNTRY **/
    private void insertCountry() {
        Map<String, Object> map = new HashMap<>();
        map.put("country_id", COUNTRY_ID);
        map.put("name", COUNTRY);
        jdbcInsert.execute(map);
    }

    /** Inserta en la db el pais con id=COUNTRY_ID_2 y name=COUNTRY_2 **/
    private void insertAnotherCountry() {
        Map<String, Object> map = new HashMap<>();
        map.put("country_id", COUNTRY_ID_2);
        map.put("name", COUNTRY_2);
        jdbcInsert.execute(map);
    }

    /* --------------------- MÉTODO: countryDao.create(Country) -------------------------------------------- */

    @Test
    public void testCreateCountrySuccessfully()
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
    }

    @Test
    public void testCreateAnotherCountrySuccessfully()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        Country c = new Country();
        c.setName(COUNTRY_2);
        c.setId(COUNTRY_ID_2);

        // 2. Ejercitar
        Country country = this.countryDao.create(c);

        // 3. Postcondiciones
        assertEquals(2, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, COUNTRIES_TABLE));
        assertEquals(COUNTRY_2, country.getName());
        assertEquals(COUNTRY_ID_2, country.getId());
    }

    @Test
    public void testCreateCountryRepeatedKeyFail()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        Country c = countryModel();
        expectedException.expect(DuplicateKeyException.class);


        // 2. Ejercitar
        Country country = this.countryDao.create(c);

        // 3. Postcondiciones
        // Que el metodo tire DuplicateKeyException
    }

    @Test
    public void testCreateCountryNullFail()
    {
        // 1. Precondiciones
        cleanAllTables();
        expectedException.expect(NullPointerException.class);

        // 2. Ejercitar
        Country country = this.countryDao.create(null);

        // 3. Postcondiciones
        // Que el metodo tire NullPointerException
    }

    @Test
    public void testCreateCountryEmptyCountryFail()
    {
        // 1. Precondiciones
        cleanAllTables();
        Country c = new Country();
        expectedException.expect(CoreMatchers.anyOf( // Falla si no se tira ninguna de las excepciones de la lista
                CoreMatchers.instanceOf(IllegalStateException.class), // Esta excepcion se tira si no tiene nombre // TODO: chequear esta excepcion (poco descriptiva)
                CoreMatchers.instanceOf(DataIntegrityViolationException.class) // Esta excepcion se tira si no tiene id // TODO: chequear esta excepcion (poco descriptiva)
        ));

        // 2. Ejercitar
        Country country = this.countryDao.create(c);

        // 3. Postcondiciones
        // Que el metodo tire IllegalStateException (no name) o DataIntegrityViolationException (no id), se hace esto porque depende de cual chequea primero.
    }

    @Test
    public void testCreateCountryEmptyIdFail()
    {
        // 1. Precondiciones
        cleanAllTables();
        Country c = new Country();
        c.setName(COUNTRY);
        expectedException.expect(DataIntegrityViolationException.class); // TODO: chequear esta excepcion (poco descriptiva)


        // 2. Ejercitar
        Country country = this.countryDao.create(c);

        // 3. Postcondiciones
        // Que el metodo tire DataIntegrityViolationException
    }

    @Test
    public void testCreateCountryEmptyNameFail()
    {
        // 1. Precondiciones
        cleanAllTables();
        Country c = new Country();
        c.setId(COUNTRY_ID);
        expectedException.expect(IllegalStateException.class); // TODO: chequear esta excepcion (poco descriptiva)

        // 2. Ejercitar
        Country country = this.countryDao.create(c);

        // 3. Postcondiciones
        // Que el metodo tire IllegalStateException
    }

    @Test
    public void testCreateCountryTooLongIdFail()
    {
        // 1. Precondiciones
        cleanAllTables();
        Country c = new Country();
        c.setName(COUNTRY);
        c.setId("CCC"); // Los countries IDs deben tener 2 caracteres
        expectedException.expect(DataIntegrityViolationException.class); // TODO: chequear esta excepcion (poco descriptiva)

        // 2. Ejercitar
        Country country = this.countryDao.create(c);

        // 3. Postcondiciones
        // Que el metodo tire DataIntegrityViolationException
    }

    @Test
    public void testCreateCountryTooShortIdFail()
    {
        // 1. Precondiciones
        cleanAllTables();
        Country c = new Country();
        c.setName(COUNTRY);
        c.setId("C"); // Los countries IDs deben tener 2 caracteres
        expectedException.expect(DataIntegrityViolationException.class); // TODO: chequear esta excepcion (poco descriptiva)

        // 2. Ejercitar
        Country country = this.countryDao.create(c);

        // 3. Postcondiciones
        // Que el metodo tire DataIntegrityViolationException
    }

    @Test
    public void testCreateCountryInvalidCharInIdFail()
    {
        // 1. Precondiciones
        cleanAllTables();
        Country c = new Country();
        c.setName(COUNTRY);
        c.setId("C2"); // Los countries IDs deben tener 2 caracteres
        expectedException.expect(DataIntegrityViolationException.class); // TODO: chequear esta excepcion (poco descriptiva)

        // 2. Ejercitar
        Country country = this.countryDao.create(c);

        // 3. Postcondiciones
        // Que el metodo tire DataIntegrityViolationException
    }

    @Test
    public void testCreateCountryCaseInsensitiveId()
    {
        // 1. Precondiciones
        cleanAllTables();
        Country c = new Country();
        c.setName(COUNTRY);
        c.setId(COUNTRY_ID.toLowerCase());

        // 2. Ejercitar
        Country country = this.countryDao.create(c);

        // 3. Postcondiciones
        assertEquals(COUNTRY_ID, country.getId());
    }

    /* --------------------- MÉTODO: countryDao.findById(String) -------------------------------------------- */

    @Test
    public void testFindCountryById()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        insertAnotherCountry();

        // 2. Ejercitar
        Optional<Country> country = this.countryDao.findById(COUNTRY_ID);

        // 3. Postcondiciones
        assertTrue(country.isPresent());
        assertEquals(COUNTRY, country.get().getName());
    }

    @Test
    public void testFindCountryByIdDoesntExist() {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        
        // 2. Ejercitar
        Optional<Country> country = this.countryDao.findById(COUNTRY_ID_2);

        // 3. Postcondiciones
        assertFalse(country.isPresent());
    }

    @Test
    public void testFindCountryByIdNull()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();

        // 2. Ejercitar
        Optional<Country> country = this.countryDao.findById(null);

        // 3. Postcondiciones
        assertFalse(country.isPresent());
    }

    @Test
    public void testFindCountryByIdInvalidId()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();

        // 2. Ejercitar
        Optional<Country> country = this.countryDao.findById("CCC");

        // 3. Postcondiciones
        assertFalse(country.isPresent());
    }

    /* --------------------- MÉTODO: countryDao.findByIds(Collection<String>) -------------------------------------------- */

    @Test
    public void testFindCountryByIds()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        insertAnotherCountry();

        // 2. Ejercitar
        Collection<Country> countries = this.countryDao.findByIds(Arrays.asList(COUNTRY_ID, COUNTRY_ID_2));

        // 3. Postcondiciones
        assertNotNull(countries);
        assertEquals(2, countries.size());
        for (Country c : countries){
            assertTrue(c.getId().equals(COUNTRY_ID) || c.getId().equals(COUNTRY_ID_2));
        }
    }

    @Test
    public void testFindCountryByIdsNotAllPresent()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();

        // 2. Ejercitar
        Collection<Country> countries = this.countryDao.findByIds(Arrays.asList(COUNTRY_ID, COUNTRY_ID_2));

        // 3. Postcondiciones
        assertNotNull(countries);
        assertEquals(1, countries.size());
        for (Country c : countries){
            assertEquals(countryModel(), c);
        }
    }

    @Test
    public void testFindCountryByIdsDontExist()
    {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<Country> countries = this.countryDao.findByIds(Arrays.asList(COUNTRY_ID, COUNTRY_ID_2));

        // 3. Postcondiciones
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

    /* --------------------- MÉTODO: countryDao.findByName(String) -------------------------------------------- */

    @Test
    public void testFindCountryByName() {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        insertAnotherCountry();
        Map<String, Object> map = new HashMap<>();
        map.put("country_id", "AT");
        map.put("name", COUNTRY);
        jdbcInsert.execute(map);

        // 2. Ejercitar
        Collection<Country> countries = this.countryDao.findByName(COUNTRY);

        // 3. Postcondiciones
        assertNotNull(countries);
        assertEquals(2, countries.size());
        for (Country c : countries){
            assertEquals(COUNTRY, c.getName());
        }
    }

    @Test
    public void testFindCountryByNameDoesntExist() {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();

        // 2. Ejercitar
        Collection<Country> countries = this.countryDao.findByName(COUNTRY_2);

        // 3. Postcondiciones
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

    @Test
    public void testFindCountryByNameNull()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        insertAnotherCountry();
        expectedException.expect(NullPointerException.class);

        // 2. Ejercitar
        this.countryDao.findByName(null);

        // 3. Postcondiciones
        // Que el metodo tire NullPointerException
    }

    @Test
    public void testFindCountryByContainingName()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        insertAnotherCountry();
        Map<String, Object> map = new HashMap<>();
        map.put("country_id", "AT");
        map.put("name", COUNTRY);
        jdbcInsert.execute(map);


        // 2. Ejercitar
        Collection<Country> countries = this.countryDao.findByName("Arg");

        // 3. Postcondiciones
        assertEquals(2, countries.size());
    }

    /* --------------------- MÉTODO: countryDao.list() -------------------------------------------- */

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
    public void testCountryEmptyList() {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<Country> countries = this.countryDao.list();

        // 3. Postcondiciones
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

    /* --------------------- MÉTODO: countryDao.update(Country) -------------------------------------------- */

    @Test
    public void testCountryUpdate() {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        insertAnotherCountry();
        Country c = countryModel();
        c.setName("Armenia");

        // 2. Ejercitar
        this.countryDao.update(c);

        // 3. Postcondiciones
        assertEquals(2,JdbcTestUtils.countRowsInTable(jdbcTemplate, COUNTRIES_TABLE));
        assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, COUNTRIES_TABLE, "name = 'Armenia'"));
        assertEquals(0,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, COUNTRIES_TABLE, "name = '"+ COUNTRY +"'"));
    }

    @Test
    public void testCountryUpdateNull()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        insertAnotherCountry();
        expectedException.expect(NullPointerException.class);

        // 2. Ejercitar
        this.countryDao.update(null);

        // 3. Postcondiciones
        // Que el metodo tire NullPointerException
        assertEquals(2,JdbcTestUtils.countRowsInTable(jdbcTemplate, COUNTRIES_TABLE));
    }

    @Test
    public void testCountryUpdateNotExistentCountry()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertAnotherCountry();
        expectedException.expect(Exception.class);  // <-- TODO: Insert exception class here

        // 2. Ejercitar
        this.countryDao.update(countryModel()); // TODO: NO HACE NADA, DEBERIA TIRAR EXCEPCION QUE NO EXISTE EL COUNTRY CON ESE ID

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, COUNTRIES_TABLE));
    }

    @Test
    public void testCountryUpdateCountryWithNullName()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        Country c = new Country();
        c.setId(COUNTRY_ID);
        expectedException.expect(IllegalStateException.class);

        // 2. Ejercitar
        this.countryDao.update(c);

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, COUNTRIES_TABLE));
        assertEquals(countryModel(), c);
    }

    @Test
    public void testCountryUpdateCountryWithNullId()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        Country c = new Country();
        c.setName(COUNTRY);
        expectedException.expect(Exception.class); // <-- TODO: Insert exception class here

        // 2. Ejercitar
        this.countryDao.update(c); // TODO: NO HACE NADA, DEBERIA TIRAR EXCEPCION QUE DEBE TENER ID NOT NULL

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, COUNTRIES_TABLE));
    }

    /* --------------------- MÉTODO: countryDao.remove(String id) -------------------------------------------- */

    @Test
    public void testCountryRemoveById()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();

        // 2. Ejercitar
        this.countryDao.remove(COUNTRY_ID);

        // 3. Postcondiciones
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, COUNTRIES_TABLE));
    }

    @Test
    public void testCountryRemoveByIdNotExistent()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();

        // 2. Ejercitar
        this.countryDao.remove(COUNTRY_ID_2);

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, COUNTRIES_TABLE));
    }

    @Test
    public void testCountryRemoveByNullId()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();

        // 2. Ejercitar
        this.countryDao.remove((String) null);

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, COUNTRIES_TABLE));
    }

    /* --------------------- MÉTODO: countryDao.remove(Country) -------------------------------------------- */

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
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, COUNTRIES_TABLE));
    }

    @Test
    public void testCountryRemoveByModelNotExistent()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertAnotherCountry();

        // 2. Ejercitar
        this.countryDao.remove(countryModel());

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, COUNTRIES_TABLE));
    }

    @Test
    public void testCountryRemoveByNullModel()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        expectedException.expect(NullPointerException.class);

        // 2. Ejercitar
        this.countryDao.remove((Country) null);

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, COUNTRIES_TABLE));
    }

    /* --------------------- MÉTODO: countryDao.count() -------------------------------------------- */

    @Test
    public void testCountryCount()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        insertAnotherCountry();

        // 2. Ejercitar
        ModelMetadata modelMetadata = this.countryDao.count();

        // 3. Postcondiciones
        assertEquals(2, (int) modelMetadata.getCount()); // TODO: fix
        System.out.println(modelMetadata.getMax()); // No se que devuelve esto
        System.out.println(modelMetadata.getMin()); // No se que devuelve esto
    }

    @Test
    public void testCountryCountEmptyTable()
    {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        ModelMetadata modelMetadata = this.countryDao.count();

        // 3. Postcondiciones
        assertEquals(0, (int) modelMetadata.getCount()); // TODO: fix
        System.out.println(modelMetadata.getMax()); // No se que devuelve esto
        System.out.println(modelMetadata.getMin()); // No se que devuelve esto
    }

    /* --------------------- MÉTODO: countryDao.findByField() -------------------------------------------- */

    @Test
    public void testCountryFindByFieldName()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        insertAnotherCountry();

        // 2. Ejercitar
        List<Country> countries = this.countryDao.findByField("name", COUNTRY);

        // 3. Postcondiciones
        assertNotNull(countries);
        assertEquals(1, countries.size());
        for (Country c : countries){
            assertEquals(COUNTRY, c.getName());
        }
    }

    @Test
    public void testCountryFindByFieldId()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        insertAnotherCountry();

        // 2. Ejercitar
        List<Country> countries = this.countryDao.findByField("country_id", COUNTRY_ID);

        // 3. Postcondiciones
        assertNotNull(countries);
        assertEquals(1, countries.size());
        for (Country c : countries){
            assertEquals(COUNTRY_ID, c.getId());
        }
    }

    @Test
    public void testCountryFindByFieldNull()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        insertAnotherCountry();

        // 2. Ejercitar
        List<Country> countries = this.countryDao.findByField("country_id", null); //TODO: Deberia tirar NullPointer (?)

        // 3. Postcondiciones
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

    @Test
    public void testCountryFindByFieldNotExistent()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        insertAnotherCountry();
        expectedException.expect(BadSqlGrammarException.class);

        // 2. Ejercitar
        List<Country> countries = this.countryDao.findByField("country_id_no_existo", COUNTRY_ID); //TODO: Deberia tirar otro tipo de error (?)

        // 3. Postcondiciones
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

    @Test
    public void testCountryFindByFieldContentNotExistent()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        insertAnotherCountry();

        // 2. Ejercitar
        List<Country> countries = this.countryDao.findByField("country_id", "No existo"); //TODO: Deberia tirar NullPointer (?)

        // 3. Postcondiciones
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }
}