package ar.edu.itba.paw.nocache;

import org.hsqldb.jdbc.JDBCDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
@ComponentScan({ "ar.edu.itba.paw.persistence"})
public class TestConfig {
    private static final String HSQLDB_URL = "jdbc:hsqldb:mem:test;sql.syntax_pgs=true;check_props=true";
    private static final String HSQLDB_USER = "ha";
    private static final String HSQLDB_PASS = "";

    @Value("classpath:sql/schema.sql")
    private Resource schemaSql;

    @Bean
    public DataSource dataSource(){
        final SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriverClass(JDBCDriver.class);
        ds.setUrl(HSQLDB_URL);
        ds.setUsername(HSQLDB_USER);
        ds.setPassword(HSQLDB_PASS);
        return ds;
    }

    @Bean
    public DatabasePopulator databasePopulator(){
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaSql);
        return populator;
    }

    @Bean
    public boolean isCacheEnabled(){
        return false;
    }

    private DataSourceInitializer dataSourceInitializer(){
        DataSourceInitializer dsi = new DataSourceInitializer();
        dsi.setDataSource(dataSource());
        dsi.setDatabasePopulator(databasePopulator());
        return dsi;
    }
}
