package ar.edu.itba.paw;

import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Province;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;
import ar.edu.itba.paw.persistence.StaffDaoImpl;
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
public class StaffDaoImplTest
{
    private static final String OFFICE_NAME = "Hospital Nacional";
    private static final String STREET = "Av 9 de Julio";
    private static final String PROVINCE = "Buenos Aires";
    private static final String PHONE = "1234567890";
    private static final String EMAIL = "test@test.com";
    private static final int STREET_NUMBER = 123;
    private static final String COUNTRY = "Argentina";
    private static final String COUNTRY_ID = "AR";
    private static final String NAME = "Juan";
    private static final String SURNAME = "Perez";
    private static final int REGISTRATION_NUMBER = 123;
    private static final String SPECIALTY = "Oftalmologo";

    private static final String SPECIALTY_TABLE = "system_staff_specialty";
    private static final String STAFF_TABLE = "staff";
    private static final String OFFICE_TABLE = "office";
    private static final String PROVINCE_TABLE = "system_province";
    private static final String COUNTRY_TABLE = "system_country";
    private static final String SPECIALTY_STAFF_TABLE = "system_staff_specialty_staff";


    private StaffDaoImpl staffDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert staffSpecialtyStaffInsert;
    private SimpleJdbcInsert staffSpecialtyInsert;
    private SimpleJdbcInsert staffJdbcInsert;
    private SimpleJdbcInsert officeJdbcInsert;
    private SimpleJdbcInsert provinceJdbcInsert;
    private SimpleJdbcInsert countryJdbcInsert;

    @Autowired
    private DataSource ds;

    private void cleanAllTables(){
        JdbcTestUtils.deleteFromTables(this.jdbcTemplate, SPECIALTY_STAFF_TABLE);
        JdbcTestUtils.deleteFromTables(this.jdbcTemplate, SPECIALTY_TABLE);
        JdbcTestUtils.deleteFromTables(this.jdbcTemplate, STAFF_TABLE);
        JdbcTestUtils.deleteFromTables(this.jdbcTemplate, OFFICE_TABLE);
        JdbcTestUtils.deleteFromTables(this.jdbcTemplate, PROVINCE_TABLE);
        JdbcTestUtils.deleteFromTables(this.jdbcTemplate, COUNTRY_TABLE);
        this.jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
    }

    private void insertOffice(){
        // Insertar pais
        Map<String, Object> countryMap = new HashMap<>();
        countryMap.put("name", COUNTRY);
        countryMap.put("country_id", COUNTRY_ID);
        this.countryJdbcInsert.execute(countryMap);

        // Insertar provincia
        Map<String, Object> provinceMap = new HashMap<>();
        provinceMap.put("name", PROVINCE);
        provinceMap.put("country_id", COUNTRY_ID);
        this.provinceJdbcInsert.execute(provinceMap);

        // Insertar oficina
        Map<String, Object> officeMap = new HashMap<>();
        officeMap.put("name", OFFICE_NAME);
        officeMap.put("email", EMAIL);
        officeMap.put("phone", PHONE);
        officeMap.put("province_id", 0); // Identity de HSQLDB empieza en 0
        officeMap.put("street", STREET);
        officeMap.put("street_number", STREET_NUMBER);
        this.officeJdbcInsert.execute(officeMap);
    }

    private void insertStaff(){
        this.insertOffice();

        Map<String, Object> staffMap = new HashMap<>();
        staffMap.put("office_id", 0);
        staffMap.put("first_name", NAME);
        staffMap.put("surname", SURNAME);
        staffMap.put("email", EMAIL);
        staffMap.put("phone", PHONE);
        staffMap.put("registration_number", REGISTRATION_NUMBER); // Identity de HSQLDB empieza en 0
        this.staffJdbcInsert.execute(staffMap);

        Map<String, Object> staffSpecialtyMap = new HashMap<>();
        staffSpecialtyMap.put("name", SPECIALTY);
        this.staffSpecialtyInsert.execute(staffSpecialtyMap);

        Map<String, Object> staffSpecialtyStaffMap = new HashMap<>();
        staffSpecialtyStaffMap.put("specialty_id", 0);
        staffSpecialtyStaffMap.put("staff_id", 0);
        this.staffSpecialtyStaffInsert.execute(staffSpecialtyStaffMap);
    }

    private void insertAnotherStaff(){
        Map<String, Object> staffMap = new HashMap<>();
        staffMap.put("office_id", 0);
        staffMap.put("first_name", NAME + "_1");
        staffMap.put("surname", SURNAME + "_1");
        staffMap.put("email", EMAIL);
        staffMap.put("phone", PHONE);
        staffMap.put("registration_number", REGISTRATION_NUMBER); // Identity de HSQLDB empieza en 0
        this.staffJdbcInsert.execute(staffMap);
    }

    private Staff staffModel() {
        Staff s = new Staff();
        s.setFirstName(NAME);
        s.setSurname(SURNAME);
        s.setEmail(EMAIL);
        s.setPhone(PHONE);
        s.setRegistrationNumber(REGISTRATION_NUMBER);
        s.setId(0);
        s.setStaffSpecialties(Collections.singletonList(this.staffSpecialtyModel()));
        return s;
    }

    private StaffSpecialty staffSpecialtyModel() {
        StaffSpecialty ss = new StaffSpecialty();
        ss.setName(SPECIALTY);
        ss.setId(0);
        return ss;
    }

    private Office officeModel() {
        Office o = new Office();
        o.setId(0);
        o.setName(NAME);
        o.setEmail(EMAIL);
        o.setPhone(PHONE);
        o.setProvince(this.provinceModel());
        o.setStaffs(Collections.singletonList(this.staffModel()));
        o.setStreet(STREET);
        o.setStreetNumber(STREET_NUMBER);
        return o;
    }

    private Province provinceModel() {
        Province p = new Province();
        p.setName(PROVINCE);
        p.setId(0); // Identity de HSQLDB empieza en 0
        return p;
    }

    @Before
    public void setUp(){
        this.staffDao = new StaffDaoImpl(this.ds);
        this.jdbcTemplate = new JdbcTemplate(this.ds);
        this.staffSpecialtyStaffInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(SPECIALTY_STAFF_TABLE);
        this.staffSpecialtyInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(SPECIALTY_TABLE)
                .usingGeneratedKeyColumns("specialty_id");
        this.staffJdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(STAFF_TABLE)
                .usingGeneratedKeyColumns("staff_id");
        this.officeJdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(OFFICE_TABLE)
                .usingGeneratedKeyColumns("office_id");
        this.provinceJdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(PROVINCE_TABLE)
                .usingGeneratedKeyColumns("province_id");
        this.countryJdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(COUNTRY_TABLE);
    }

    @Test
    public void testCreateStaff()
    {
        // 1. Precondiciones
        // Vaciar tablas
        this.cleanAllTables();

        this.insertOffice();

        Staff s = this.staffModel();

        // 2. Ejercitar
        Staff staff = this.staffDao.create(s);

        // 3. Postcondiciones
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, OFFICE_TABLE));
        assertEquals(NAME, staff.getFirstName());
        assertEquals(0, (int)staff.getId()); // Identity de HSQLDB empieza en 0
    }

    @Test
    public void testFindById(){
        // 1. Precondiciones
        this.cleanAllTables();
        this.insertStaff();

        // 2. Ejercitar
        Optional<Staff> maybeStaff = this.staffDao.findById(0); // Identity de HSQLDB empieza en 0

        // 3. Postcondiciones
        assertTrue(maybeStaff.isPresent());
        assertEquals(0, (int)maybeStaff.get().getId());
        assertEquals(NAME, maybeStaff.get().getFirstName());
    }

    @Test
    public void testFindByIdDoesntExist(){
        // 1. Precondiciones
        this.cleanAllTables();

        // 2. Ejercitar
        Optional<Staff> maybeStaff = this.staffDao.findById(0); // Identity de HSQLDB empieza en 0

        // 3. Postcondiciones
        assertFalse(maybeStaff.isPresent());
    }

    @Test
    public void testFindByIds(){
        // 1. Precondiciones
        // Vaciar tablas
        this.cleanAllTables();

        this.insertStaff();
        this.insertAnotherStaff();

        // 2. Ejercitar
        Collection<Staff> staffs = this.staffDao.findByIds(Arrays.asList(0,1,2)); // Identity de HSQLDB empieza en 0

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(2, staffs.size());
    }

    @Test
    public void testFindByIdsDoesntExist(){
        // 1. Precondiciones
        this.cleanAllTables();

        // 2. Ejercitar
        Collection<Staff> staffs = this.staffDao.findByIds(Arrays.asList(0,1,2)); // Identity de HSQLDB empieza en 0

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertTrue(staffs.isEmpty());
    }

    @Test
    public void testFindByField(){
        // 1. Precondiciones
        this.cleanAllTables();

        this.insertStaff();
        this.insertAnotherStaff();

        // 2. Ejercitar
        Collection<Staff> staffs = this.staffDao.findByField("email", EMAIL);

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(2, staffs.size());
    }

    @Test
    public void testFindByFieldDoesntExist(){
        // 1. Precondiciones
        this.cleanAllTables();

        // 2. Ejercitar
        Collection<Staff> staffs = this.staffDao.findByField("email", EMAIL);

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertTrue(staffs.isEmpty());
    }

    @Test
    public void testFindByFieldOp(){
        // 1. Precondiciones
        this.cleanAllTables();

        this.insertStaff();
        this.insertAnotherStaff();

        // 2. Ejercitar
        Collection<Staff> staffs = this.staffDao.findByField("staff_id", JDBCWhereClauseBuilder.Operation.GEQ, 1);

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(1, staffs.size());
    }

    @Test
    public void testFindByFieldOpDoesntExist(){
        // 1. Precondiciones
        this.cleanAllTables();

        // 2. Ejercitar
        Collection<Staff> staffs = this.staffDao.findByField("staff_id", JDBCWhereClauseBuilder.Operation.GEQ, 1);

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertTrue(staffs.isEmpty());
    }

    @Test
    public void testFindByName(){
        // 1. Precondiciones
        this.cleanAllTables();

        this.insertStaff();
        this.insertAnotherStaff();

        // 2. Ejercitar
        Collection<Staff> staffs = this.staffDao.findByName(NAME);

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(1, staffs.size());
    }

    @Test
    public void testFindByNameDoesntExist(){
        // 1. Precondiciones
        this.cleanAllTables();

        // 2. Ejercitar
        Collection<Staff> staffs = this.staffDao.findByName(NAME);

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertTrue(staffs.isEmpty());
    }

    @Test
    public void testList(){
        // 1. Precondiciones
        this.cleanAllTables();
        this.insertStaff();
        this.insertAnotherStaff();

        // 2. Ejercitar
        Collection<Staff> staffs = this.staffDao.list();

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(2, staffs.size());
    }
//
    @Test
    public void testEmptyList(){
        // 1. Precondiciones
        this.cleanAllTables();

        // 2. Ejercitar
        Collection<Staff> staffs = this.staffDao.list();

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertTrue(staffs.isEmpty());
    }

    @Test
    public void testRemoveById(){
        // 1. Precondiciones
        this.cleanAllTables();
        this.insertStaff();
        this.insertAnotherStaff();
        JdbcTestUtils.deleteFromTables(this.jdbcTemplate, SPECIALTY_STAFF_TABLE); // Borro el speciality linkeado al staff a borrar

        // 2. Ejercitar
        this.staffDao.remove(0);

        // 3. Postcondiciones
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, STAFF_TABLE));
    }

    @Test
    public void testRemoveByModel(){
        // 1. Precondiciones
        this.cleanAllTables();
        this.insertStaff();
        this.insertAnotherStaff();
        JdbcTestUtils.deleteFromTables(this.jdbcTemplate, SPECIALTY_STAFF_TABLE); // Borro el speciality linkeado al staff a borrar

        // Modelo de la oficina a crear
        Staff s = this.staffModel();

        // 2. Ejercitar
        this.staffDao.remove(s);

        // 3. Postcondiciones
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, STAFF_TABLE));
    }

    @Test
    public void testUpdate(){
        // 1. Precondiciones
        this.cleanAllTables();

        this.insertStaff();

        Staff s = this.staffModel();
        s.setFirstName(NAME + " (updated)");

        // 2. Ejercitar
        this.staffDao.update(s);

        // 3. Postcondiciones
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, STAFF_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(this.jdbcTemplate, STAFF_TABLE, "first_name = '" + NAME + " (updated)'"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(this.jdbcTemplate, STAFF_TABLE, "first_name = '" + NAME + "'"));

    }

    @Test
    public void testFindByNameAndStaffSpecialties(){
        // 1. Precondiciones
        this.cleanAllTables();
        this.insertStaff();
        this.insertAnotherStaff();

        // 2. Ejercitar
        Collection<Staff> staffs = this.staffDao.findByNameAndStaffSpecialties(NAME, Collections.singletonList(this.staffSpecialtyModel()));

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(1, staffs.size());
    }

    @Test
    public void testFindByNameAndStaffSpecialtiesDoesntExist(){
        // 1. Precondiciones
        this.cleanAllTables();

        // 2. Ejercitar
        Collection<Staff> staffs = this.staffDao.findByNameAndStaffSpecialties(NAME, Collections.singletonList(this.staffSpecialtyModel()));

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertTrue(staffs.isEmpty());
    }

    @Test
    public void testFindByNameOfficeAndStaffSpecialties(){
        // 1. Precondiciones
        this.cleanAllTables();
        this.insertStaff();
        this.insertAnotherStaff();

        // 2. Ejercitar
        Collection<Staff> staffs = this.staffDao.findByNameOfficeAndStaffSpecialties(NAME, Collections.singletonList(this.officeModel()), Collections.singletonList(this.staffSpecialtyModel()));

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(1, staffs.size());
    }

    @Test
    public void testFindByNameOfficeAndStaffSpecialitiesDoesntExist(){
        // 1. Precondiciones
        this.cleanAllTables();

        // 2. Ejercitar
        Collection<Staff> staffs = this.staffDao.findByNameOfficeAndStaffSpecialties(NAME, Collections.singletonList(this.officeModel()), Collections.singletonList(this.staffSpecialtyModel()));

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertTrue(staffs.isEmpty());
    }

    @Test
    public void testFindByOfficeAndStaffSpecialties(){
        // 1. Precondiciones
        this.cleanAllTables();
        this.insertStaff();
        this.insertAnotherStaff();

        // 2. Ejercitar
        Collection<Staff> staffs = this.staffDao.findByOfficeAndStaffSpecialties(Collections.singletonList(this.officeModel()), Collections.singletonList(this.staffSpecialtyModel()));

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(1, staffs.size());
    }

    @Test
    public void testFindByOfficeAndStaffSpecialitiesDoesntExist(){
        // 1. Precondiciones
        this.cleanAllTables();

        // 2. Ejercitar
        Collection<Staff> staffs = this.staffDao.findByOfficeAndStaffSpecialties(Collections.singletonList(this.officeModel()), Collections.singletonList(this.staffSpecialtyModel()));

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertTrue(staffs.isEmpty());
    }

    @Test
    public void testFindByStaffSpecialties(){
        // 1. Precondiciones
        this.cleanAllTables();
        this.insertStaff();
        this.insertAnotherStaff();

        // 2. Ejercitar
        Collection<Staff> staffs = this.staffDao.findByStaffSpecialties(Collections.singletonList(this.staffSpecialtyModel()));

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertEquals(1, staffs.size());
    }

    @Test
    public void testFindByStaffSpecialitiesDoesntExist(){
        // 1. Precondiciones
        this.cleanAllTables();

        // 2. Ejercitar
        Collection<Staff> staffs = this.staffDao.findByStaffSpecialties(Collections.singletonList(this.staffSpecialtyModel()));

        // 3. Postcondiciones
        assertNotNull(staffs);
        assertTrue(staffs.isEmpty());
    }
}
