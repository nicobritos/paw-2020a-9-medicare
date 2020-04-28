package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.AppointmentDao;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.persistence.generics.GenericDaoImpl;
import ar.edu.itba.paw.persistence.utils.builder.JDBCQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCSelectQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.Operation;
import ar.edu.itba.paw.persistence.utils.cache.CacheHelper;
import ar.edu.itba.paw.persistence.utils.cache.FilteredCachedCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class AppointmentDaoImpl extends GenericDaoImpl<Appointment, Integer> implements AppointmentDao {
    private final RowMapper<Appointment> rowMapper = (resultSet, rowNum) -> this.hydrate(resultSet);
    public static final String TABLE_NAME = getTableNameFromModel(Appointment.class);
    public static final String PRIMARY_KEY_NAME = getPrimaryKeyNameFromModel(Appointment.class);

    @Autowired
    public AppointmentDaoImpl(DataSource dataSource) {
        super(dataSource, Appointment.class, Integer.class);
    }

    @Override
    public List<Appointment> findPending(Patient patient) {
        FilteredCachedCollection<Appointment> cachedCollection = CacheHelper.filter(
                Appointment.class,
                Integer.class,
                appointment -> appointment.getPatient().getId().equals(patient.getId())
        );
        if (this.isCacheComplete(cachedCollection)) {
            return cachedCollection.getCollectionAsList();
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("patient", patient.getId());

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromName("patient_id"), Operation.EQ, ":patient");

        if (!cachedCollection.getCollection().isEmpty()) {
            this.excludeModels(cachedCollection.getCompleteCollection(), parameterSource, whereClauseBuilder);
        }
        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableName())
                .where(whereClauseBuilder);

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public List<Appointment> findPending(Staff staff) {
        FilteredCachedCollection<Appointment> cachedCollection = CacheHelper.filter(
                Appointment.class,
                Integer.class,
                appointment -> appointment.getStaff().getId().equals(staff.getId())
        );
        if (this.isCacheComplete(cachedCollection)) {
            return cachedCollection.getCollectionAsList();
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("staff", staff.getId());

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromName("staff_id"), Operation.EQ, ":staff");

        if (!cachedCollection.getCollection().isEmpty()) {
            this.excludeModels(cachedCollection.getCompleteCollection(), parameterSource, whereClauseBuilder);
        }
        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableName())
                .where(whereClauseBuilder);

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public List<Appointment> findPending(Patient patient, Staff staff) {
        FilteredCachedCollection<Appointment> cachedCollection = CacheHelper.filter(
                Appointment.class,
                Integer.class,
                appointment -> appointment.getPatient().getId().equals(patient.getId()) && appointment.getStaff().getId().equals(staff.getId())
        );
        if (this.isCacheComplete(cachedCollection)) {
            return cachedCollection.getCollectionAsList();
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("patient", patient.getId());
        parameterSource.addValue("staff", staff.getId());

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromName("patient_id"), Operation.EQ, ":patient")
                .and()
                .where(this.formatColumnFromName("staff_id"), Operation.EQ, ":staff");

        if (!cachedCollection.getCollection().isEmpty()) {
            this.excludeModels(cachedCollection.getCompleteCollection(), parameterSource, whereClauseBuilder);
        }
        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableName())
                .where(whereClauseBuilder);

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public List<Appointment> findByDate(Staff staff, LocalDate date) {
        FilteredCachedCollection<Appointment> cachedCollection = CacheHelper.filter(
                Appointment.class,
                Integer.class,
                appointment -> appointment.getStaff().getId().equals(staff.getId()) &&
                        appointment.getFromDate().getDate() == LocalDate.now().getDayOfMonth()
        );
        if (this.isCacheComplete(cachedCollection)) {
            return cachedCollection.getCollectionAsList();
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("staff", staff.getId());
        parameterSource.addValue("year", date.getYear());
        parameterSource.addValue("month", date.getMonthValue());
        parameterSource.addValue("day", date.getDayOfMonth());

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromName("staff_id"), Operation.EQ, ":staff")
                .and()
                .where(this.formatColumnFromName("from_date"), Operation.EQ, ":year", JDBCWhereClauseBuilder.ColumnTransformer.YEAR)
                .and()
                .where(this.formatColumnFromName("from_date"), Operation.EQ, ":month", JDBCWhereClauseBuilder.ColumnTransformer.MONTH)
                .and()
                .where(this.formatColumnFromName("from_date"), Operation.EQ, ":day", JDBCWhereClauseBuilder.ColumnTransformer.DAY);

        if (!cachedCollection.getCollection().isEmpty()) {
            this.excludeModels(cachedCollection.getCompleteCollection(), parameterSource, whereClauseBuilder);
        }
        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableName())
                .where(whereClauseBuilder);

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    protected RowMapper<Appointment> getRowMapper() {
        return this.rowMapper;
    }
}
