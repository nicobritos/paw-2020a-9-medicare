package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.AppointmentDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.generics.GenericDaoImpl;
import ar.edu.itba.paw.persistence.utils.RowMapperAlias;
import ar.edu.itba.paw.persistence.utils.builder.JDBCSelectQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCSelectQueryBuilder.JoinType;
import ar.edu.itba.paw.persistence.utils.builder.JDBCUpdateQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.Operation;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

@Repository
public class AppointmentDaoImpl extends GenericDaoImpl<Appointment, Integer> implements AppointmentDao {
    public static final RowMapperAlias<Appointment> ROW_MAPPER = (prefix, resultSet) -> {
        Appointment appointment = new Appointment();
        try {
            appointment.setId(resultSet.getInt(formatColumnFromName(AppointmentDaoImpl.PRIMARY_KEY_NAME, prefix)));
        } catch (SQLException e) {
            appointment.setId(resultSet.getInt(AppointmentDaoImpl.PRIMARY_KEY_NAME));
        }
        populateEntity(appointment, resultSet, prefix);
        return appointment;
    };
    public static final String TABLE_NAME = getTableNameFromModel(Appointment.class);
    public static final String PRIMARY_KEY_NAME = getPrimaryKeyNameFromModel(Appointment.class);

    @Autowired
    public AppointmentDaoImpl(DataSource dataSource) {
        super(dataSource, Appointment.class);
    }

    @Override
    public Optional<Appointment> findById(Integer id) {
        JDBCSelectQueryBuilder selectQueryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(Appointment.class)
                .from("appointment")
                .join("staff_id", Operation.EQ, StaffDaoImpl.TABLE_NAME, "staff_id", Staff.class)
                .join("patient_id", Operation.EQ, PatientDaoImpl.TABLE_NAME, "patient_id", Patient.class)
                .where(new JDBCWhereClauseBuilder()
                        .where("appointment_id", Operation.EQ, ":appointment_id")
                );
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("appointment_id", id);
        return this.selectQuerySingle(selectQueryBuilder, parameterSource);
    }

    @Override
    public List<Appointment> find(Patient patient) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("patient", patient.getId());

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromName("patient_id"), Operation.EQ, ":patient");

        JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(Appointment.class)
                .from(this.getTableName())
                .where(whereClauseBuilder);

        return this.selectQuery(queryBuilder, parameterSource);
    }

    @Override
    public List<Appointment> find(Staff staff) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("staff", staff.getId());

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromName("staff_id"), Operation.EQ, ":staff");

        JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(Appointment.class)
                .from(this.getTableName())
                .where(whereClauseBuilder);

        return this.selectQuery(queryBuilder, parameterSource);
    }

    @Override
    public List<Appointment> find(List<Staff> staffs){
        Map<String, Object> params = new HashMap<>();
        JDBCWhereClauseBuilder staffsWhereClause = new JDBCWhereClauseBuilder();
        putStaffsArguments(staffs, params, staffsWhereClause);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(params);

        JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(Appointment.class)
                .from(this.getTableName())
                .where(staffsWhereClause);

        return this.selectQuery(queryBuilder, parameterSource);
    }

    @Override
    public List<Appointment> findPending(Patient patient) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("patient", patient.getId());
        parameterSource.addValue("status", AppointmentStatus.CANCELLED);

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromName("patient_id"), Operation.EQ, ":patient")
                .and()
                .where(this.formatColumnFromName("status"), Operation.EQ, ":status");

        JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(Appointment.class)
                .from(this.getTableName())
                .where(whereClauseBuilder);

        return this.selectQuery(queryBuilder, parameterSource);
    }

    @Override
    public List<Appointment> findPending(Staff staff) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("staff", staff.getId());
        parameterSource.addValue("status", AppointmentStatus.CANCELLED);

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromName("staff_id"), Operation.EQ, ":staff")
                .and()
                .where(this.formatColumnFromName("status"), Operation.EQ, ":status");

        JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(Appointment.class)
                .from(this.getTableName())
                .where(whereClauseBuilder);

        return this.selectQuery(queryBuilder, parameterSource);
    }

    @Override
    public List<Appointment> findPending(Patient patient, Staff staff) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("patient", patient.getId());
        parameterSource.addValue("staff", staff.getId());
        parameterSource.addValue("status", AppointmentStatus.PENDING.name());

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromName("patient_id"), Operation.EQ, ":patient")
                .and()
                .where(this.formatColumnFromName("staff_id"), Operation.EQ, ":staff")
                .and()
                .where(this.formatColumnFromName("status"), Operation.EQ, ":status");

        JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(Appointment.class)
                .from(this.getTableName())
                .where(whereClauseBuilder);

        return this.selectQuery(queryBuilder, parameterSource);
    }

    @Override
    public List<Appointment> findByDate(Collection<Staff> staffs, DateTime date) {
        Map<String, Object> parameters = new HashMap<>();
        JDBCWhereClauseBuilder staffWhereClause = new JDBCWhereClauseBuilder();
        putStaffsArguments(staffs, parameters, staffWhereClause);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameters.put("year", date.getYear());
        parameters.put("month", date.getMonthOfYear());
        parameters.put("day", date.getDayOfMonth());
        parameterSource.addValues(parameters);



        JDBCWhereClauseBuilder otherWhereClause = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromName("from_date"), Operation.EQ, ":year", JDBCWhereClauseBuilder.ColumnTransformer.YEAR)
                .and()
                .where(this.formatColumnFromName("from_date"), Operation.EQ, ":month", JDBCWhereClauseBuilder.ColumnTransformer.MONTH)
                .and()
                .where(this.formatColumnFromName("from_date"), Operation.EQ, ":day", JDBCWhereClauseBuilder.ColumnTransformer.DAY);


        JDBCWhereClauseBuilder whereClauseBuilder;
        if(!otherWhereClause.toString().isEmpty()) {
            whereClauseBuilder = otherWhereClause;
            if(!staffWhereClause.toString().isEmpty()){
                whereClauseBuilder.and(staffWhereClause);
            }
        } else {
            whereClauseBuilder = staffWhereClause;
        }

        JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(Appointment.class)
                .from(this.getTableName())
                .where(whereClauseBuilder);

        return this.selectQuery(queryBuilder, parameterSource);
    }

    @Override
    public List<Appointment> findByDate(Patient patient, DateTime date) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("patient", patient.getId());
        parameterSource.addValue("year", date.getYear());
        parameterSource.addValue("month", date.getMonthOfYear());
        parameterSource.addValue("day", date.getDayOfMonth());

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromName("patient_id"), Operation.EQ, ":patient")
                .and()
                .where(this.formatColumnFromName("from_date"), Operation.EQ, ":year", JDBCWhereClauseBuilder.ColumnTransformer.YEAR)
                .and()
                .where(this.formatColumnFromName("from_date"), Operation.EQ, ":month", JDBCWhereClauseBuilder.ColumnTransformer.MONTH)
                .and()
                .where(this.formatColumnFromName("from_date"), Operation.EQ, ":day", JDBCWhereClauseBuilder.ColumnTransformer.DAY);

        JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(Appointment.class)
                .from(this.getTableName())
                .where(whereClauseBuilder);

        return this.selectQuery(queryBuilder, parameterSource);
    }

    @Override
    public void setStaff(Appointment appointment, Staff staff) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("staff_id", staff.getId());
        JDBCUpdateQueryBuilder updateQueryBuilder = new JDBCUpdateQueryBuilder()
                .update(this.getTableName(), this.getTableAlias())
                .value("staff_id", ":staff_id")
                .where(new JDBCWhereClauseBuilder()
                    .where(PRIMARY_KEY_NAME, Operation.EQ, "appointment_id")
                );
        this.updateQuery(updateQueryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public void setPatient(Appointment appointment, Patient patient) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("appointment_id", appointment.getId());
        parameterSource.addValue("patient_id", patient.getId());

        JDBCUpdateQueryBuilder updateQueryBuilder = new JDBCUpdateQueryBuilder()
                .update(this.getTableName(), this.getTableAlias())
                .value("patient_id", ":patient_id")
                .where(new JDBCWhereClauseBuilder()
                        .where(PRIMARY_KEY_NAME, Operation.EQ, ":appointment_id")
                );
        this.updateQuery(updateQueryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    protected ResultSetExtractor<List<Appointment>> getResultSetExtractor() {
        return resultSet -> {
            Map<Integer, Appointment> entitiesMap = new HashMap<>();

            Map<Integer, Patient> patientMap = new HashMap<>();
            Map<Integer, Office> officeMap = new HashMap<>();
            Map<Integer, Locality> localityMap = new HashMap<>();
            Map<Integer, Province> provinceMap = new HashMap<>();
            Map<String, Country> countryMap = new HashMap<>();
            Map<Integer, User> userMap = new HashMap<>();

            Map<Integer, Staff> staffMap = new HashMap<>();
            Map<Integer, StaffSpecialty> staffSpecialtyMap = new HashMap<>();

            List<Appointment> sortedEntities = new LinkedList<>();
            while (resultSet.next()) {
                int id;
                String idString;
                Patient patient = null;
                Staff staff = null;
                User userPatient = null;
                User userStaff = null;
                Office officeStaff = null;
                Office officePatient = null;
                StaffSpecialty staffSpecialty = null;
                Locality localityPatient = null;
                Province provincePatient = null;
                Country countryPatient = null;
                Locality localityStaff = null;
                Province provinceStaff = null;
                Country countryStaff = null;

                try {
                    id = resultSet.getInt(this.formatColumnFromAlias(this.getIdColumnName()));
                } catch (SQLException e) {
                    id = resultSet.getInt(this.getIdColumnName());
                }
                if (resultSet.wasNull())
                    continue;
                Appointment entity = entitiesMap.computeIfAbsent(id, integer -> {
                    try {
                        Appointment newAppointment = ROW_MAPPER.mapRow(this.getTableAlias(), resultSet);
                        sortedEntities.add(newAppointment);
                        return newAppointment;
                    } catch (SQLException e) {
                        return null;
                    }
                });
                if (entity == null) {
                    continue;
                }

                id = resultSet.getInt(formatColumnFromName(PatientDaoImpl.PRIMARY_KEY_NAME, "p"));
                if (!resultSet.wasNull()) {
                    patient = patientMap.computeIfAbsent(id, integer -> {
                        try {
                            return PatientDaoImpl.ROW_MAPPER.mapRow("p", resultSet);
                        } catch (SQLException e) {
                            return null;
                        }
                    });
                }
                id = resultSet.getInt(formatColumnFromName(UserDaoImpl.PRIMARY_KEY_NAME, "up"));
                if (!resultSet.wasNull()) {
                    userPatient = userMap.computeIfAbsent(id, integer -> {
                        try {
                            return UserDaoImpl.ROW_MAPPER.mapRow("up", resultSet);
                        } catch (SQLException e) {
                            return null;
                        }
                    });
                }
                id = resultSet.getInt(formatColumnFromName(OfficeDaoImpl.PRIMARY_KEY_NAME, "op"));
                if (!resultSet.wasNull()) {
                    officePatient = officeMap.computeIfAbsent(id, integer -> {
                        try {
                            return OfficeDaoImpl.ROW_MAPPER.mapRow("op", resultSet);
                        } catch (SQLException e) {
                            return null;
                        }
                    });
                }
                id = resultSet.getInt(formatColumnFromName(LocalityDaoImpl.PRIMARY_KEY_NAME, "lp"));
                if (!resultSet.wasNull()) {
                    localityPatient = localityMap.computeIfAbsent(id, integer -> {
                        try {
                            return LocalityDaoImpl.ROW_MAPPER.mapRow("lp", resultSet);
                        } catch (SQLException throwables) {
                            return null;
                        }
                    });
                }
                id = resultSet.getInt(formatColumnFromName(ProvinceDaoImpl.PRIMARY_KEY_NAME, "pps"));
                if (!resultSet.wasNull()) {
                    provincePatient = provinceMap.computeIfAbsent(id, integer -> {
                        try {
                            return ProvinceDaoImpl.ROW_MAPPER.mapRow("pps", resultSet);
                        } catch (SQLException throwables) {
                            return null;
                        }
                    });
                }
                idString = resultSet.getString(formatColumnFromName(CountryDaoImpl.PRIMARY_KEY_NAME, "cp"));
                if (!resultSet.wasNull()) {
                    countryPatient = countryMap.computeIfAbsent(idString, integer -> {
                        try {
                            return CountryDaoImpl.ROW_MAPPER.mapRow("cp", resultSet);
                        } catch (SQLException throwables) {
                            return null;
                        }
                    });
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
                    userStaff = userMap.computeIfAbsent(id, integer -> {
                        try {
                            return UserDaoImpl.ROW_MAPPER.mapRow("us", resultSet);
                        } catch (SQLException e) {
                            return null;
                        }
                    });
                }
                id = resultSet.getInt(formatColumnFromName(OfficeDaoImpl.PRIMARY_KEY_NAME, "os"));
                if (!resultSet.wasNull()) {
                    officeStaff = officeMap.computeIfAbsent(id, integer -> {
                        try {
                            return OfficeDaoImpl.ROW_MAPPER.mapRow("os", resultSet);
                        } catch (SQLException throwables) {
                            return null;
                        }
                    });
                }
                id = resultSet.getInt(formatColumnFromName(LocalityDaoImpl.PRIMARY_KEY_NAME, "l"));
                if (!resultSet.wasNull()) {
                    localityStaff = localityMap.computeIfAbsent(id, integer -> {
                        try {
                            return LocalityDaoImpl.ROW_MAPPER.mapRow("l", resultSet);
                        } catch (SQLException throwables) {
                            return null;
                        }
                    });
                }
                id = resultSet.getInt(formatColumnFromName(ProvinceDaoImpl.PRIMARY_KEY_NAME, "ps"));
                if (!resultSet.wasNull()) {
                    provinceStaff = provinceMap.computeIfAbsent(id, integer -> {
                        try {
                            return ProvinceDaoImpl.ROW_MAPPER.mapRow("ps", resultSet);
                        } catch (SQLException throwables) {
                            return null;
                        }
                    });
                }
                idString = resultSet.getString(formatColumnFromName(CountryDaoImpl.PRIMARY_KEY_NAME, "cs"));
                if (!resultSet.wasNull()) {
                    countryStaff = countryMap.computeIfAbsent(idString, integer -> {
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

                if (patient != null) {
                    entity.setPatient(patient);
                    if (officePatient != null) {
                        patient.setOffice(officePatient);
                        officePatient.setLocality(localityPatient);
                    }
                    if (userPatient != null)
                        patient.setUser(userPatient);
                }
                if (localityPatient != null && provincePatient != null) {
                    localityPatient.setProvince(provincePatient);
                }
                if (provincePatient != null && countryPatient != null) {
                    provincePatient.setCountry(countryPatient);
                }

                if (staff != null) {
                    entity.setStaff(staff);
                    if (officeStaff != null) {
                        staff.setOffice(officeStaff);
                        officeStaff.setLocality(localityStaff);
                    }
                    if (userStaff != null)
                        staff.setUser(userStaff);
                    if (staffSpecialty != null)
                        staff.getStaffSpecialties().add(staffSpecialty);
                }
                if (localityStaff != null && provinceStaff != null) {
                    localityStaff.setProvince(provinceStaff);
                }
                if (provinceStaff != null && countryStaff != null) {
                    provinceStaff.setCountry(countryStaff);
                }
            }
            return sortedEntities;
        };
    }

    @Override
    protected void populateJoins(JDBCSelectQueryBuilder selectQueryBuilder) {
        selectQueryBuilder
                .joinAlias("patient_id", PatientDaoImpl.TABLE_NAME, "p", PatientDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Patient.class)
                .joinAlias("p", "user_id", UserDaoImpl.TABLE_NAME, "up", UserDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, User.class)
                .joinAlias("p", "office_id", OfficeDaoImpl.TABLE_NAME, "op", OfficeDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Office.class)
                .joinAlias("op", "locality_id", LocalityDaoImpl.TABLE_NAME, "lp", LocalityDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Locality.class)
                .joinAlias("lp", "province_id", ProvinceDaoImpl.TABLE_NAME, "pps", ProvinceDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Province.class)
                .joinAlias("pps", "country_id", CountryDaoImpl.TABLE_NAME, "cp", CountryDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Country.class);
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

    private void putStaffsArguments(Collection<Staff> staffs, Map<String, Object> argumentsValues, JDBCWhereClauseBuilder whereClauseBuilder) {
        if (staffs.isEmpty())
            return;

        Collection<String> arguments = new HashSet<>();
        int i = 0;
        for (Staff staff : staffs) {
            String parameter = "_office_" + i;
            argumentsValues.put(parameter, staff.getId());
            arguments.add(":" + parameter);
            i++;
        }
        whereClauseBuilder
                .and()
                .in(
                        formatColumnFromName("staff_id", this.getTableAlias()),
                        arguments
                );
    }
}
