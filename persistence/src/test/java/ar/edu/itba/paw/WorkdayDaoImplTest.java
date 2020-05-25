package ar.edu.itba.paw;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.WorkdayDaoImpl;
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
public class WorkdayDaoImplTest {
    private static final String OFFICE_NAME = "Hospital Nacional";
    public static final String NAME_2 = OFFICE_NAME + "_1";
    private static final String OFFICE_STREET = "Av 9 de Julio";
    private static final String PROVINCE = "Buenos Aires";
    private static final String LOCALITY = "Capital Federal";
    private static final String OFFICE_PHONE = "1234567890";
    private static final String OFFICE_EMAIL = "test@test.com";
    private static final int OFFICE_STREET_NUMBER = 123;
    private static final String COUNTRY = "Argentina";
    private static final String COUNTRY_ID = "AR";

    private static final String STAFF_NAME = "Juan";
    private static final String STAFF_SURNAME = "Perez";
    private static final String STAFF_PHONE = "12345678";
    private static final String STAFF_EMAIL = "juan@perez.com";
    private static final Integer STAFF_REGISTRATION_NUMBER = 1234;

    private static final Integer START_HOUR = 9;
    private static final Integer END_HOUR = 18;
    private static final Integer START_MINUTE = 30;
    private static final Integer END_MINUTE = 15;
    private static final Integer END_MINUTE_2 = 30;
    private static final WorkdayDay DAY_OF_WEEK = WorkdayDay.MONDAY;
    private static final WorkdayDay DAY_OF_WEEK_2 = WorkdayDay.TUESDAY;


    private static final String OFFICE_TABLE = "office";
    private static final String LOCALITY_TABLE = "system_locality";
    private static final String PROVINCE_TABLE = "system_province";
    private static final String COUNTRY_TABLE = "system_country";
    private static final String STAFF_TABLE = "staff";
    private static final String WORKDAY_TABLE = "workday";

    private WorkdayDaoImpl workdayDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert officeJdbcInsert;
    private SimpleJdbcInsert localityJdbcInsert;
    private SimpleJdbcInsert provinceJdbcInsert;
    private SimpleJdbcInsert countryJdbcInsert;
    private SimpleJdbcInsert staffJdbcInsert;
    private SimpleJdbcInsert workdayJdbcInsert;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        this.workdayDao = new WorkdayDaoImpl();
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
        this.staffJdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(STAFF_TABLE)
                .usingGeneratedKeyColumns("staff_id");
        this.workdayJdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(WORKDAY_TABLE)
                .usingGeneratedKeyColumns("workday_id");
    }

    private void cleanAllTables() {
        this.jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
    }

    private void insertLocality() {
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

    private void insertOffice() {
        insertLocality();

        Map<String, Object> officeMap = new HashMap<>();
        officeMap.put("name", OFFICE_NAME);
        officeMap.put("email", OFFICE_EMAIL);
        officeMap.put("phone", OFFICE_PHONE);
        officeMap.put("locality_id", 0); // Identity de HSQLDB empieza en 0
        officeMap.put("street", OFFICE_STREET);
        officeMap.put("street_number", OFFICE_STREET_NUMBER);
        officeJdbcInsert.execute(officeMap);
    }

    private Staff staffModel() {
        Staff staff = new Staff();
        staff.setId(0);
        staff.setFirstName(STAFF_NAME);
        staff.setSurname(STAFF_SURNAME);
        staff.setEmail(STAFF_EMAIL);
        staff.setPhone(STAFF_PHONE);
        staff.setRegistrationNumber(STAFF_REGISTRATION_NUMBER);
        return staff;
    }

    private void insertStaff() {
        this.insertOffice();

        Map<String, Object> staffMap = new HashMap<>();
        staffMap.put("office_id", 0);
        staffMap.put("first_name", STAFF_NAME);
        staffMap.put("surname", STAFF_SURNAME);
        staffMap.put("email", STAFF_EMAIL);
        staffMap.put("phone", STAFF_PHONE);
        staffMap.put("registration_number", STAFF_REGISTRATION_NUMBER); // Identity de HSQLDB empieza en 0
        this.staffJdbcInsert.execute(staffMap);
    }

    private void insertWorkday() {
        insertStaff();
        Map<String, Object> workdayMap = new HashMap<>();
        workdayMap.put("staff_id", 0);
        workdayMap.put("start_hour", START_HOUR);
        workdayMap.put("start_minute", START_MINUTE);
        workdayMap.put("end_hour", END_HOUR);
        workdayMap.put("end_minute", END_MINUTE);
        workdayMap.put("day", DAY_OF_WEEK);
        this.workdayJdbcInsert.execute(workdayMap);
    }

    private void insertAnotherWorkday() {
        Map<String, Object> workdayMap = new HashMap<>();
        workdayMap.put("staff_id", 0);
        workdayMap.put("start_hour", START_HOUR);
        workdayMap.put("start_minute", START_MINUTE);
        workdayMap.put("end_hour", END_HOUR);
        workdayMap.put("end_minute", END_MINUTE);
        workdayMap.put("day", DAY_OF_WEEK_2);
        this.workdayJdbcInsert.execute(workdayMap);
    }

    private Country countryModel() {
        Country c = new Country();
        c.setName(COUNTRY);
        c.setId(COUNTRY_ID);
        return c;
    }

    private Locality localityModel() {
        Locality l = new Locality();
        l.setName(LOCALITY);
        l.setId(0); // Identity de HSQLDB empieza en 0
        return l;
    }

    private Office officeModel() {
        Office o = new Office();
        o.setId(0);
        o.setName(OFFICE_NAME);
        o.setEmail(OFFICE_EMAIL);
        o.setPhone(OFFICE_PHONE);
        o.setLocality(localityModel());
        o.setStreet(OFFICE_STREET);
        return o;
    }

    private Workday workdayModel() {
        Workday workday = new Workday();
        workday.setId(0);
        workday.setDay(DAY_OF_WEEK);
        workday.setStartHour(START_HOUR);
        workday.setEndHour(END_HOUR);
        workday.setStartMinute(START_MINUTE);
        workday.setEndMinute(END_MINUTE);
        workday.setStaff(staffModel());
        return workday;
    }

    @Test
    public void testCreateWorkday() {
        // 1. Precondiciones
        cleanAllTables();
        insertStaff();
        Workday workday = this.workdayModel();

        // 2. Ejercitar
        workday = this.workdayDao.create(workday);

        // 3. Postcondiciones
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, WORKDAY_TABLE));
        assertEquals(DAY_OF_WEEK, workday.getDay());
    }

    @Test
    public void testFindById() {
        // 1. Precondiciones
        cleanAllTables();
        insertWorkday();

        // 2. Ejercitar
        Optional<Workday> maybeWorkday = workdayDao.findById(0); // Identity de HSQLDB empieza en 0

        // 3. Postcondiciones
        assertTrue(maybeWorkday.isPresent());
        assertEquals(0, (int) maybeWorkday.get().getId());
        assertEquals(DAY_OF_WEEK, maybeWorkday.get().getDay());
    }

    @Test
    public void testFindByIdDoesntExist() {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Optional<Workday> maybeWorkday = workdayDao.findById(0); // Identity de HSQLDB empieza en 0

        // 3. Postcondiciones
        assertFalse(maybeWorkday.isPresent());
    }

    @Test
    public void testFindByIds() {
        // 1. Precondiciones
        cleanAllTables();
        insertWorkday();
        insertAnotherWorkday();

        // 2. Ejercitar
        Collection<Workday> workdays = workdayDao.findByIds(Arrays.asList(0, 1, 2)); // Identity de HSQLDB empieza en 0

        // 3. Postcondiciones
        assertNotNull(workdays);
        assertEquals(2, workdays.size());
    }

    @Test
    public void testFindByIdsDoesntExist() {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<Workday> workdays = workdayDao.findByIds(Arrays.asList(0, 1, 2)); // Identity de HSQLDB empieza en 0

        // 3. Postcondiciones
        assertNotNull(workdays);
        assertTrue(workdays.isEmpty());
    }

    @Test
    public void testList() {
        // 1. Precondiciones
        // Vaciar tablas
        cleanAllTables();
        insertWorkday();
        insertAnotherWorkday();

        // 2. Ejercitar
        Collection<Workday> workdays = workdayDao.list();

        // 3. Postcondiciones
        assertNotNull(workdays);
        assertEquals(2, workdays.size());
    }

    @Test
    public void testEmptyList() {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<Workday> workdays = workdayDao.list();

        // 3. Postcondiciones
        assertNotNull(workdays);
        assertTrue(workdays.isEmpty());
    }

    @Test
    public void testRemoveById() {
        // 1. Precondiciones
        cleanAllTables();
        insertWorkday();
        insertAnotherWorkday();

        // 2. Ejercitar
        workdayDao.remove(0);

        // 3. Postcondiciones
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, WORKDAY_TABLE));
    }

    @Test
    public void testRemoveByModel() {
        // 1. Precondiciones
        cleanAllTables();
        insertWorkday();
        insertAnotherWorkday();

        // 2. Ejercitar
        workdayDao.remove(workdayModel());

        // 3. Postcondiciones
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, OFFICE_TABLE));
    }

    @Test
    public void testUpdate() {
        // 1. Precondiciones
        cleanAllTables();
        insertWorkday();

        // Modelo de la oficina a crear
        Workday w = this.workdayDao.findById(workdayModel().getId()).get();
        w.setEndMinute(END_MINUTE_2);

        // 2. Ejercitar
        workdayDao.update(w);

        // 3. Postcondiciones
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, WORKDAY_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, WORKDAY_TABLE, "end_minute = " + END_MINUTE_2));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, WORKDAY_TABLE, "end_minute = " + END_MINUTE));

    }
}
