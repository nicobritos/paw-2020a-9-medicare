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
    private static final RowMapper<Country> ROW_MAPPER = (resultSet, rowNum) -> {
        Country country = new Country();

        country.setId(resultSet.getString("country_id"));
        country.setName(resultSet.getString("name"));

        return country;
    };

    @Autowired
    public CountryDaoImpl(DataSource dataSource) {
        super(dataSource, Country.class);
    }

    @Override
    protected RowMapper<Country> getRowMapper() {
        return ROW_MAPPER;
    }
}
