package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.PatientDao;
import ar.edu.itba.paw.models.*;
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
import java.util.*;

@Repository
public class PatientDaoImpl extends GenericSearchableDaoImpl<Patient, Integer> implements PatientDao {
    public static final RowMapperAlias<Patient> ROW_MAPPER = (prefix, resultSet) -> {
        Patient patient = new Patient();
        try {
            patient.setId(resultSet.getInt(formatColumnFromName(PatientDaoImpl.PRIMARY_KEY_NAME, prefix)));
        } catch (SQLException e) {
            patient.setId(resultSet.getInt(PatientDaoImpl.PRIMARY_KEY_NAME));
        }
        populateEntity(patient, resultSet, prefix);
        return patient;
    };
    public static final String TABLE_NAME = getTableNameFromModel(Patient.class);
    public static final String PRIMARY_KEY_NAME = getPrimaryKeyNameFromModel(Patient.class);

    @Autowired
    public PatientDaoImpl(DataSource dataSource) {
        super(dataSource, Patient.class, Integer.class);
    }

    @Override
    public Optional<Patient> findByUserAndOffice(User user, Office office) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("user_id", user.getId());
        parameterSource.addValue("office_id", office.getId());

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromName("office_id"), Operation.EQ, ":office_id")
                .and()
                .where(this.formatColumnFromName("user_id"), Operation.EQ, ":user_id");

        JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(Patient.class)
                .from(this.getTableName())
                .where(whereClauseBuilder);

        return this.selectQuery(queryBuilder, parameterSource).stream().findFirst();
    }

    @Override
    public Optional<Patient> findByUser(User user) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("user_id", user.getId());

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromName("user_id"), Operation.EQ, ":user_id");

        JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(Patient.class)
                .from(this.getTableName())
                .where(whereClauseBuilder);

        return this.selectQuery(queryBuilder, parameterSource).stream().findFirst();
    }

    @Override
    protected ResultSetExtractor<List<Patient>> getResultSetExtractor() {
        return resultSet -> {
            Map<Integer, Patient> entitiesMap = new HashMap<>();

            Map<Integer, Office> officeMap = new HashMap<>();
            Map<Integer, Locality> localityMap = new HashMap<>();
            Map<Integer, Province> provinceMap = new HashMap<>();
            Map<String, Country> countryMap = new HashMap<>();
            Map<Integer, User> userMap = new HashMap<>();

            List<Patient> sortedEntities = new LinkedList<>();
            while (resultSet.next()) {
                int id;
                String idString;
                User user = null;
                Office office = null;
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
                Patient patient = entitiesMap.computeIfAbsent(id, integer -> {
                    try {
                        Patient newEntity = ROW_MAPPER.mapRow(this.getTableAlias(), resultSet);
                        sortedEntities.add(newEntity);
                        return newEntity;
                    } catch (SQLException e) {
                        return null;
                    }
                });
                if (patient == null) {
                    continue;
                }

                id = resultSet.getInt(formatColumnFromName(UserDaoImpl.PRIMARY_KEY_NAME, "up"));
                if (!resultSet.wasNull()) {
                    user = userMap.computeIfAbsent(id, integer -> {
                        try {
                            return UserDaoImpl.ROW_MAPPER.mapRow("up", resultSet);
                        } catch (SQLException e) {
                            return null;
                        }
                    });
                }
                id = resultSet.getInt(formatColumnFromName(OfficeDaoImpl.PRIMARY_KEY_NAME, "op"));
                if (!resultSet.wasNull()) {
                    office = officeMap.computeIfAbsent(id, integer -> {
                        try {
                            return OfficeDaoImpl.ROW_MAPPER.mapRow("op", resultSet);
                        } catch (SQLException e) {
                            return null;
                        }
                    });
                }
                id = resultSet.getInt(formatColumnFromName(LocalityDaoImpl.PRIMARY_KEY_NAME, "lp"));
                if (!resultSet.wasNull()) {
                    locality = localityMap.computeIfAbsent(id, integer -> {
                        try {
                            return LocalityDaoImpl.ROW_MAPPER.mapRow("lp", resultSet);
                        } catch (SQLException throwables) {
                            return null;
                        }
                    });
                }
                id = resultSet.getInt(formatColumnFromName(ProvinceDaoImpl.PRIMARY_KEY_NAME, "pps"));
                if (!resultSet.wasNull()) {
                    province = provinceMap.computeIfAbsent(id, integer -> {
                        try {
                            return ProvinceDaoImpl.ROW_MAPPER.mapRow("pps", resultSet);
                        } catch (SQLException throwables) {
                            return null;
                        }
                    });
                }
                idString = resultSet.getString(formatColumnFromName(CountryDaoImpl.PRIMARY_KEY_NAME, "cp"));
                if (!resultSet.wasNull()) {
                    country = countryMap.computeIfAbsent(idString, integer -> {
                        try {
                            return CountryDaoImpl.ROW_MAPPER.mapRow("cp", resultSet);
                        } catch (SQLException throwables) {
                            return null;
                        }
                    });
                }

                if (office != null) {
                    patient.setOffice(office);
                    office.setLocality(locality);
                }
                if (user != null)
                    patient.setUser(user);
                if (locality != null && province != null) {
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
                .joinAlias("user_id", UserDaoImpl.TABLE_NAME, "up", UserDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, User.class)
                .joinAlias("office_id", OfficeDaoImpl.TABLE_NAME, "op", OfficeDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Office.class)
                .joinAlias("op", "locality_id", LocalityDaoImpl.TABLE_NAME, "lp", LocalityDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Locality.class)
                .joinAlias("lp", "province_id", ProvinceDaoImpl.TABLE_NAME, "pps", ProvinceDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Province.class)
                .joinAlias("pps", "country_id", CountryDaoImpl.TABLE_NAME, "cp", CountryDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Country.class);
    }
}
