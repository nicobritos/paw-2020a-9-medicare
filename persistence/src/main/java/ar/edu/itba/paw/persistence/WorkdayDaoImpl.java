package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.WorkdayDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.generics.GenericDaoImpl;
import ar.edu.itba.paw.persistence.utils.JDBCArgumentValue;
import ar.edu.itba.paw.persistence.utils.RowMapperAlias;
import ar.edu.itba.paw.persistence.utils.builder.JDBCSelectQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCSelectQueryBuilder.JoinType;
import ar.edu.itba.paw.persistence.utils.builder.JDBCUpdateQueryBuilder;
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
public class WorkdayDaoImpl extends GenericDaoImpl<Workday, Integer> implements WorkdayDao {
    public static final RowMapperAlias<Workday> ROW_MAPPER = (prefix, resultSet) -> {
        Workday workday = new Workday();
        try {
            workday.setId(resultSet.getInt(formatColumnFromName(WorkdayDaoImpl.PRIMARY_KEY_NAME, prefix)));
        } catch (SQLException e) {
            workday.setId(resultSet.getInt(WorkdayDaoImpl.PRIMARY_KEY_NAME));
        }
        populateEntity(workday, resultSet, prefix);
        return workday;
    };
    public static final String TABLE_NAME = getTableNameFromModel(Workday.class);
    public static final String PRIMARY_KEY_NAME = getPrimaryKeyNameFromModel(Workday.class);

    @Autowired
    public WorkdayDaoImpl(DataSource dataSource) {
        super(dataSource, Workday.class);
    }

    @Override
    public List<Workday> findByUser(User user) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("user", user.getId());

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(formatColumnFromName(UserDaoImpl.PRIMARY_KEY_NAME, UserDaoImpl.TABLE_NAME), Operation.EQ, ":user");

        JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(Workday.class)
                .from(this.getTableName())
                .join("staff_id", StaffDaoImpl.TABLE_NAME, StaffDaoImpl.PRIMARY_KEY_NAME, null)
                .join(StaffDaoImpl.TABLE_NAME, "user_id", UserDaoImpl.TABLE_NAME, UserDaoImpl.PRIMARY_KEY_NAME, null)
                .where(whereClauseBuilder);

        return this.selectQuery(queryBuilder, parameterSource);
    }

    @Override
    public List<Workday> findByStaff(Staff staff) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("staff", staff.getId());

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromName("staff_id"), Operation.EQ, ":staff");

        JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(Workday.class)
                .from(this.getTableName())
                .where(whereClauseBuilder);

        return this.selectQuery(queryBuilder, parameterSource);
    }

    @Override
    public List<Workday> findByStaff(Staff staff, WorkdayDay day) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("staff", staff.getId());
        parameterSource.addValue("day", day.name());

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where("day", Operation.EQ, ":day")
                .and()
                .where(this.formatColumnFromName("staff_id"), Operation.EQ, ":staff");

        JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(Workday.class)
                .from(this.getTableName())
                .where(whereClauseBuilder);

        return this.selectQuery(queryBuilder, parameterSource);
    }

    @Override
    public boolean isStaffWorking(Staff staff, AppointmentTimeSlot timeSlot) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("staff", staff.getId());
        parameterSource.addValue("day", WorkdayDay.from(timeSlot.getDate()).name());
        parameterSource.addValue("start_hour", timeSlot.getDate().getHourOfDay());
        parameterSource.addValue("end_hour", timeSlot.getToDate().getHourOfDay());

        Operation toHourOperation;
        if (timeSlot.getToDate().getMinuteOfHour() == 0) {
            toHourOperation = Operation.GEQ;
        } else {
            toHourOperation = Operation.GT;
        }

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where("day", Operation.EQ, ":day")
                .and()
                .where("start_hour", Operation.LEQ, ":start_hour")
                .and()
                .where("end_hour", toHourOperation, ":end_hour")
                .and()
                .where(this.formatColumnFromName("staff_id"), Operation.EQ, ":staff");

        JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(Workday.class)
                .from(this.getTableName())
                .where(whereClauseBuilder);

        return !this.selectQuery(queryBuilder, parameterSource).isEmpty();
    }

    @Override
    public void setStaff(Workday workday, Staff staff) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("staff", staff.getId());
        parameterSource.addValue("workday", workday.getId());

        JDBCUpdateQueryBuilder updateQueryBuilder = new JDBCUpdateQueryBuilder()
                .update(this.getTableName(), this.getTableAlias())
                .value("staff_id", ":staff")
                .where(new JDBCWhereClauseBuilder()
                        .where(PRIMARY_KEY_NAME, Operation.EQ, ":workday")
                );
        this.updateQuery(updateQueryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    protected ResultSetExtractor<List<Workday>> getResultSetExtractor() {
        return resultSet -> {
            Map<Integer, Workday> entitiesMap = new HashMap<>();

            Map<Integer, Office> officeMap = new HashMap<>();
            Map<Integer, Locality> localityMap = new HashMap<>();
            Map<Integer, Province> provinceMap = new HashMap<>();
            Map<String, Country> countryMap = new HashMap<>();
            Map<Integer, User> userMap = new HashMap<>();

            Map<Integer, Staff> staffMap = new HashMap<>();
            Map<Integer, StaffSpecialty> staffSpecialtyMap = new HashMap<>();

            List<Workday> sortedEntities = new LinkedList<>();
            while (resultSet.next()) {
                int id;
                String idString;
                Staff staff = null;
                User user = null;
                Office office = null;
                StaffSpecialty staffSpecialty = null;
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
                Workday workday = entitiesMap.computeIfAbsent(id, integer -> {
                    try {
                        Workday newWorkday = ROW_MAPPER.mapRow(this.getTableAlias(), resultSet);
                        sortedEntities.add(newWorkday);
                        return newWorkday;
                    } catch (SQLException e) {
                        return null;
                    }
                });
                if (workday == null) {
                    continue;
                }

                id = resultSet.getInt(formatColumnFromName(StaffDaoImpl.PRIMARY_KEY_NAME, "s"));
                if (!resultSet.wasNull()) {
                    staff = staffMap.computeIfAbsent(id, integer -> {
                        try {
                            return StaffDaoImpl.ROW_MAPPER.mapRow("s", resultSet);
                        } catch (SQLException throwables) {
                            return null;
                        }
                    });
                }
                id = resultSet.getInt(formatColumnFromName(UserDaoImpl.PRIMARY_KEY_NAME, "us"));
                if (!resultSet.wasNull()) {
                    user = userMap.computeIfAbsent(id, integer -> {
                        try {
                            return UserDaoImpl.ROW_MAPPER.mapRow("us", resultSet);
                        } catch (SQLException e) {
                            return null;
                        }
                    });
                }
                id = resultSet.getInt(formatColumnFromName(OfficeDaoImpl.PRIMARY_KEY_NAME, "os"));
                if (!resultSet.wasNull()) {
                    office = officeMap.computeIfAbsent(id, integer -> {
                        try {
                            return OfficeDaoImpl.ROW_MAPPER.mapRow("os", resultSet);
                        } catch (SQLException throwables) {
                            return null;
                        }
                    });
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
                id = resultSet.getInt(formatColumnFromName(StaffSpecialtyDaoImpl.PRIMARY_KEY_NAME, "ss"));
                if (!resultSet.wasNull()) {
                    staffSpecialty = staffSpecialtyMap.computeIfAbsent(id, integer -> {
                        try {
                            return StaffSpecialtyDaoImpl.ROW_MAPPER.mapRow("ss", resultSet);
                        } catch (SQLException throwables) {
                            return null;
                        }
                    });
                }

                if (staff != null) {
                    workday.setStaff(staff);
                    if (office != null) {
                        staff.setOffice(office);
                        office.setLocality(locality);
                    }
                    if (user != null)
                        staff.setUser(user);
                    if (staffSpecialty != null)
                        staff.getStaffSpecialties().add(staffSpecialty);
                }

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
            .joinAlias("staff_id", StaffDaoImpl.TABLE_NAME, "s", StaffDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Staff.class)
            .joinAlias("s", "user_id", UserDaoImpl.TABLE_NAME, "us", UserDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, User.class)
            .joinAlias("s", "office_id", OfficeDaoImpl.TABLE_NAME, "os", OfficeDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Office.class)
            .joinAlias("os", "locality_id", LocalityDaoImpl.TABLE_NAME, "l", LocalityDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Locality.class)
            .joinAlias("l", "province_id", ProvinceDaoImpl.TABLE_NAME, "ps", ProvinceDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Province.class)
            .joinAlias("ps", "country_id", CountryDaoImpl.TABLE_NAME, "cs", CountryDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Country.class)
            .joinAlias("staff_id", "system_staff_specialty_staff", "sss", "staff_id", JoinType.LEFT, null)
            .joinAlias("sss", "specialty_id", StaffSpecialtyDaoImpl.TABLE_NAME, "ss", StaffSpecialtyDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, StaffSpecialty.class);
    }

    @Override
    protected Map<String, JDBCArgumentValue> getModelRelationsArgumentValue(Workday model, String prefix) {
        Map<String, JDBCArgumentValue> map = new HashMap<>();
        map.put("staff_id", new JDBCArgumentValue(prefix + "staff_id", model.getStaff() != null ? model.getStaff().getId() : null));
        return map;
    }
}
