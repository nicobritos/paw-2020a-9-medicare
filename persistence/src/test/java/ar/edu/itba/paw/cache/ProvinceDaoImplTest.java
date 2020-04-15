package ar.edu.itba.paw.cache;

import ar.edu.itba.paw.models.Province;
import ar.edu.itba.paw.persistence.ProvinceDaoImpl;
import ar.edu.itba.paw.persistence.utils.CacheHelper;
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
public class ProvinceDaoImplTest
{
    private static final String PROVINCE = "Buenos Aires";
    private static final String COUNTRY = "Argentina";
    private static final String COUNTRY_ID = "AR";

    private static final String PROVINCES_TABLE = "system_province";
    private static final String COUNTRIES_TABLE = "system_country";



    private ProvinceDaoImpl provinceDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert provinceJdbcInsert;
    private SimpleJdbcInsert countryJdbcInsert;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp(){
        this.provinceDao = new ProvinceDaoImpl(this.ds);
        this.jdbcTemplate = new JdbcTemplate(this.ds);
        this.provinceJdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(PROVINCES_TABLE)
                .usingGeneratedKeyColumns("province_id");
        this.countryJdbcInsert = new SimpleJdbcInsert(this.ds)
                .withTableName(COUNTRIES_TABLE);
    }

    private void cleanAllTables(){
        this.jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
        CacheHelper.clean();
    }

    private Province provinceModel(){
        Province p = new Province();
        p.setName(PROVINCE);
        p.setId(0);
        return p;
    }

    private void insertCountry(){
        Map<String, Object> map = new HashMap<>();
        map.put("country_id", COUNTRY_ID);
        map.put("name", COUNTRY);
        countryJdbcInsert.execute(map);
    }

    private void insertProvince(){
        insertCountry();
        Map<String, Object> map = new HashMap<>();
        map.put("country_id", COUNTRY_ID);
        map.put("name", PROVINCE);
        provinceJdbcInsert.execute(map);
    }

    private void insertAnotherProvince() {
        Map<String, Object> map = new HashMap<>();
        map.put("country_id", COUNTRY_ID);
        map.put("name", "Corrientes");
        provinceJdbcInsert.execute(map);
    }
    
    @Test
    public void testCreateProvince()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertCountry();
        Province p = provinceModel();

        // 2. Ejercitar
        Province province = this.provinceDao.create(p);

        // 3. Postcondiciones
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, PROVINCES_TABLE));
        assertEquals(PROVINCE, province.getName());
        assertEquals(0, (int) province.getId());
    }

    @Test
    public void testFindProvinceById()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertProvince();

        // 2. Ejercitar
        Optional<Province> province = this.provinceDao.findById(0);

        // 3. Postcondiciones
        assertTrue(province.isPresent());
        assertEquals(PROVINCE, province.get().getName());
    }

    @Test
    public void testFindProvinceByIdDoesntExist()
    {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Optional<Province> province = this.provinceDao.findById(0);

        // 3. Postcondiciones
        assertFalse(province.isPresent());
    }

    @Test
    public void testFindProvinceByField()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertProvince();

        // 2. Ejercitar
        List<Province> provinces = this.provinceDao.findByField("name", PROVINCE);

        // 3. Postcondiciones
        assertNotNull(provinces);
        assertFalse(provinces.isEmpty());
        assertEquals(PROVINCE, provinces.get(0).getName());
    }

    @Test
    public void testFindProvinceByFieldDoesntExist()
    {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        List<Province> provinces = this.provinceDao.findByField("name", PROVINCE);

        // 3. Postcondiciones
        assertNotNull(provinces);
        assertTrue(provinces.isEmpty());
    }

    @Test
    public void testFindProvinceByFieldOp()
    {
        // 1. Precondiciones
        cleanAllTables();

        insertProvince();

        // 2. Ejercitar
        List<Province> provinces = this.provinceDao.findByField("name", JDBCWhereClauseBuilder.Operation.EQ, PROVINCE);

        // 3. Postcondiciones
        assertNotNull(provinces);
        assertFalse(provinces.isEmpty());
        assertEquals(PROVINCE, provinces.get(0).getName());
    }

    @Test
    public void testFindProvinceByFieldOpDoesntExist()
    {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        List<Province> provinces = this.provinceDao.findByField("name", JDBCWhereClauseBuilder.Operation.EQ, PROVINCE);

        // 3. Postcondiciones
        assertNotNull(provinces);
        assertTrue(provinces.isEmpty());
    }

    @Test
    public void testFindProvincesByIds()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertProvince();
        insertAnotherProvince();

        // 2. Ejercitar
        Collection<Province> provinces = this.provinceDao.findByIds(Arrays.asList(0, 1));

        // 3. Postcondiciones
        assertNotNull(provinces);
        assertEquals(2, provinces.size());
        for (Province p : provinces){
            assertTrue(p.getName().equals(PROVINCE) || p.getName().equals("Corrientes"));
        }
    }

    @Test
    public void testFindProvincesByIdsDoesntExist()
    {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<Province> provinces = this.provinceDao.findByIds(Arrays.asList(0, 1));

        // 3. Postcondiciones
        assertNotNull(provinces);
        assertTrue(provinces.isEmpty());

    }

    @Test
    public void testFindProvincesByName()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertProvince();

        // 2. Ejercitar
        Collection<Province> provinces = this.provinceDao.findByName(PROVINCE);

        // 3. Postcondiciones
        assertNotNull(provinces);
        assertEquals(1, provinces.size());
        for (Province p : provinces){
            assertEquals(PROVINCE, p.getName());
        }
    }

    @Test
    public void testFindProvincesByNameDoesntExist()
    {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<Province> provinces = this.provinceDao.findByName(PROVINCE);

        // 3. Postcondiciones
        assertNotNull(provinces);
        assertTrue(provinces.isEmpty());
    }

    @Test
    public void testProvinceList()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertProvince();
        insertAnotherProvince();

        // 2. Ejercitar
        Collection<Province> provinces = this.provinceDao.list();

        // 3. Postcondiciones
        assertNotNull(provinces);
        assertEquals(2, provinces.size());
    }

    @Test
    public void testProvincesEmptyList()
    {
        // 1. Precondiciones
        cleanAllTables();

        // 2. Ejercitar
        Collection<Province> provinces = this.provinceDao.list();

        // 3. Postcondiciones
        assertNotNull(provinces);
        assertTrue(provinces.isEmpty());
    }

    @Test
    public void testProvinceUpdate()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertProvince();
        Province p = new Province();
        p.setId(0);
        p.setName("Corrientes");

        // 2. Ejercitar
        this.provinceDao.update(p);

        // 3. Postcondiciones
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, PROVINCES_TABLE));
        assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, PROVINCES_TABLE, "name = 'Corrientes'"));
        assertEquals(0,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, PROVINCES_TABLE, "name = '" + PROVINCE + "'"));

    }

    @Test
    public void testProvinceRemoveById()
    {
        // 1. Precondiciones
        cleanAllTables();
       insertProvince();

        // 2. Ejercitar
        this.provinceDao.remove(0);

        // 3. Postcondiciones
        assertEquals(0,JdbcTestUtils.countRowsInTable(jdbcTemplate, PROVINCES_TABLE));
    }

    @Test
    public void testCountryRemoveByModel()
    {
        // 1. Precondiciones
        cleanAllTables();
        insertProvince();
        Province p = provinceModel();

        // 2. Ejercitar
        this.provinceDao.remove(p);

        // 3. Postcondiciones
        assertEquals(0,JdbcTestUtils.countRowsInTable(jdbcTemplate, PROVINCES_TABLE));
    }
}
