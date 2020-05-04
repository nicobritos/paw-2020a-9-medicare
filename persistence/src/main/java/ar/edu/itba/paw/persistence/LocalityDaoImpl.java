package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.LocalityDao;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Province;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import ar.edu.itba.paw.persistence.utils.JDBCArgumentValue;
import ar.edu.itba.paw.persistence.utils.RowMapperAlias;
import ar.edu.itba.paw.persistence.utils.StringSearchType;
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
public class LocalityDaoImpl extends GenericSearchableDaoImpl<Locality, Integer> implements LocalityDao {
    public static final RowMapperAlias<Locality> ROW_MAPPER = (prefix, resultSet) -> {
        Locality locality = new Locality();
        try {
            locality.setId(resultSet.getInt(formatColumnFromName(LocalityDaoImpl.PRIMARY_KEY_NAME, prefix)));
        } catch (SQLException e) {
            locality.setId(resultSet.getInt(LocalityDaoImpl.PRIMARY_KEY_NAME));
        }
        populateEntity(locality, resultSet, prefix);
        return locality;
    };
    public static final String TABLE_NAME = getTableNameFromModel(Locality.class);
    public static final String PRIMARY_KEY_NAME = getPrimaryKeyNameFromModel(Locality.class);

    @Autowired
    public LocalityDaoImpl(DataSource dataSource) {
        super(dataSource, Locality.class, Integer.class);
    }

    @Override
    public List<Locality> findByProvince(Province province) {
        return this.findByField("province_id", Operation.EQ, province);
    }

    @Override
    public List<Locality> findByProvinceAndName(Province province, String name) {
        name = name.toLowerCase();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("province", province.getId());
        parameterSource.addValue("name", StringSearchType.CONTAINS_NO_ACC.transform(name));

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromName("name"), Operation.LIKE, ":name", ColumnTransformer.LOWER)
                .and()
                .where(this.formatColumnFromName("province_id"), Operation.EQ, ":province");

        JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(Locality.class)
                .from(this.getTableName())
                .where(whereClauseBuilder);

        return this.selectQuery(queryBuilder, parameterSource);
    }

    @Override
    protected ResultSetExtractor<List<Locality>> getResultSetExtractor() {
        return resultSet -> {
            Map<Integer, Locality> entitiesMap = new HashMap<>();
            Map<Integer, Province> provinceMap = new HashMap<>();
            Map<String, Country> countryMap = new HashMap<>();

            List<Locality> sortedLocalities = new LinkedList<>();
            while (resultSet.next()) {
                int id;
                String idString;
                Province province = null;
                Country country = null;

                try {
                    id = resultSet.getInt(this.formatColumnFromAlias(this.getIdColumnName()));
                } catch (SQLException e) {
                    id = resultSet.getInt(this.getIdColumnName());
                }
                if (resultSet.wasNull())
                    continue;
                Locality locality = entitiesMap.computeIfAbsent(id, integer -> {
                    try {
                        Locality newLocality = ROW_MAPPER.mapRow(this.getTableAlias(), resultSet);
                        sortedLocalities.add(newLocality);
                        return newLocality;
                    } catch (SQLException throwables) {
                        return null;
                    }
                });
                if (locality == null) {
                    continue;
                }

                id = resultSet.getInt(formatColumnFromName(ProvinceDaoImpl.PRIMARY_KEY_NAME, "p"));
                if (!resultSet.wasNull()) {
                    province = provinceMap.computeIfAbsent(id, integer -> {
                        try {
                            return ProvinceDaoImpl.ROW_MAPPER.mapRow("p", resultSet);
                        } catch (SQLException throwables) {
                            return null;
                        }
                    });
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

                if (province != null) {
                    locality.setProvince(province);
                    if (country != null)
                        province.setCountry(country);
                }
            }
            return sortedLocalities;
        };
    }

    @Override
    protected void populateJoins(JDBCSelectQueryBuilder selectQueryBuilder) {
        selectQueryBuilder
                .joinAlias("province_id", ProvinceDaoImpl.TABLE_NAME, "p", ProvinceDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Province.class)
                .joinAlias("p", "country_id", CountryDaoImpl.TABLE_NAME, "c", CountryDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Country.class);
    }

    @Override
    protected Map<String, JDBCArgumentValue> getModelRelationsArgumentValue(Locality model, String prefix) {
        Map<String, JDBCArgumentValue> map = new HashMap<>();
        map.put("province_id", new JDBCArgumentValue(prefix + "province_id", model.getProvince() != null ? model.getProvince().getId() : null));
        return map;
    }
}
