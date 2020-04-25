package ar.edu.itba.paw.cache;

import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.persistence.LocalityDaoImpl;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder;
import ar.edu.itba.paw.persistence.utils.cache.CacheHelper;
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
public class LocalityDaoImplTest
{
    private static final String LOCALITY = "Capital Federal";
    private static final String PROVINCE = "Buenos Aires";

    private static final String LOCALITIES_TABLE = "system_locality";
    private static final String PROVINCES_TABLE = "system_province";



    private LocalityDaoImpl localityDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert localityJdbcInsert;
    private SimpleJdbcInsert provinceJdbcInsert;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp(){
        this.localityDao = new LocalityDaoImpl(this.ds);
        this.jdbcTemplate = new JdbcTemplate(this.ds);
        this.localityJdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(LOCALITIES_TABLE)
                .usingGeneratedKeyColumns("locality_id");
        this.provinceJdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(PROVINCES_TABLE)
                .usingGeneratedKeyColumns("province_id");
    }

    private void cleanAllTables(){
        this.jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
        CacheHelper.clean();
    }

    private Locality localityModel(){
        Locality p = new Locality();
        p.setName(LOCALITY);
        p.setId(0);
        return p;
    }

    private void insertProvince(){
        Map<String, Object> map = new HashMap<>();
        map.put("province_id", 0);
        map.put("name", PROVINCE);
        provinceJdbcInsert.execute(map);
    }

    private void insertLocality(){
        insertProvince();
        Map<String, Object> map = new HashMap<>();
        map.put("province_id", 0);
        map.put("name", LOCALITY);
        localityJdbcInsert.execute(map);
    }

    private void insertAnotherLocality() {
        Map<String, Object> map = new HashMap<>();
        map.put("province_id", 0);
        map.put("name", "Palermo");
        localityJdbcInsert.execute(map);
    }

    @Test
    public void testCreateLocality()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertProvince();
        Locality p = localityModel();

        // 2. Ejercitar
        Locality locality = this.localityDao.create(p);

        // 3. Postcondiciones
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, LOCALITIES_TABLE));
        assertEquals(LOCALITY, locality.getName());
        assertEquals(0, (int) locality.getId());
    }

    @Test
    public void testFindLocalityById()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertLocality();

        // 2. Ejercitar
        Optional<Locality> locality = this.localityDao.findById(0);

        // 3. Postcondiciones
        assertTrue(locality.isPresent());
        assertEquals(LOCALITY, locality.get().getName());
    }

    @Test
    public void testFindLocalityByIdDoesntExist()
    {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Optional<Locality> locality = this.localityDao.findById(0);

        // 3. Postcondiciones
        assertFalse(locality.isPresent());
    }

    @Test
    public void testFindLocalityByField()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertLocality();

        // 2. Ejercitar
        Collection<Locality> localitys = this.localityDao.findByField("name", LOCALITY);

        // 3. Postcondiciones
        assertNotNull(localitys);
        assertFalse(localitys.isEmpty());
        assertEquals(LOCALITY, localitys.stream().findFirst().get().getName());
    }

    @Test
    public void testFindLocalityByFieldDoesntExist()
    {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<Locality> localitys = this.localityDao.findByField("name", LOCALITY);

        // 3. Postcondiciones
        assertNotNull(localitys);
        assertTrue(localitys.isEmpty());
    }

    @Test
    public void testFindLocalityByFieldOp()
    {
        // 1. Precondiciones
        cleanAllTables();

        insertLocality();

        // 2. Ejercitar
        Collection<Locality> localitys = this.localityDao.findByField("name", JDBCWhereClauseBuilder.Operation.EQ, LOCALITY);

        // 3. Postcondiciones
        assertNotNull(localitys);
        assertFalse(localitys.isEmpty());
        assertEquals(LOCALITY, localitys.stream().findFirst().get().getName());
    }

    @Test
    public void testFindLocalityByFieldOpDoesntExist()
    {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<Locality> localitys = this.localityDao.findByField("name", JDBCWhereClauseBuilder.Operation.EQ, LOCALITY);

        // 3. Postcondiciones
        assertNotNull(localitys);
        assertTrue(localitys.isEmpty());
    }

    @Test
    public void testFindLocalitysByIds()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertLocality();
        insertAnotherLocality();

        // 2. Ejercitar
        Collection<Locality> localitys = this.localityDao.findByIds(Arrays.asList(0, 1));

        // 3. Postcondiciones
        assertNotNull(localitys);
        assertEquals(2, localitys.size());
        for (Locality p : localitys){
            assertTrue(p.getName().equals(LOCALITY) || p.getName().equals("Palermo"));
        }
    }

    @Test
    public void testFindLocalitysByIdsDoesntExist()
    {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<Locality> localitys = this.localityDao.findByIds(Arrays.asList(0, 1));

        // 3. Postcondiciones
        assertNotNull(localitys);
        assertTrue(localitys.isEmpty());

    }

    @Test
    public void testFindLocalitysByName()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertLocality();

        // 2. Ejercitar
        Collection<Locality> localitys = this.localityDao.findByName(LOCALITY);

        // 3. Postcondiciones
        assertNotNull(localitys);
        assertEquals(1, localitys.size());
        for (Locality p : localitys){
            assertEquals(LOCALITY, p.getName());
        }
    }

    @Test
    public void testFindLocalitysByNameDoesntExist()
    {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<Locality> localitys = this.localityDao.findByName(LOCALITY);

        // 3. Postcondiciones
        assertNotNull(localitys);
        assertTrue(localitys.isEmpty());
    }

    @Test
    public void testLocalityList()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertLocality();
        insertAnotherLocality();

        // 2. Ejercitar
        Collection<Locality> localitys = this.localityDao.list();

        // 3. Postcondiciones
        assertNotNull(localitys);
        assertEquals(2, localitys.size());
    }

    @Test
    public void testLocalitysEmptyList()
    {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<Locality> localitys = this.localityDao.list();

        // 3. Postcondiciones
        assertNotNull(localitys);
        assertTrue(localitys.isEmpty());
    }

    @Test
    public void testLocalityUpdate()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertLocality();
        Locality p = this.localityDao.findById(0).get();
        p.setName("Palermo");

        // 2. Ejercitar
        this.localityDao.update(p);

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, LOCALITIES_TABLE));
        assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, LOCALITIES_TABLE, "name = 'Palermo'"));
        assertEquals(0,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, LOCALITIES_TABLE, "name = '" + LOCALITY + "'"));

    }

    @Test
    public void testLocalityRemoveById()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertLocality();

        // 2. Ejercitar
        this.localityDao.remove(0);

        // 3. Postcondiciones
        assertEquals(0,JdbcTestUtils.countRowsInTable(jdbcTemplate, LOCALITIES_TABLE));
    }

    @Test
    public void testProvinceRemoveByModel()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertLocality();
        Locality p = localityModel();

        // 2. Ejercitar
        this.localityDao.remove(p);

        // 3. Postcondiciones
        assertEquals(0,JdbcTestUtils.countRowsInTable(jdbcTemplate, LOCALITIES_TABLE));
    }
}
