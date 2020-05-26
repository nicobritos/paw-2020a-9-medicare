package ar.edu.itba.paw;

import ar.edu.itba.paw.models.ModelMetadata;
import ar.edu.itba.paw.models.StaffSpecialty;
import ar.edu.itba.paw.persistence.StaffSpecialtyDaoImpl;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
@Sql(scripts = "classpath:sql/schema.sql")
@ContextConfiguration(classes = TestConfig.class)
public class StaffSpecialtyDaoImplTest {
    private static final int STARTING_ID = 0;
    private static final String STAFF_SPECIALTY = "Oftalmologo";
    private static final Object STAFF_SPECIALTY_2 = "Dentista";
    
    private static final String STAFF_SPECIALTIES_TABLE = "system_staff_specialty";

    private StaffSpecialtyDaoImpl staffSpecialtyDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert staffSpecialtyInsert;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        this.staffSpecialtyDao = new StaffSpecialtyDaoImpl(this.ds);
        this.jdbcTemplate = new JdbcTemplate(this.ds);
        this.staffSpecialtyInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(STAFF_SPECIALTIES_TABLE)
                .usingGeneratedKeyColumns("speciality_id");
    }

    /* ---------------------- FUNCIONES AUXILIARES ---------------------------------------------------------------- */

    private void cleanAllTables() {
        this.jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
    }

    /** Devuelve una StaffSpecialty con name=STAFF_SPECIALTY **/
    private StaffSpecialty staffSpecialtyModel() {
        StaffSpecialty ss = new StaffSpecialty();
        ss.setName(STAFF_SPECIALTY);
        ss.setId(STARTING_ID);
        return ss;
    }

    /** Inserta en la db la imagen con name=STAFF_SPECIALTY **/
    private void insertStaffSpecialty() {
        Map<String, Object> ssMap = new HashMap<>();
        ssMap.put("name", STAFF_SPECIALTY);
        staffSpecialtyInsert.execute(ssMap);
    }

    /** Inserta en la db la imagen con name=STAFF_SPECIALTY_2 **/
    private void insertAnotherStaffSpecialty() {
        Map<String, Object> ssMap = new HashMap<>();
        ssMap.put("name", STAFF_SPECIALTY_2);
        staffSpecialtyInsert.execute(ssMap);
    }
    /* --------------------- MÉTODO: staffSpecialtyDao.create(StaffSpecialty) -------------------------------------------- */

    @Test
    public void testCreateStaffSpecialtySuccessfully() {
        // 1. Precondiciones
        cleanAllTables();
        StaffSpecialty ss = staffSpecialtyModel();

        // 2. Ejercitar
        StaffSpecialty staffSpecialty = this.staffSpecialtyDao.create(ss);

        // 3. Postcondiciones
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, STAFF_SPECIALTIES_TABLE));
        assertEquals(STAFF_SPECIALTY, staffSpecialty.getName());
    }

    @Test
    public void testCreateAnotherStaffSpecialtySuccessfully() {
        // 1. Precondiciones
        cleanAllTables();
        insertAnotherStaffSpecialty();
        StaffSpecialty ss = staffSpecialtyModel();

        // 2. Ejercitar
        StaffSpecialty staffSpecialty = this.staffSpecialtyDao.create(ss);

        // 3. Postcondiciones
        assertEquals(2, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, STAFF_SPECIALTIES_TABLE));
        assertEquals(STAFF_SPECIALTY, staffSpecialty.getName());
    }

    @Test
    public void testCreateStaffSpecialtyNullFail()
    {
        // 1. Precondiciones
        cleanAllTables();
        expectedException.expect(NullPointerException.class);

        // 2. Ejercitar
        StaffSpecialty staffSpecialty = this.staffSpecialtyDao.create(null);

        // 3. Postcondiciones
        // Que el metodo tire NullPointerException
    }

    @Test
    public void testCreateStaffSpecialtyEmptyStaffSpecialtyFail()
    {
        // 1. Precondiciones
        cleanAllTables();
        StaffSpecialty ss = new StaffSpecialty();
        expectedException.expect(CoreMatchers.anyOf( // Falla si no se tira ninguna de las excepciones de la lista
                CoreMatchers.instanceOf(IllegalStateException.class), // Esta excepcion se tira si no tiene data // TODO: chequear esta excepcion (poco descriptiva)
                CoreMatchers.instanceOf(DataIntegrityViolationException.class) // Esta excepcion se tira si no tiene id // TODO: chequear esta excepcion (poco descriptiva)
        ));

        // 2. Ejercitar
        StaffSpecialty staffSpecialty = this.staffSpecialtyDao.create(ss);

        // 3. Postcondiciones
        // Que el metodo tire IllegalStateException (no data) o DataIntegrityViolationException (no id), se hace esto porque depende de cual chequea primero.
    }

    @Test
    public void testCreateStaffSpecialtyEmptyNameFail()
    {
        // 1. Precondiciones
        cleanAllTables();
        StaffSpecialty ss = staffSpecialtyModel();
        ss.setName(null);
        expectedException.expect(IllegalStateException.class); // TODO: chequear esta excepcion (poco descriptiva)

        // 2. Ejercitar
        StaffSpecialty staffSpecialty = this.staffSpecialtyDao.create(ss);

        // 3. Postcondiciones
        // Que el metodo tire IllegalStateException
    }

    /* --------------------- MÉTODO: staffSpecialtyDao.findById(String) -------------------------------------------- */

    @Test
    public void testFindStaffSpecialtyById()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpecialty();
        insertAnotherStaffSpecialty();

        // 2. Ejercitar
        Optional<StaffSpecialty> staffSpecialty = this.staffSpecialtyDao.findById(STARTING_ID);

        // 3. Postcondiciones
        assertTrue(staffSpecialty.isPresent());
        assertEquals(STAFF_SPECIALTY, staffSpecialty.get().getName());
    }

    @Test
    public void testFindStaffSpecialtyByIdDoesntExist()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpecialty();

        // 2. Ejercitar
        Optional<StaffSpecialty> staffSpecialty = this.staffSpecialtyDao.findById(STARTING_ID + 1);

        // 3. Postcondiciones
        assertFalse(staffSpecialty.isPresent());
    }

    @Test
    public void testFindStaffSpecialtyByIdNull()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpecialty();

        // 2. Ejercitar
        Optional<StaffSpecialty> staffSpecialty = this.staffSpecialtyDao.findById(null);

        // 3. Postcondiciones
        assertFalse(staffSpecialty.isPresent());
    }

    /* --------------------- MÉTODO: staffSpecialtyDao.findByIds(Collection<String>) -------------------------------------------- */

    @Test
    public void testFindStaffSpecialtyByIds()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpecialty();
        insertAnotherStaffSpecialty();

        // 2. Ejercitar
        Collection<StaffSpecialty> staffSpecialties = this.staffSpecialtyDao.findByIds(Arrays.asList(STARTING_ID, STARTING_ID + 1));

        // 3. Postcondiciones
        assertNotNull(staffSpecialties);
        assertEquals(2, staffSpecialties.size());
        for (StaffSpecialty ss : staffSpecialties){
            assertTrue(ss.getId().equals(STARTING_ID) || ss.getId().equals(STARTING_ID + 1));
        }
    }

    @Test
    public void testFindStaffSpecialtyByIdsNotAllPresent()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpecialty();

        // 2. Ejercitar
        Collection<StaffSpecialty> staffSpecialties = this.staffSpecialtyDao.findByIds(Arrays.asList(STARTING_ID, STARTING_ID + 1));

        // 3. Postcondiciones
        assertNotNull(staffSpecialties);
        assertEquals(1, staffSpecialties.size());
        for (StaffSpecialty ss : staffSpecialties){
            assertEquals(staffSpecialtyModel(), ss);
        }
    }

    @Test
    public void testFindStaffSpecialtyByIdsDontExist()
    {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<StaffSpecialty> staffSpecialties = this.staffSpecialtyDao.findByIds(Arrays.asList(STARTING_ID, STARTING_ID + 1));

        // 3. Postcondiciones
        assertNotNull(staffSpecialties);
        assertTrue(staffSpecialties.isEmpty());
    }

    /* --------------------- MÉTODO: staffSpecialtyDao.list() -------------------------------------------- */

    @Test
    public void testStaffSpecialtyList()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpecialty();
        insertAnotherStaffSpecialty();

        // 2. Ejercitar
        Collection<StaffSpecialty> staffSpecialties = this.staffSpecialtyDao.list();

        // 3. Postcondiciones
        assertNotNull(staffSpecialties);
        assertEquals(2, staffSpecialties.size());
    }

    @Test
    public void testStaffSpecialtyEmptyList()
    {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<StaffSpecialty> staffSpecialties = this.staffSpecialtyDao.list();

        // 3. Postcondiciones
        assertNotNull(staffSpecialties);
        assertTrue(staffSpecialties.isEmpty());
    }

    /* --------------------- MÉTODO: staffSpecialtyDao.update(StaffSpecialty) -------------------------------------------- */

    @Test
    public void testStaffSpecialtyUpdate()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpecialty();
        insertAnotherStaffSpecialty();
        StaffSpecialty ss = staffSpecialtyModel();
        ss.setName("Armenia");

        // 2. Ejercitar
        this.staffSpecialtyDao.update(ss);

        // 3. Postcondiciones
        assertEquals(2,JdbcTestUtils.countRowsInTable(jdbcTemplate, STAFF_SPECIALTIES_TABLE));
        assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, STAFF_SPECIALTIES_TABLE, "name = 'Armenia'"));
        assertEquals(0,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, STAFF_SPECIALTIES_TABLE, "name = '"+ STAFF_SPECIALTY +"'"));
    }

    @Test
    public void testStaffSpecialtyUpdateNull()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpecialty();
        insertAnotherStaffSpecialty();
        expectedException.expect(NullPointerException.class);

        // 2. Ejercitar
        this.staffSpecialtyDao.update(null);

        // 3. Postcondiciones
        // Que el metodo tire NullPointerException
        assertEquals(2,JdbcTestUtils.countRowsInTable(jdbcTemplate, STAFF_SPECIALTIES_TABLE));
    }

    @Test
    public void testStaffSpecialtyUpdateNotExistentStaffSpecialty()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertAnotherStaffSpecialty();
        StaffSpecialty ss = staffSpecialtyModel();
        ss.setId(STARTING_ID + 1);
        expectedException.expect(Exception.class);  // <-- TODO: Insert exception class here

        // 2. Ejercitar
        this.staffSpecialtyDao.update(ss); // TODO: NO HACE NADA, DEBERIA TIRAR EXCEPCION QUE NO EXISTE EL STAFF_SPECIALTY CON ESE ID

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, STAFF_SPECIALTIES_TABLE));
    }

    @Test
    public void testStaffSpecialtyUpdateStaffSpecialtyWithNullName()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpecialty();
        StaffSpecialty ss = staffSpecialtyModel();
        ss.setName(null);
        expectedException.expect(IllegalStateException.class);

        // 2. Ejercitar
        this.staffSpecialtyDao.update(ss);

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, STAFF_SPECIALTIES_TABLE));
        assertEquals(staffSpecialtyModel(), ss);
    }

    @Test
    public void testStaffSpecialtyUpdateStaffSpecialtyWithNullId()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpecialty();
        StaffSpecialty ss = staffSpecialtyModel();
        ss.setId(null);
        expectedException.expect(Exception.class); // <-- TODO: Insert exception class here

        // 2. Ejercitar
        this.staffSpecialtyDao.update(ss); // TODO: NO HACE NADA, DEBERIA TIRAR EXCEPCION QUE DEBE TENER ID NOT NULL

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, STAFF_SPECIALTIES_TABLE));
    }

    /* --------------------- MÉTODO: staffSpecialtyDao.remove(String id) -------------------------------------------- */

    @Test
    public void testStaffSpecialtyRemoveById()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpecialty();

        // 2. Ejercitar
        this.staffSpecialtyDao.remove(STARTING_ID);

        // 3. Postcondiciones
        assertEquals(0,JdbcTestUtils.countRowsInTable(jdbcTemplate, STAFF_SPECIALTIES_TABLE));
    }

    @Test
    public void testStaffSpecialtyRemoveByIdNotExistent()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpecialty();

        // 2. Ejercitar
        this.staffSpecialtyDao.remove(STARTING_ID + 1);

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, STAFF_SPECIALTIES_TABLE));
    }

    @Test
    public void testStaffSpecialtyRemoveByNullId()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpecialty();

        // 2. Ejercitar
        this.staffSpecialtyDao.remove((Integer) null);

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, STAFF_SPECIALTIES_TABLE));
    }
    /* --------------------- MÉTODO: staffSpecialtyDao.remove(StaffSpecialty) -------------------------------------------- */

    @Test
    public void testStaffSpecialtyRemoveByModel()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpecialty();
        StaffSpecialty ss = staffSpecialtyModel();

        // 2. Ejercitar
        this.staffSpecialtyDao.remove(ss);

        // 3. Postcondiciones
        assertEquals(0,JdbcTestUtils.countRowsInTable(jdbcTemplate, STAFF_SPECIALTIES_TABLE));
    }

    @Test
    public void testStaffSpecialtyRemoveByModelNotExistent()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertAnotherStaffSpecialty();
        StaffSpecialty ss = staffSpecialtyModel();
        ss.setId(STARTING_ID + 1);

        // 2. Ejercitar
        this.staffSpecialtyDao.remove(ss);

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, STAFF_SPECIALTIES_TABLE));
    }

    @Test
    public void testStaffSpecialtyRemoveByNullModel()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpecialty();
        expectedException.expect(NullPointerException.class);

        // 2. Ejercitar
        this.staffSpecialtyDao.remove((StaffSpecialty) null);

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, STAFF_SPECIALTIES_TABLE));
    }

    /* --------------------- MÉTODO: staffSpecialtyDao.count() -------------------------------------------- */

    @Test
    public void testStaffSpecialtyCount()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpecialty();
        insertAnotherStaffSpecialty();

        // 2. Ejercitar
        ModelMetadata modelMetadata = this.staffSpecialtyDao.count();

        // 3. Postcondiciones
        assertEquals(2, (int) modelMetadata.getCount()); // TODO: fix
        System.out.println(modelMetadata.getMax()); // No se que devuelve esto
        System.out.println(modelMetadata.getMin()); // No se que devuelve esto
    }

    @Test
    public void testStaffSpecialtyCountEmptyTable()
    {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        ModelMetadata modelMetadata = this.staffSpecialtyDao.count();

        // 3. Postcondiciones
        assertEquals(0, (int) modelMetadata.getCount()); // TODO: fix
        System.out.println(modelMetadata.getMax()); // No se que devuelve esto
        System.out.println(modelMetadata.getMin()); // No se que devuelve esto
    }

    /* --------------------- MÉTODO: staffSpecialtyDao.findByField() -------------------------------------------- */

    @Test
    public void testStaffSpecialtyFindByFieldName()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpecialty();
        insertAnotherStaffSpecialty();

        // 2. Ejercitar
        List<StaffSpecialty> staffSpecialties = this.staffSpecialtyDao.findByField("name", STAFF_SPECIALTY);

        // 3. Postcondiciones
        assertNotNull(staffSpecialties);
        assertEquals(1, staffSpecialties.size());
        for (StaffSpecialty ss : staffSpecialties){
            assertEquals(STAFF_SPECIALTY, ss.getName());
        }
    }

    @Test
    public void testStaffSpecialtyFindByFieldId()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpecialty();
        insertAnotherStaffSpecialty();

        // 2. Ejercitar
        List<StaffSpecialty> staffSpecialties = this.staffSpecialtyDao.findByField("specialty_id", STARTING_ID);

        // 3. Postcondiciones
        assertNotNull(staffSpecialties);
        assertEquals(1, staffSpecialties.size());
        for (StaffSpecialty ss : staffSpecialties){
            assertEquals(STARTING_ID, (int) ss.getId());
        }
    }

    @Test
    public void testStaffSpecialtyFindByFieldNull()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpecialty();
        insertAnotherStaffSpecialty();

        // 2. Ejercitar
        List<StaffSpecialty> staffSpecialties = this.staffSpecialtyDao.findByField("specialty_id", null); //TODO: Deberia tirar NullPointer (?)

        // 3. Postcondiciones
        assertNotNull(staffSpecialties);
        assertTrue(staffSpecialties.isEmpty());
    }

    @Test
    public void testStaffSpecialtyFindByFieldNotExistent()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpecialty();
        insertAnotherStaffSpecialty();
        expectedException.expect(BadSqlGrammarException.class);

        // 2. Ejercitar
        List<StaffSpecialty> staffSpecialties = this.staffSpecialtyDao.findByField("specialty_id_no_existo", STARTING_ID); //TODO: Deberia tirar otro tipo de error (?)

        // 3. Postcondiciones
        assertNotNull(staffSpecialties);
        assertTrue(staffSpecialties.isEmpty());
    }

    @Test
    public void testStaffSpecialtyFindByFieldContentNotExistent()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertStaffSpecialty();
        insertAnotherStaffSpecialty();

        // 2. Ejercitar
        List<StaffSpecialty> staffSpecialties = this.staffSpecialtyDao.findByField("specialty_id", -1); //TODO: Deberia tirar NullPointer (?)

        // 3. Postcondiciones
        assertNotNull(staffSpecialties);
        assertTrue(staffSpecialties.isEmpty());
    }
}