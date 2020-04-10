package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.CountryDao;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import ar.edu.itba.paw.persistence.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

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
        super(dataSource);
    }

    @Override
    protected RowMapper<Country> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getTableName() {
        return "system_country";
    }

    @Override
    protected String getIdColumnName() {
        return "country_id";
    }

    @Override
    protected Map<String, Pair<String, Object>> getModelColumnsArgumentValue(Country model) {
        Map<String, Pair<String, Object>> map = new HashMap<>();

        map.put("country_id", new Pair<>(":country_id", model.getId()));
        map.put("name", new Pair<>(":name", model.getId()));

        return map;
    }
}
