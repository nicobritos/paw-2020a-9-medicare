package ar.edu.itba.paw.nocache;

import ar.edu.itba.paw.models.StaffSpecialty;
import ar.edu.itba.paw.persistence.StaffSpecialtyDaoImpl;
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
public class StaffSpecialityDaoImplTest {
    private static final String STAFF_SPECIALTY = "Oftalmologo";
    private static final String SPECIALTIES_TABLE = "system_staff_specialty";

    private StaffSpecialtyDaoImpl staffSpecialtyDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert staffSpecialtyInsert;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        this.staffSpecialtyDao = new StaffSpecialtyDaoImpl(this.ds);
        this.jdbcTemplate = new JdbcTemplate(this.ds);
        this.staffSpecialtyInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(SPECIALTIES_TABLE)
                .usingGeneratedKeyColumns("speciality_id");
    }

    private void cleanAllTables() {
        this.jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
    }

    private StaffSpecialty staffSpecialtyModel() {
        StaffSpecialty ss = new StaffSpecialty();
        ss.setName(STAFF_SPECIALTY);
        ss.setId(0);
        return ss;
    }

    private void insertStaffSpeciality() {
        Map<String, Object> ssMap = new HashMap<>();
        ssMap.put("name", STAFF_SPECIALTY);
        staffSpecialtyInsert.execute(ssMap);
    }

    private void insertAnotherStaffSpeciality() {
        Map<String, Object> ssMap = new HashMap<>();
        ssMap.put("name", "Dentista");
        staffSpecialtyInsert.execute(ssMap);
    }


    @Test
    public void testCreateStaffSpecialty() {
        // 1. Precondiciones
        cleanAllTables();
        StaffSpecialty ss = staffSpecialtyModel();

        // 2. Ejercitar
        StaffSpecialty staffSpecialty = this.staffSpecialtyDao.create(ss);

        // 3. Postcondiciones
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, SPECIALTIES_TABLE));
        assertEquals(STAFF_SPECIALTY, staffSpecialty.getName());
        assertEquals(0, (int) staffSpecialty.getId()); // Identity de HSQLDB empieza en 0
    }

    @Test
    public void testFindById() {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpeciality();

        // 2. Ejercitar
        Optional<StaffSpecialty> maybeStaffSpecialty = staffSpecialtyDao.findById(0); // Identity de HSQLDB empieza en 0

        // 3. Postcondiciones
        assertTrue(maybeStaffSpecialty.isPresent());
        assertEquals(0, (int) maybeStaffSpecialty.get().getId());
        assertEquals(STAFF_SPECIALTY, maybeStaffSpecialty.get().getName());
    }

    @Test
    public void testFindByIdDoesntExist() {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Optional<StaffSpecialty> maybeStaffSpecialty = staffSpecialtyDao.findById(0); // Identity de HSQLDB empieza en 0

        // 3. Postcondiciones
        assertFalse(maybeStaffSpecialty.isPresent());
    }

    @Test
    public void testFindByIds() {
        // 1. Precondiciones
        cleanAllTables();

        insertStaffSpeciality();
        insertAnotherStaffSpeciality();

        // 2. Ejercitar
        Collection<StaffSpecialty> staffSpecialties = staffSpecialtyDao.findByIds(Arrays.asList(0, 1, 2)); // Identity de HSQLDB empieza en 0

        // 3. Postcondiciones
        assertNotNull(staffSpecialties);
        assertEquals(2, staffSpecialties.size());
    }

    @Test
    public void testFindByIdsDoesntExist() {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<StaffSpecialty> staffSpecialties = staffSpecialtyDao.findByIds(Arrays.asList(0, 1, 2)); // Identity de HSQLDB empieza en 0

        // 3. Postcondiciones
        assertNotNull(staffSpecialties);
        assertTrue(staffSpecialties.isEmpty());
    }

    @Test
    public void testFindByField() {
        // 1. Precondiciones
        cleanAllTables();

        insertStaffSpeciality();
        insertAnotherStaffSpeciality();

        // 2. Ejercitar
        Collection<StaffSpecialty> staffSpecialties = staffSpecialtyDao.findByField("name", STAFF_SPECIALTY);

        // 3. Postcondiciones
        assertNotNull(staffSpecialties);
        assertEquals(1, staffSpecialties.size());
    }

    @Test
    public void testFindByFieldDoesntExist() {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<StaffSpecialty> staffSpecialties = staffSpecialtyDao.findByField("name", STAFF_SPECIALTY);

        // 3. Postcondiciones
        assertNotNull(staffSpecialties);
        assertTrue(staffSpecialties.isEmpty());
    }

    @Test
    public void testFindByFieldOp() {
        // 1. Precondiciones
        cleanAllTables();

        insertStaffSpeciality();
        insertAnotherStaffSpeciality();

        // 2. Ejercitar
        Collection<StaffSpecialty> staffSpecialties = staffSpecialtyDao.findByField("specialty_id", JDBCWhereClauseBuilder.Operation.GEQ, 1);

        // 3. Postcondiciones
        assertNotNull(staffSpecialties);
        assertEquals(1, staffSpecialties.size());
    }

    @Test
    public void testFindByFieldOpDoesntExist() {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<StaffSpecialty> staffSpecialties = staffSpecialtyDao.findByField("specialty_id", JDBCWhereClauseBuilder.Operation.GEQ, 1);

        // 3. Postcondiciones
        assertNotNull(staffSpecialties);
        assertTrue(staffSpecialties.isEmpty());
    }

    @Test
    public void testFindByName() {
        // 1. Precondiciones
        cleanAllTables();

        insertStaffSpeciality();
        insertAnotherStaffSpeciality();

        // 2. Ejercitar
        Collection<StaffSpecialty> staffSpecialties = staffSpecialtyDao.findByName(STAFF_SPECIALTY);

        // 3. Postcondiciones
        assertNotNull(staffSpecialties);
        assertEquals(1, staffSpecialties.size());
    }

    @Test
    public void testFindByNameDoesntExist() {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<StaffSpecialty> staffSpecialties = staffSpecialtyDao.findByName(STAFF_SPECIALTY);

        // 3. Postcondiciones
        assertNotNull(staffSpecialties);
        assertTrue(staffSpecialties.isEmpty());
    }

    @Test
    public void testList() {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpeciality();
        insertAnotherStaffSpeciality();

        // 2. Ejercitar
        Collection<StaffSpecialty> staffSpecialties = staffSpecialtyDao.list();

        // 3. Postcondiciones
        assertNotNull(staffSpecialties);
        assertEquals(2, staffSpecialties.size());
    }

    @Test
    public void testEmptyList() {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<StaffSpecialty> staffSpecialties = staffSpecialtyDao.list();

        // 3. Postcondiciones
        assertNotNull(staffSpecialties);
        assertTrue(staffSpecialties.isEmpty());
    }

    @Test
    public void testRemoveById() {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpeciality();
        insertAnotherStaffSpeciality();

        // 2. Ejercitar
        staffSpecialtyDao.remove(0);

        // 3. Postcondiciones
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, SPECIALTIES_TABLE));
    }

    @Test
    public void testRemoveByModel() {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpeciality();
        insertAnotherStaffSpeciality();

        // Modelo de la oficina a crear
        StaffSpecialty s = staffSpecialtyModel();

        // 2. Ejercitar
        staffSpecialtyDao.remove(s);

        // 3. Postcondiciones
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, SPECIALTIES_TABLE));
    }

    @Test
    public void testUpdate() {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpeciality();
        StaffSpecialty s = staffSpecialtyModel();
        s.setName(STAFF_SPECIALTY + " (updated)");

        // 2. Ejercitar
        staffSpecialtyDao.update(s);

        // 3. Postcondiciones
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, SPECIALTIES_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SPECIALTIES_TABLE, "name = '" + STAFF_SPECIALTY + " (updated)'"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SPECIALTIES_TABLE, "name = '" + STAFF_SPECIALTY + "'"));

    }
}