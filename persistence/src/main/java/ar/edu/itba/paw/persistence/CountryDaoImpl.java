package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.CountryDao;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class CountryDaoImpl extends GenericSearchableDaoImpl<Country, String> implements CountryDao {
    private final RowMapper<Country> rowMapper = (resultSet, rowNum) -> this.hydrate(resultSet);
    public static final String TABLE_NAME = getTableNameFromModel(Country.class);
    public static final String PRIMARY_KEY_NAME = getPrimaryKeyNameFromModel(Country.class);

    @Autowired
    public CountryDaoImpl(DataSource dataSource) {
        super(dataSource, Country.class, String.class);
    }

    @Override
    protected RowMapper<Country> getRowMapper() {
        return this.rowMapper;
    }
}
