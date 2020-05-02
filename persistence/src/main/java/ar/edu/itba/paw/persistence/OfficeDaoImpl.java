package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.OfficeDao;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Province;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import ar.edu.itba.paw.persistence.utils.RowMapperAlias;
import ar.edu.itba.paw.persistence.utils.builder.JDBCSelectQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCSelectQueryBuilder.JoinType;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder;
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
public class OfficeDaoImpl extends GenericSearchableDaoImpl<Office, Integer> implements OfficeDao {
    public static final RowMapperAlias<Office> ROW_MAPPER = (prefix, resultSet) -> {
        Office office = new Office();
        office.setId(resultSet.getInt(formatColumnFromName(OfficeDaoImpl.PRIMARY_KEY_NAME, prefix)));
        populateEntity(office, resultSet, prefix);
        return office;
    };
    public static final String TABLE_NAME = getTableNameFromModel(Office.class);
    public static final String PRIMARY_KEY_NAME = getPrimaryKeyNameFromModel(Office.class);

    @Autowired
    public OfficeDaoImpl(DataSource dataSource) {
        super(dataSource, Office.class, Integer.class);
    }

    @Override
    public List<Office> findByCountry(Country country) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("country_id", country.getId());

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where("country_id", Operation.EQ, ":country_id");

        JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(Office.class)
                .from(this.getTableAlias())
                .join("locality_id", LocalityDaoImpl.TABLE_NAME, LocalityDaoImpl.PRIMARY_KEY_NAME, Locality.class)
                .join(LocalityDaoImpl.TABLE_NAME, "province_id", ProvinceDaoImpl.TABLE_NAME, ProvinceDaoImpl.PRIMARY_KEY_NAME, Province.class)
                .where(whereClauseBuilder)
                .distinct();

        return this.selectQuery(queryBuilder, parameterSource);
    }

    @Override
    public List<Office> findByProvince(Province province) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("province_id", province.getId());

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where("province_id", Operation.EQ, ":province_id");

        JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(Office.class)
                .from(this.getTableAlias())
                .join("locality_id", LocalityDaoImpl.TABLE_NAME, LocalityDaoImpl.PRIMARY_KEY_NAME, Locality.class)
                .where(whereClauseBuilder)
                .distinct();

        return this.selectQuery(queryBuilder, parameterSource);
    }

    @Override
    public List<Office> findByLocality(Locality locality) {
        return this.findByField("locality_id", Operation.EQ, locality);
    }

    @Override
    protected ResultSetExtractor<List<Office>> getResultSetExtractor() {
        return resultSet -> {
            Map<Integer, Office> appointmentMap = new HashMap<>();

            Map<Integer, Locality> localityMap = new HashMap<>();
            Map<Integer, Province> provinceMap = new HashMap<>();
            Map<String, Country> countryMap = new HashMap<>();

            List<Office> sortedEntities = new LinkedList<>();
            while (resultSet.next()) {
                int id;
                String idString;
                Locality locality = null;
                Province province = null;
                Country country = null;

                try {
                    id = resultSet.getInt(this.formatColumnFromAlias(this.getIdColumnName()));
                } catch (SQLException e) {
                    id = resultSet.getInt(this.getIdColumnName());
                }
                if (resultSet.wasNull())
                    continue;
                Office office = appointmentMap.computeIfAbsent(id, integer -> {
                    try {
                        Office newOffice = ROW_MAPPER.mapRow(this.getTableAlias(), resultSet);
                        sortedEntities.add(newOffice);
                        return newOffice;
                    } catch (SQLException e) {
                        return null;
                    }
                });
                if (office == null) {
                    continue;
                }

                id = resultSet.getInt(formatColumnFromName(LocalityDaoImpl.PRIMARY_KEY_NAME, "l"));
                if (!resultSet.wasNull()) {
                    locality = localityMap.computeIfAbsent(id, integer -> {
                        try {
                            return LocalityDaoImpl.ROW_MAPPER.mapRow("l", resultSet);
                        } catch (SQLException throwables) {
                            return null;
                        }
                    });
                }
                id = resultSet.getInt(formatColumnFromName(ProvinceDaoImpl.PRIMARY_KEY_NAME, "ps"));
                if (!resultSet.wasNull()) {
                    province = provinceMap.computeIfAbsent(id, integer -> {
                        try {
                            return ProvinceDaoImpl.ROW_MAPPER.mapRow("ps", resultSet);
                        } catch (SQLException throwables) {
                            return null;
                        }
                    });
                }
                idString = resultSet.getString(formatColumnFromName(CountryDaoImpl.PRIMARY_KEY_NAME, "cs"));
                if (!resultSet.wasNull()) {
                    country = countryMap.computeIfAbsent(idString, integer -> {
                        try {
                            return CountryDaoImpl.ROW_MAPPER.mapRow("cs", resultSet);
                        } catch (SQLException throwables) {
                            return null;
                        }
                    });
                }

                if (locality != null) {
                    office.setLocality(locality);
                    if (province != null)
                        locality.setProvince(province);
                }
                if (province != null && country != null) {
                    province.setCountry(country);
                }
            }
            return sortedEntities;
        };
    }

    @Override
    protected void populateJoins(JDBCSelectQueryBuilder selectQueryBuilder) {
        selectQueryBuilder
                .joinAlias("locality_id", LocalityDaoImpl.TABLE_NAME, "l", LocalityDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Locality.class)
                .joinAlias("l", "province_id", ProvinceDaoImpl.TABLE_NAME, "ps", ProvinceDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Province.class)
                .joinAlias("ps", "country_id", CountryDaoImpl.TABLE_NAME, "cs", CountryDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Country.class);
    }
}
