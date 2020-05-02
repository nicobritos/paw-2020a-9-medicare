package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.ProvinceDao;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.models.Province;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import ar.edu.itba.paw.persistence.utils.RowMapperAlias;
import ar.edu.itba.paw.persistence.utils.StringSearchType;
import ar.edu.itba.paw.persistence.utils.builder.JDBCQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCSelectQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCSelectQueryBuilder.JoinType;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.ColumnTransformer;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
public class ProvinceDaoImpl extends GenericSearchableDaoImpl<Province, Integer> implements ProvinceDao {
    public static final RowMapperAlias<Province> ROW_MAPPER = (prefix, resultSet) -> {
        Province province = new Province();
        try {
            province.setId(resultSet.getInt(formatColumnFromName(ProvinceDaoImpl.PRIMARY_KEY_NAME, prefix)));
        } catch (SQLException e) {
            province.setId(resultSet.getInt(ProvinceDaoImpl.PRIMARY_KEY_NAME));
        }
        populateEntity(province, resultSet, prefix);
        return province;
    };
    public static final String TABLE_NAME = getTableNameFromModel(Province.class);
    public static final String PRIMARY_KEY_NAME = getPrimaryKeyNameFromModel(Province.class);

    @Autowired
    public ProvinceDaoImpl(DataSource dataSource) {
        super(dataSource, Province.class, Integer.class);
    }

    @Override
    public List<Province> findByCountry(Country country) {
        return this.findByField("country_id", Operation.EQ, country);
    }

    @Override
    public List<Province> findByCountryAndName(Country country, String name) {
        name = name.toLowerCase();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("country", country.getId());
        parameterSource.addValue("name", StringSearchType.CONTAINS_NO_ACC.transform(name));

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromName("name"), Operation.LIKE, ":name", ColumnTransformer.LOWER)
                .and()
                .where(this.formatColumnFromName("country_id"), Operation.EQ, ":country");

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(Province.class)
                .from(this.getTableName())
                .where(whereClauseBuilder);

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    protected ResultSetExtractor<List<Province>> getResultSetExtractor() {
        return resultSet -> {
            Map<Integer, Province> entitiesMap = new HashMap<>();
            Map<String, Country> countryMap = new HashMap<>();

            List<Province> sortedEntities = new LinkedList<>();
            while (resultSet.next()) {
                int id;
                String idString;
                Country country = null;

                try {
                    id = resultSet.getInt(this.formatColumnFromAlias(this.getIdColumnName()));
                } catch (SQLException e) {
                    id = resultSet.getInt(this.getIdColumnName());
                }
                if (resultSet.wasNull())
                    continue;
                Province province = entitiesMap.computeIfAbsent(id, integer -> {
                    try {
                        Province newProvince = ROW_MAPPER.mapRow(this.getTableAlias(), resultSet);
                        sortedEntities.add(newProvince);
                        return newProvince;
                    } catch (SQLException throwables) {
                        return null;
                    }
                });
                if (province == null) {
                    continue;
                }

                idString = resultSet.getString(formatColumnFromName(CountryDaoImpl.PRIMARY_KEY_NAME, "c"));
                if (!resultSet.wasNull()) {
                    country = countryMap.computeIfAbsent(idString, integer -> {
                        try {
                            return CountryDaoImpl.ROW_MAPPER.mapRow("c", resultSet);
                        } catch (SQLException throwables) {
                            return null;
                        }
                    });
                }

                if (country != null)
                    province.setCountry(country);
            }
            return sortedEntities;
        };
    }

    @Override
    protected void populateJoins(JDBCSelectQueryBuilder selectQueryBuilder) {
        selectQueryBuilder
                .joinAlias("country_id", CountryDaoImpl.TABLE_NAME, "c", CountryDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Country.class);
    }
}
