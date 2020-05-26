package ar.edu.itba.paw.tests;

import ar.edu.itba.paw.interfaces.daos.StaffDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.config.TestConfig;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:sql/schema.sql")
@ContextConfiguration(classes = TestConfig.class)
public class StaffDaoImplTest
{
    private static final int STARTING_ID = 0;
    private static final String OFFICE = "Hospital Nacional";
    private static final String OFFICE_2 = "Consultorio Provincial";
    private static final String STREET = "Av 9 de Julio 123";
    private static final String LOCALITY = "Capital Federal";
    private static final String PROVINCE = "Buenos Aires";
    private static final String COUNTRY = "Argentina";
    private static final String COUNTRY_ID = "AR";
    private static final String OFFICE_PHONE = "(011) 1234567890";
    private static final String OFFICE_EMAIL = "test@officetest.com";
    private static final String URL = "www.hnacional.com";
    private static final String FIRST_NAME = "Nombre";
    private static final String SURNAME = "Apellido";
    private static final String EMAIL = "napellido@test.com";
    private static final String PHONE = "1123456789";
    private static final String PASSWORD = "pass1234";
    private static final int PROFILE_ID = STARTING_ID;
    private static final String TOKEN = "123";
    private static final String FIRST_NAME_2 = "Roberto";
    private static final String SURNAME_2 = "Rodriguez";
    private static final String EMAIL_2 = "napellido2@test2.com";
    private static final String PHONE_2 = "(011) 1123456789";
    private static final String PASSWORD_2 = "password1234";
    private static final int PROFILE_ID_2 = STARTING_ID + 1;
    private static final String MIME_TYPE = "image/svg+xml";
    private static final String PICTURE = "defaultProfilePic.svg";
    private static final Resource IMG = new ClassPathResource("img/" + PICTURE);
    private static final byte[] IMG_DATA = getImgData(IMG);
    private static final long IMG_SIZE = getImgSize(IMG);
    private static final int REGISTRATION_NUMBER = 123;
    private static final String STAFF_SPECIALTY = "Odontologo";

    private static final String OFFICES_TABLE = "office";
    private static final String LOCALITIES_TABLE = "system_locality";
    private static final String PROVINCES_TABLE = "system_province";
    private static final String COUNTRIES_TABLE = "system_country";
    private static final String USERS_TABLE = "users";
    private static final String PICTURES_TABLE = "picture";
    private static final String STAFFS_TABLE = "staff";
    private static final String STAFF_SPECIALTIES_TABLE = "system_staff_specialty";

    @Autowired
    private StaffDao staffDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert officeJdbcInsert;
    private SimpleJdbcInsert localityJdbcInsert;
    private SimpleJdbcInsert provinceJdbcInsert;
    private SimpleJdbcInsert countryJdbcInsert;
    private SimpleJdbcInsert userJdbcInsert;
    private SimpleJdbcInsert pictureJdbcInsert;
    private SimpleJdbcInsert staffJdbcInsert;
    private SimpleJdbcInsert staffSpecialtiesJdbcInsert;


    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private DataSource ds;
    
    @Before
    public void setUp(){
        this.jdbcTemplate = new JdbcTemplate(this.ds);
        this.officeJdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(OFFICES_TABLE)
                .usingGeneratedKeyColumns("office_id");
        this.localityJdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(LOCALITIES_TABLE)
                .usingGeneratedKeyColumns("locality_id");
        this.provinceJdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(PROVINCES_TABLE)
                .usingGeneratedKeyColumns("province_id");
        this.countryJdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(COUNTRIES_TABLE);
        this.userJdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(USERS_TABLE)
                .usingGeneratedKeyColumns("users_id");
        this.pictureJdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(PICTURES_TABLE)
                .usingGeneratedKeyColumns("picture_id");
        this.staffJdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(STAFFS_TABLE)
                .usingGeneratedKeyColumns("staff_id");
        this.staffSpecialtiesJdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(STAFF_SPECIALTIES_TABLE)
                .usingGeneratedKeyColumns("specialty_id");
    }

    /* ---------------------- FUNCIONES AUXILIARES ---------------------------------------------------------------- */

    private static byte[] getImgData(Resource img){
        try {
            return Files.readAllBytes(img.getFile().toPath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private static long getImgSize(Resource img){
        try {
            return img.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private void cleanAllTables() {
        this.jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
    }

    /**
     * Devuelve un User con
     * firstName = IMG_DATA
     * surname = SURNAME
     * password = PASSWORD
     * email = EMAIL
     * phone = PHONE
     * profileID = PROFILE_ID
     * token = null
     * tokenCreatedDate = null
     * id = STARTING_ID
     **/
    private User userModel() {
        User u = new User();
        try {
            u.setEmail(EMAIL);
            u.setPassword(PASSWORD);
            u.setFirstName(FIRST_NAME);
            u.setSurname(SURNAME);
            u.setPhone(PHONE);
            u.setProfilePicture(pictureModel());
            u.setToken(TOKEN);
            u.setTokenCreatedDate(null);
            u.setVerified(true);
            u.setId(STARTING_ID);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        return u;
    }

    private Picture pictureModel(){
        Picture p = new Picture();
        p.setId(STARTING_ID);
        p.setMimeType(MIME_TYPE);
        p.setSize(IMG_SIZE);
        p.setData(IMG_DATA);
        p.setName(PICTURE);
        return p;
    }

    /** Inserta en la db la imagen con
     * data = IMG_DATA
     * mimeType = MIME_TYPE
     * name = NAME
     * size = IMG_SIZE
     **/
    private void insertPicture() {
        Map<String, Object> map = new HashMap<>();
        map.put("data", IMG_DATA);
        map.put("mime_type", MIME_TYPE);
        map.put("size", IMG_SIZE);
        map.put("name", PICTURE);
        pictureJdbcInsert.execute(map);
    }

    /**
     * Inserta en la db el user con
     * firstName = IMG_DATA
     * surname = SURNAME
     * password = PASSWORD
     * email = EMAIL
     * phone = PHONE
     * profileID = PROFILE_ID
     * token = TOKEN
     * tokenCreatedDate = null
     **/
    private void insertUser() {
        insertPicture();
        Map<String, Object> map = new HashMap<>();
        map.put("first_name", FIRST_NAME);
        map.put("surname", SURNAME);
        map.put("password", PASSWORD);
        map.put("email", EMAIL);
        map.put("phone", PHONE);
        map.put("profile_id", PROFILE_ID);
        map.put("token", TOKEN);
        map.put("token_created_date", null);
        userJdbcInsert.execute(map);
    }

    /**
     * Inserta en la db el user con
     * firstName = FIRST_NAME
     * surname = SURNAME
     * password = PASSWORD
     * email = EMAIL
     * phone = PHONE
     * profileID = PROFILE_ID
     * token = null
     * tokenCreatedDate = null
     **/
    private void insertAnotherUser() {
        insertPicture();
        insertPicture();
        Map<String, Object> map = new HashMap<>();
        map.put("first_name", FIRST_NAME_2);
        map.put("surname", SURNAME_2);
        map.put("password", PASSWORD_2);
        map.put("email", EMAIL_2);
        map.put("phone", PHONE_2);
        map.put("profile_id", PROFILE_ID_2);
        map.put("token", null);
        map.put("token_created_date", null);
        userJdbcInsert.execute(map);
    }

    /** Devuelve una locality con id=STARTING_ID, name=PROVINCE y como country el devuelto en countryModel() **/
    private Locality localityModel(){
        Locality l = new Locality();
        l.setName(LOCALITY);
        l.setProvince(provinceModel());
        l.setId(STARTING_ID);
        return l;
    }

    /** Devuelve una province con id=STARTING_ID, name=PROVINCE y como country el devuelto en countryModel() **/
    private Province provinceModel(){
        Province p = new Province();
        p.setId(STARTING_ID);
        p.setCountry(countryModel());
        p.setName(PROVINCE);
        return p;
    }

    /** Devuelve un country con id=COUNTRY_ID y name=COUNTRY **/
    private Country countryModel(){
        Country c = new Country();
        c.setName(COUNTRY);
        c.setId(COUNTRY_ID);
        return c;
    }

    /** Devuelve un office con
     * id=STARTING_ID
     * name=OFFICE
     * email=EMAIL
     * phone=PHONE
     * street=STREET
     * url=URL
     * y como locality toma el devuelto por localityModel()
     **/
    private Office officeModel(){
        Office o = new Office();
        o.setId(STARTING_ID);
        o.setName(OFFICE);
        o.setEmail(OFFICE_EMAIL);
        o.setPhone(OFFICE_PHONE);
        o.setLocality(localityModel());
        o.setStreet(STREET);
        o.setUrl(URL);
        return o;
    }

    /** Inserta en la db el pais con id=COUNTRY_ID y name=COUNTRY **/
    private void insertCountry(){
        Map<String, Object> map = new HashMap<>();
        map.put("country_id", COUNTRY_ID);
        map.put("name", COUNTRY);
        countryJdbcInsert.execute(map);
    }

    /** Inserta en la db la provincia con country_id=COUNTRY_ID y name=PROVINCE **/
    private void insertProvince(){
        insertCountry();
        Map<String, Object> map = new HashMap<>();
        map.put("country_id", COUNTRY_ID);
        map.put("name", PROVINCE);
        provinceJdbcInsert.execute(map);
    }

    /** Inserta en la db la localidad con country_id=STARTING_ID y name=LOCALITY **/
    private void insertLocality(){
        insertProvince();
        Map<String, Object> map = new HashMap<>();
        map.put("province_id", STARTING_ID);
        map.put("name", LOCALITY);
        localityJdbcInsert.execute(map);
    }

    /** Inserta en la db la oficina con
     * name=OFFICE
     * email=EMAIL
     * phone=PHONE
     * street=STREET
     * url=URL
     * localityId=STARTING_ID
     **/
    private void insertOffice(){
        insertLocality();

        Map<String, Object> officeMap = new HashMap<>();
        officeMap.put("name", OFFICE);
        officeMap.put("email", OFFICE_EMAIL);
        officeMap.put("phone", OFFICE_PHONE);
        officeMap.put("locality_id", STARTING_ID); // Identity de HSQLDB empieza en 0
        officeMap.put("street", STREET);
        officeMap.put("url", URL);
        officeJdbcInsert.execute(officeMap);
    }

    /** Inserta en la db la oficina con
     * localityId=STARTING_ID
     * name=OFFICE
     * email=EMAIL
     * phone=PHONE
     * street=STREET
     * url=URL
     **/
    private void insertAnotherOffice(){
        Map<String, Object> officeMap = new HashMap<>();
        officeMap.put("name", OFFICE_2);
        officeMap.put("email", OFFICE_EMAIL);
        officeMap.put("phone", OFFICE_PHONE);
        officeMap.put("locality_id", STARTING_ID); // Identity de HSQLDB empieza en 0
        officeMap.put("street", STREET);
        officeMap.put("url", URL);
        officeJdbcInsert.execute(officeMap);
    }
    
    private Staff staffModel(){
        Staff s = new Staff();
        s.setFirstName(FIRST_NAME);
        s.setRegistrationNumber(REGISTRATION_NUMBER);
        s.setSurname(SURNAME);
        s.setEmail(EMAIL);
        s.setPhone(PHONE);
        s.setId(STARTING_ID);
        s.setUser(userModel());
        s.setOffice(officeModel());
        return s;
    }

    /** Inserta en la db el staff con
     * firstName=FIRST_NAME
     * surname=SURNAME
     * registrationNumber=REGISTRATION_NUMBER
     * email=EMAIL
     * phone=PHONE
     * user_id=STARTING_ID
     * office_id=STARTING_ID
     **/
    private void insertStaff(){
        insertOffice();
        insertUser();
        Map<String, Object> staffMap = new HashMap<>();
        staffMap.put("first_name", FIRST_NAME);
        staffMap.put("registration_number", REGISTRATION_NUMBER);
        staffMap.put("surname", SURNAME);
        staffMap.put("email", EMAIL); // Identity de HSQLDB empieza en 0
        staffMap.put("phone", PHONE);
        staffMap.put("user_id", STARTING_ID);
        staffMap.put("office_id", STARTING_ID);
        staffJdbcInsert.execute(staffMap);
    }

    private void insertAnotherStaff(){
        insertAnotherUser();
        insertAnotherOffice();
        Map<String, Object> staffMap = new HashMap<>();
        staffMap.put("first_name", FIRST_NAME_2);
        staffMap.put("registration_number", REGISTRATION_NUMBER);
        staffMap.put("surname", SURNAME_2);
        staffMap.put("email", EMAIL); // Identity de HSQLDB empieza en 0
        staffMap.put("phone", PHONE);
        staffMap.put("user_id", STARTING_ID + 1);
        staffMap.put("office_id", STARTING_ID + 1);
        staffJdbcInsert.execute(staffMap);
    }

    /** Inserta en la db la especialidad con name=STAFF_SPECIALTY **/
    private void insertStaffSpecialty(){
        Map<String, Object> map = new HashMap<>();
        map.put("name", STAFF_SPECIALTY);
        staffSpecialtiesJdbcInsert.execute(map);
    }

    /** Devuelve un StaffSpecialty con name=STAFF_SPECIALTY **/
    private StaffSpecialty staffSpecialtyModel(){
        StaffSpecialty ss = new StaffSpecialty();
        ss.setName(STAFF_SPECIALTY);
        ss.setId(STARTING_ID);
        return ss;
    }

    /* --------------------- MÉTODO: staffDao.create(Staff) -------------------------------------------- */

    @Test
    public void testCreateStaffSuccessfully() {
        // 1. Precondiciones
        cleanAllTables();
        insertUser();
        insertOffice();
        Staff s = staffModel();

        // 2. Ejercitar
        Staff staff = this.staffDao.create(s);

        // 3. Postcondiciones
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, STAFFS_TABLE));
        assertEquals(FIRST_NAME, s.getFirstName());
        assertEquals(EMAIL, s.getEmail());
        assertEquals(officeModel(), s.getOffice());
        assertEquals(PHONE, s.getPhone());
        assertEquals(REGISTRATION_NUMBER, s.getRegistrationNumber());
        assertEquals(new LinkedList<>(), s.getStaffSpecialties());
        assertEquals(SURNAME, s.getSurname());
        assertEquals(userModel(), s.getUser());
    }

    @Test
    public void testCreateAnotherStaffSuccessfully() {
        // 1. Precondiciones
        cleanAllTables();
        insertUser();
        insertOffice();
        insertAnotherStaff();
        Staff s = staffModel();

        // 2. Ejercitar
        Staff staff = this.staffDao.create(s);

        // 3. Postcondiciones
        assertEquals(2, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, STAFFS_TABLE));
        assertEquals(FIRST_NAME, s.getFirstName());
        assertEquals(EMAIL, s.getEmail());
        assertEquals(officeModel(), s.getOffice());
        assertEquals(PHONE, s.getPhone());
        assertEquals(REGISTRATION_NUMBER, s.getRegistrationNumber());
        assertEquals(new LinkedList<>(), s.getStaffSpecialties());
        assertEquals(SURNAME, s.getSurname());
        assertEquals(userModel(), s.getUser());
    }

    @Test
    public void testCreateStaffNullFail()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertUser();
        insertOffice();
        expectedException.expect(NullPointerException.class);

        // 2. Ejercitar
        Staff staff = this.staffDao.create(null);

        // 3. Postcondiciones
        // Que el metodo tire NullPointerException
    }

    @Test
    public void testCreateStaffEmptyStaffFail()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertUser();
        insertOffice();
        Staff s = new Staff();
        expectedException.expect(CoreMatchers.anyOf( // Falla si no se tira ninguna de las excepciones de la lista
                CoreMatchers.instanceOf(IllegalStateException.class), // Esta excepcion se tira si no tiene data // TODO: chequear esta excepcion (poco descriptiva)
                CoreMatchers.instanceOf(DataIntegrityViolationException.class) // Esta excepcion se tira si no tiene id // TODO: chequear esta excepcion (poco descriptiva)
        ));

        // 2. Ejercitar
        Staff staff = this.staffDao.create(s);

        // 3. Postcondiciones
        // Que el metodo tire IllegalStateException (no data) o DataIntegrityViolationException (no id), se hace esto porque depende de cual chequea primero.
    }

    @Test
    public void testCreateStaffEmptyNameFail()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertUser();
        insertOffice();
        Staff s = staffModel();
        s.setFirstName(null);
        expectedException.expect(IllegalStateException.class); // TODO: chequear esta excepcion (poco descriptiva)

        // 2. Ejercitar
        Staff staff = this.staffDao.create(s);

        // 3. Postcondiciones
        // Que el metodo tire IllegalStateException
    }

    @Test
    public void testCreateStaffEmptySurnameFail()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertUser();
        insertOffice();
        Staff s = staffModel();
        s.setSurname(null);
        expectedException.expect(IllegalStateException.class); // TODO: chequear esta excepcion (poco descriptiva)

        // 2. Ejercitar
        Staff staff = this.staffDao.create(s);

        // 3. Postcondiciones
        // Que el metodo tire IllegalStateException
    }

    @Test
    public void testCreateStaffEmptyUserFail()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertUser();
        insertOffice();
        Staff s = staffModel();
        s.setUser(null);
        expectedException.expect(IllegalStateException.class); // TODO: chequear esta excepcion (poco descriptiva)

        // 2. Ejercitar
        Staff staff = this.staffDao.create(s);

        // 3. Postcondiciones
        // Que el metodo tire IllegalStateException
    }

    @Test
    public void testCreateStaffEmptyOfficeFail()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertUser();
        insertOffice();
        Staff s = staffModel();
        s.setOffice(null);
        expectedException.expect(IllegalStateException.class); // TODO: chequear esta excepcion (poco descriptiva)

        // 2. Ejercitar
        Staff staff = this.staffDao.create(s);

        // 3. Postcondiciones
        // Que el metodo tire IllegalStateException
    }

    @Test
    public void testCreateStaffEmptyEmailFail()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertUser();
        insertOffice();
        Staff s = staffModel();
        s.setEmail(null);
        expectedException.expect(IllegalStateException.class); // TODO: chequear esta excepcion (poco descriptiva)

        // 2. Ejercitar
        Staff staff = this.staffDao.create(s);

        // 3. Postcondiciones
        // Que el metodo tire IllegalStateException
    }

    /* --------------------- MÉTODO: staffDao.findById(String) -------------------------------------------- */

    @Test
    public void testFindStaffById()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        Optional<Staff> staff = this.staffDao.findById(STARTING_ID);

        // 3. Postcondiciones
        assertTrue(staff.isPresent());
        assertEquals(staffModel(), staff.get());
    }

    @Test
    public void testFindStaffByIdDoesntExist()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();

        // 2. Ejercitar
        Optional<Staff> staff = this.staffDao.findById(STARTING_ID + 1);

        // 3. Postcondiciones
        assertFalse(staff.isPresent());
    }

    @Test
    public void testFindStaffByIdNull()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();

        // 2. Ejercitar
        Optional<Staff> staff = this.staffDao.findById(null);

        // 3. Postcondiciones
        assertFalse(staff.isPresent());
    }

    /* --------------------- MÉTODO: staffDao.findByIds(Collection<String>) -------------------------------------------- */

    @Test
    public void testFindStaffByIds()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        Collection<Staff> staffs = this.staffDao.findByIds(Arrays.asList(STARTING_ID, STARTING_ID + 1));

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(2, staffs.size());
        for (Staff s : staffs){
            assertTrue(s.getId().equals(STARTING_ID) || s.getId().equals(STARTING_ID + 1));
        }
    }

    @Test
    public void testFindStaffByIdsNotAllPresent()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();

        // 2. Ejercitar
        Collection<Staff> staffs = this.staffDao.findByIds(Arrays.asList(STARTING_ID, STARTING_ID + 1));

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(1, staffs.size());
        for (Staff s : staffs){
            assertEquals(staffModel(), s);
        }
    }

    @Test
    public void testFindStaffByIdsDontExist()
    {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<Staff> staffs = this.staffDao.findByIds(Arrays.asList(STARTING_ID, STARTING_ID + 1));

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertTrue(staffs.isEmpty());
    }

    /* --------------------- MÉTODO: staffDao.list() -------------------------------------------- */

    @Test
    public void testStaffList()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        Collection<Staff> staffs = this.staffDao.list();

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(2, staffs.size());
    }

    @Test
    public void testStaffEmptyList()
    {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<Staff> staffs = this.staffDao.list();

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertTrue(staffs.isEmpty());
    }

    /* --------------------- MÉTODO: staffDao.update(Staff) -------------------------------------------- */

    @Test
    public void testStaffUpdate()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();
        Staff s = staffModel();
        s.setFirstName("Armenia");

        // 2. Ejercitar
        this.staffDao.update(s);

        // 3. Postcondiciones
        assertEquals(2,JdbcTestUtils.countRowsInTable(jdbcTemplate, STAFFS_TABLE));
        assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, STAFFS_TABLE, "first_name = 'Armenia'"));
        assertEquals(0,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, STAFFS_TABLE, "first_name = '"+ FIRST_NAME +"'"));
    }

    @Test
    public void testStaffUpdateNull()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();
        expectedException.expect(NullPointerException.class);

        // 2. Ejercitar
        this.staffDao.update(null);

        // 3. Postcondiciones
        // Que el metodo tire NullPointerException
        assertEquals(2,JdbcTestUtils.countRowsInTable(jdbcTemplate, STAFFS_TABLE));
    }

    @Test
    public void testStaffUpdateNotExistentStaff()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertUser();
        insertOffice();
        insertAnotherStaff();
        Staff s = staffModel();
        s.setId(STARTING_ID + 1);
        expectedException.expect(Exception.class);  // <-- TODO: Insert exception class here

        // 2. Ejercitar
        this.staffDao.update(s); // TODO: NO HACE NADA, DEBERIA TIRAR EXCEPCION QUE NO EXISTE EL STAFF CON ESE ID

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, STAFFS_TABLE));
    }

    @Test
    public void testStaffUpdateStaffWithNullName()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        Staff s = staffModel();
        s.setFirstName(null);
        expectedException.expect(IllegalStateException.class);

        // 2. Ejercitar
        this.staffDao.update(s);

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, STAFFS_TABLE));
        assertEquals(staffModel(), s);
    }

    @Test
    public void testStaffUpdateStaffWithNullId()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        Staff s = staffModel();
        s.setId(null);
        expectedException.expect(Exception.class); // <-- TODO: Insert exception class here

        // 2. Ejercitar
        this.staffDao.update(s); // TODO: NO HACE NADA, DEBERIA TIRAR EXCEPCION QUE DEBE TENER ID NOT NULL

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, STAFFS_TABLE));
    }

    /* --------------------- MÉTODO: staffDao.remove(String id) -------------------------------------------- */

    @Test
    public void testStaffRemoveById()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();

        // 2. Ejercitar
        this.staffDao.remove(STARTING_ID);

        // 3. Postcondiciones
        assertEquals(0,JdbcTestUtils.countRowsInTable(jdbcTemplate, STAFFS_TABLE));
    }

    @Test
    public void testStaffRemoveByIdNotExistent()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();

        // 2. Ejercitar
        this.staffDao.remove(STARTING_ID + 1);

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, STAFFS_TABLE));
    }

    @Test
    public void testStaffRemoveByNullId()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();

        // 2. Ejercitar
        this.staffDao.remove((Integer) null);

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, STAFFS_TABLE));
    }
    /* --------------------- MÉTODO: staffDao.remove(Staff) -------------------------------------------- */

    @Test
    public void testStaffRemoveByModel()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        Staff s = staffModel();

        // 2. Ejercitar
        this.staffDao.remove(s);

        // 3. Postcondiciones
        assertEquals(0,JdbcTestUtils.countRowsInTable(jdbcTemplate, STAFFS_TABLE));
    }

    @Test
    public void testStaffRemoveByModelNotExistent()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertUser();
        insertOffice();
        insertAnotherStaff();
        Staff s = staffModel();
        s.setId(STARTING_ID + 1);

        // 2. Ejercitar
        this.staffDao.remove(s);

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, STAFFS_TABLE));
    }

    @Test
    public void testStaffRemoveByNullModel()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        expectedException.expect(NullPointerException.class);

        // 2. Ejercitar
        this.staffDao.remove((Staff) null);

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, STAFFS_TABLE));
    }

    /* --------------------- MÉTODO: staffDao.count() -------------------------------------------- */

    @Test
    public void testStaffCount()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        ModelMetadata modelMetadata = this.staffDao.count();

        // 3. Postcondiciones
        assertEquals(2, (long) modelMetadata.getCount()); // TODO: fix
        System.out.println(modelMetadata.getMax()); // No se que devuelve esto
        System.out.println(modelMetadata.getMin()); // No se que devuelve esto
    }

    @Test
    public void testStaffCountEmptyTable()
    {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        ModelMetadata modelMetadata = this.staffDao.count();

        // 3. Postcondiciones
        assertEquals(0, (long) modelMetadata.getCount()); // TODO: fix
        System.out.println(modelMetadata.getMax()); // No se que devuelve esto
        System.out.println(modelMetadata.getMin()); // No se que devuelve esto
    }

    /* --------------------- MÉTODO: staffDao.findBy(Collection<String> names, Collection<String> surnames, Collection<Office>, Collection<StaffSpecialty>, Collection<Locality>) -------------------------------------------- */

    @Test
    public void testStaffFindByNamesOfficeStaffSpecialtyLocality()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        List<Staff> staffs = this.staffDao.findBy(Collections.singletonList(FIRST_NAME), Collections.singletonList(SURNAME), Collections.singletonList(officeModel()), Collections.emptyList(), Collections.singletonList(localityModel()));

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(1, staffs.size());
    }

    @Test
    public void testStaffFindByNamesOfficeStaffSpecialtyLocalityNullAll()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        List<Staff> staffs = this.staffDao.findBy((Collection<String>) null, null, null, null, null);

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(2, staffs.size());
    }

    @Test
    public void testStaffFindByNamesOfficeStaffSpecialtyLocalityOnlyFirstName()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        List<Staff> staffs = this.staffDao.findBy(Collections.singletonList(FIRST_NAME), null, null, null, null);

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(1, staffs.size());
    }

    @Test
    public void testStaffFindByNamesOfficeStaffSpecialtyLocalityOnlySurname()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        List<Staff> staffs = this.staffDao.findBy(null, Collections.singletonList(SURNAME), null, null, null);

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(1, staffs.size());
    }

    @Test
    public void testStaffFindByNamesOfficeStaffSpecialtyLocalityOnlyOffice()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        List<Staff> staffs = this.staffDao.findBy((Collection<String>) null, null, Collections.singleton(officeModel()), null, null);

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(1, staffs.size());
    }

    @Test
    public void testStaffFindByNamesOfficeStaffSpecialtyLocalityOnlySpecialties()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        List<Staff> staffs = this.staffDao.findBy((Collection<String>) null, null, null, Collections.emptyList(), null);

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(2, staffs.size());
    }

    @Test
    public void testStaffFindByNamesOfficeStaffSpecialtyLocalityOnlyLocality()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        List<Staff> staffs = this.staffDao.findBy((Collection<String>) null, null, null, null, Collections.singleton(localityModel()));

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(2, staffs.size());
    }

    /* --------------------- MÉTODO: staffDao.findBy(String name, String surname, Collection<Office>, Collection<StaffSpecialty>, Collection<Locality>) -------------------------------------------- */

    @Test
    public void testStaffFindByNameOfficeStaffSpecialtyLocality()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        List<Staff> staffs = this.staffDao.findBy(FIRST_NAME, SURNAME, Collections.singletonList(officeModel()), Collections.emptyList(), Collections.singletonList(localityModel()));

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(1, staffs.size());
    }

    @Test
    public void testStaffFindByNameOfficeStaffSpecialtyLocalityNullAll()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        List<Staff> staffs = this.staffDao.findBy((String)null, null, null, null, null);

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(2, staffs.size());
    }

    @Test
    public void testStaffFindByNameOfficeStaffSpecialtyLocalityOnlyFirstName()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        List<Staff> staffs = this.staffDao.findBy(FIRST_NAME, null, null, null, null);

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(1, staffs.size());
    }

    @Test
    public void testStaffFindByNameOfficeStaffSpecialtyLocalityOnlySurname()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        List<Staff> staffs = this.staffDao.findBy(null, SURNAME, null, null, null);

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(1, staffs.size());
    }

    @Test
    public void testStaffFindByNameOfficeStaffSpecialtyLocalityOnlyOffice()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        List<Staff> staffs = this.staffDao.findBy((String) null, null, Collections.singleton(officeModel()), null, null);

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(1, staffs.size());
    }

    @Test
    public void testStaffFindByNameOfficeStaffSpecialtyLocalityOnlySpecialties()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        List<Staff> staffs = this.staffDao.findBy((String) null, null, null, Collections.emptyList(), null);

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(2, staffs.size());
    }

    @Test
    public void testStaffFindByNameOfficeStaffSpecialtyLocalityOnlyLocality()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        List<Staff> staffs = this.staffDao.findBy((String) null, null, null, null, Collections.singleton(localityModel()));

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(2, staffs.size());
    }

    /* --------------------- MÉTODO: staffDao.findBy(Collection<String> names, Collection<String> surnames, Collection<Office>, Collection<StaffSpecialty>, Collection<Locality>, int page, int pageSize) -------------------------------------------- */

    @Test
    public void testStaffFindByNamesOfficeStaffSpecialtyLocalityPage()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        Paginator<Staff> paginator = this.staffDao.findBy(Collections.singletonList(FIRST_NAME), Collections.singletonList(SURNAME), Collections.singletonList(officeModel()), Collections.emptyList(), Collections.singletonList(localityModel()), 1, 1);

        // 3. Postcondiciones
        assertNotNull(paginator);
        assertEquals(1, paginator.getPage());
        assertEquals(1, paginator.getPageSize());
        assertEquals(0, paginator.getRemainingPages());
        assertEquals(1, (long) paginator.getTotalCount());
        assertEquals(1, paginator.getTotalPages());
        assertEquals(1, paginator.getModels().size());
    }

    @Test
    public void testStaffFindByNamesOfficeStaffSpecialtyLocalityPageNullAll()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        Paginator<Staff> paginator = this.staffDao.findBy((Collection<String>) null, null, null, null, null, 1, 1);

        // 3. Postcondiciones
        assertNotNull(paginator);
        assertEquals(1, paginator.getPage());
        assertEquals(1, paginator.getPageSize());
        assertEquals(1, paginator.getRemainingPages());
        assertEquals(2, (long) paginator.getTotalCount());
        assertEquals(2, paginator.getTotalPages());
        assertEquals(1, paginator.getModels().size());
    }

    @Test
    public void testStaffFindByNamesOfficeStaffSpecialtyLocalityPageOnlyFirstName()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        List<Staff> staffs = this.staffDao.findBy(Collections.singletonList(FIRST_NAME), null, null, null, null);

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(1, staffs.size());
    }

    @Test
    public void testStaffFindByNamesOfficeStaffSpecialtyLocalityPageOnlySurname()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        Paginator<Staff> paginator = this.staffDao.findBy(null, Collections.singletonList(SURNAME), null, null, null, 1,1);

        // 3. Postcondiciones
        assertNotNull(paginator);
        assertEquals(1, paginator.getPage());
        assertEquals(1, paginator.getPageSize());
        assertEquals(0, paginator.getRemainingPages());
        assertEquals(1, (long) paginator.getTotalCount());
        assertEquals(1, paginator.getTotalPages());
        assertEquals(1, paginator.getModels().size());
    }

    @Test
    public void testStaffFindByNamesOfficeStaffSpecialtyLocalityPageOnlyOffice()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        Paginator<Staff> paginator = this.staffDao.findBy((Collection<String>) null, null, Collections.singleton(officeModel()), null, null,1,1);

        // 3. Postcondiciones
        assertNotNull(paginator);
        assertEquals(1, paginator.getPage());
        assertEquals(1, paginator.getPageSize());
        assertEquals(0, paginator.getRemainingPages());
        assertEquals(1, (long) paginator.getTotalCount());
        assertEquals(1, paginator.getTotalPages());
        assertEquals(1, paginator.getModels().size());
    }

    @Test
    public void testStaffFindByNamesOfficeStaffSpecialtyLocalityPageOnlySpecialties()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        Paginator<Staff> paginator = this.staffDao.findBy((Collection<String>) null, null, null, Collections.emptyList(), null, 1,1);

        // 3. Postcondiciones
        assertNotNull(paginator);
        assertEquals(1, paginator.getPage());
        assertEquals(1, paginator.getPageSize());
        assertEquals(1, paginator.getRemainingPages());
        assertEquals(2, (long) paginator.getTotalCount());
        assertEquals(2, paginator.getTotalPages());
        assertEquals(1, paginator.getModels().size());
    }

    @Test
    public void testStaffFindByNamesOfficeStaffSpecialtyLocalityPageOnlyLocality()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        Paginator<Staff> paginator = this.staffDao.findBy((Collection<String>) null, null, null, null, Collections.singleton(localityModel()), 1,1);

        // 3. Postcondiciones
        assertNotNull(paginator);
        assertEquals(1, paginator.getPage());
        assertEquals(1, paginator.getPageSize());
        assertEquals(1, paginator.getRemainingPages());
        assertEquals(2, (long) paginator.getTotalCount());
        assertEquals(2, paginator.getTotalPages());
        assertEquals(1, paginator.getModels().size());
    }

    /* --------------------- MÉTODO: staffDao.findBy(String name, String surname, Collection<Office>, Collection<StaffSpecialty>, Collection<Locality>, int page, int pageSize) -------------------------------------------- */

    @Test
    public void testStaffFindByNameOfficeStaffSpecialtyLocalityPage()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        Paginator<Staff> paginator = this.staffDao.findBy(FIRST_NAME, SURNAME, Collections.singletonList(officeModel()), Collections.emptyList(), Collections.singletonList(localityModel()), 1,1);

        // 3. Postcondiciones
        assertNotNull(paginator);
        assertEquals(1, paginator.getPage());
        assertEquals(1, paginator.getPageSize());
        assertEquals(0, paginator.getRemainingPages());
        assertEquals(1, (long) paginator.getTotalCount());
        assertEquals(1, paginator.getTotalPages());
        assertEquals(1, paginator.getModels().size());
    }

    @Test
    public void testStaffFindByNameOfficeStaffSpecialtyLocalityPageNullAll()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        Paginator<Staff> paginator = this.staffDao.findBy((String)null, null, null, null, null,1,1);

        // 3. Postcondiciones
        assertNotNull(paginator);
        assertEquals(1, paginator.getPage());
        assertEquals(1, paginator.getPageSize());
        assertEquals(1, paginator.getRemainingPages());
        assertEquals(2, (long) paginator.getTotalCount());
        assertEquals(2, paginator.getTotalPages());
        assertEquals(1, paginator.getModels().size());
    }

    @Test
    public void testStaffFindByNameOfficeStaffSpecialtyLocalityPageOnlyFirstName()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        Paginator<Staff> paginator = this.staffDao.findBy(FIRST_NAME, null, null, null, null, 1,1);

        // 3. Postcondiciones
        assertNotNull(paginator);
        assertEquals(1, paginator.getPage());
        assertEquals(1, paginator.getPageSize());
        assertEquals(0, paginator.getRemainingPages());
        assertEquals(1, (long) paginator.getTotalCount());
        assertEquals(1, paginator.getTotalPages());
        assertEquals(1, paginator.getModels().size());
    }

    @Test
    public void testStaffFindByNameOfficeStaffSpecialtyLocalityPageOnlySurname()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        Paginator<Staff> paginator = this.staffDao.findBy(null, SURNAME, null, null, null, 1,1);

        // 3. Postcondiciones
        assertNotNull(paginator);
        assertEquals(1, paginator.getPage());
        assertEquals(1, paginator.getPageSize());
        assertEquals(0, paginator.getRemainingPages());
        assertEquals(1, (long) paginator.getTotalCount());
        assertEquals(1, paginator.getTotalPages());
        assertEquals(1, paginator.getModels().size());
    }

    @Test
    public void testStaffFindByNameOfficeStaffSpecialtyLocalityPageOnlyOffice()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        Paginator<Staff> paginator = this.staffDao.findBy((String) null, null, Collections.singleton(officeModel()), null, null, 1, 1);

        // 3. Postcondiciones
        assertNotNull(paginator);
        assertEquals(1, paginator.getPage());
        assertEquals(1, paginator.getPageSize());
        assertEquals(0, paginator.getRemainingPages());
        assertEquals(1, (long)paginator.getTotalCount());
        assertEquals(1, paginator.getTotalPages());
        assertEquals(1, paginator.getModels().size());
    }

    @Test
    public void testStaffFindByNameOfficeStaffSpecialtyLocalityPageOnlySpecialties()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        Paginator<Staff> paginator = this.staffDao.findBy((String) null, null, null, Collections.emptyList(), null, 1,1);

        // 3. Postcondiciones
        assertNotNull(paginator);
        assertEquals(1, paginator.getPage());
        assertEquals(1, paginator.getPageSize());
        assertEquals(1, paginator.getRemainingPages());
        assertEquals(2, (long) paginator.getTotalCount());
        assertEquals(2, paginator.getTotalPages());
        assertEquals(1, paginator.getModels().size());
    }

    @Test
    public void testStaffFindByNameOfficeStaffSpecialtyLocalityPageOnlyLocality()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        insertAnotherStaff();

        // 2. Ejercitar
        Paginator<Staff> paginator = this.staffDao.findBy((String) null, null, null, null, Collections.singleton(localityModel()), 1, 1);

        // 3. Postcondiciones
        assertNotNull(paginator);
        assertEquals(1, paginator.getPage());
        assertEquals(1, paginator.getPageSize());
        assertEquals(1, paginator.getRemainingPages());
        assertEquals(2, (long) paginator.getTotalCount());
        assertEquals(2, paginator.getTotalPages());
        assertEquals(1, paginator.getModels().size());
    }
}