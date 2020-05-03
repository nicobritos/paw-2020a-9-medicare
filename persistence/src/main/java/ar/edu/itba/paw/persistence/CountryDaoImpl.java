package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.CountryDao;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import ar.edu.itba.paw.persistence.utils.JDBCArgumentValue;
import ar.edu.itba.paw.persistence.utils.RowMapperAlias;
import ar.edu.itba.paw.persistence.utils.builder.JDBCSelectQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
public class CountryDaoImpl extends GenericSearchableDaoImpl<Country, String> implements CountryDao {
    public static final RowMapperAlias<Country> ROW_MAPPER = (prefix, resultSet) -> {
        Country country = new Country();
        try {
            country.setId(resultSet.getString(formatColumnFromName(CountryDaoImpl.PRIMARY_KEY_NAME, prefix)));
        } catch (SQLException e) {
            country.setId(resultSet.getString(CountryDaoImpl.PRIMARY_KEY_NAME));
        }
        populateEntity(country, resultSet, prefix);
        return country;
    };
    public static final String TABLE_NAME = getTableNameFromModel(Country.class);
    public static final String PRIMARY_KEY_NAME = getPrimaryKeyNameFromModel(Country.class);

    @Autowired
    public CountryDaoImpl(DataSource dataSource) {
        super(dataSource, Country.class, String.class);
    }

    @Override
    protected ResultSetExtractor<List<Country>> getResultSetExtractor() {
        return resultSet -> {
            Map<String, Country> entityMap = new HashMap<>();
            List<Country> sortedEntities = new LinkedList<>();
            while (resultSet.next()) {
                String id;
                try {
                    id = resultSet.getString(this.formatColumnFromAlias(this.getIdColumnName()));
                } catch (SQLException e) {
                    id = resultSet.getString(this.getIdColumnName());
                }
                if (resultSet.wasNull())
                    continue;
                entityMap.computeIfAbsent(id, string -> {
                    try {
                        Country newEntity = ROW_MAPPER.mapRow(this.getTableAlias(), resultSet);
                        sortedEntities.add(newEntity);
                        return newEntity;
                    } catch (SQLException e) {
                        return null;
                    }
                });
            }
            return sortedEntities;
        };
    }

    @Override
    protected void populateJoins(JDBCSelectQueryBuilder selectQueryBuilder) {

    }

    @Override
    protected Map<String, JDBCArgumentValue> getModelRelationsArgumentValue(Country model, String prefix) {
        return null;
    }
}
