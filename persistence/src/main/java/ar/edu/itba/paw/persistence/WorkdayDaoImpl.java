package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.WorkdayDao;
import ar.edu.itba.paw.models.AppointmentTimeSlot;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.Workday;
import ar.edu.itba.paw.models.WorkdayDay;
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
import java.util.List;

@Repository
public class WorkdayDaoImpl extends GenericDaoImpl<Workday, Integer> implements WorkdayDao {
    private final RowMapper<Workday> rowMapper = (resultSet, rowNum) -> this.hydrate(resultSet);
    public static final String TABLE_NAME = getTableNameFromModel(Workday.class);
    public static final String PRIMARY_KEY_NAME = getPrimaryKeyNameFromModel(Workday.class);

    @Autowired
    public WorkdayDaoImpl(DataSource dataSource) {
        super(dataSource, Workday.class, Integer.class);
    }

    @Override
    public List<Workday> findByStaff(Staff staff) {
        FilteredCachedCollection<Workday> cachedCollection = CacheHelper.filter(
                Workday.class,
                Integer.class,
                workday -> workday.getStaff().getId().equals(staff.getId())
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
    public List<Workday> findByStaff(Staff staff, WorkdayDay day) {
        FilteredCachedCollection<Workday> cachedCollection = CacheHelper.filter(
                Workday.class,
                Integer.class,
                workday -> workday.getDay().equals(day.name()) && workday.getStaff().getId().equals(staff.getId())
        );
        if (this.isCacheComplete(cachedCollection)) {
            return cachedCollection.getCollectionAsList();
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("staff", staff.getId());
        parameterSource.addValue("day", day.name());

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where("day", Operation.EQ, ":day")
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
    public boolean isStaffWorking(Staff staff, AppointmentTimeSlot timeSlot) {
        FilteredCachedCollection<Workday> cachedCollection = CacheHelper.filter(
                Workday.class,
                Integer.class,
                workday -> workday.getDay().equals(timeSlot.getDay())
                        && workday.getStaff().getId().equals(staff.getId())
                        && workday.getStartHour() <= timeSlot.getFromHour()
                        && (workday.getEndHour() > timeSlot.getToHour()
                            || (workday.getEndHour() == timeSlot.getToHour()
                                && timeSlot.getToMinute() == 0
                        ))
        );
        if (!cachedCollection.getCollection().isEmpty()) {
            return true;
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("staff", staff.getId());
        parameterSource.addValue("day", timeSlot.getDay());
        parameterSource.addValue("from_hour", timeSlot.getFromHour());
        parameterSource.addValue("to_hour", timeSlot.getToHour());

        Operation toHourOperation;
        if (timeSlot.getToMinute() == 0) {
            toHourOperation = Operation.GEQ;
        } else {
            toHourOperation = Operation.GT;
        }

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where("day", Operation.EQ, ":day")
                .and()
                .where("from_hour", Operation.LEQ, ":from_hour")
                .and()
                .where("to_hour", toHourOperation, ":to_hour")
                .and()
                .where(this.formatColumnFromName("staff_id"), Operation.EQ, ":staff");

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableName())
                .where(whereClauseBuilder);

        return !this.selectQuery(queryBuilder.getQueryAsString(), parameterSource).isEmpty();
    }

    @Override
    protected RowMapper<Workday> getRowMapper() {
        return this.rowMapper;
    }
}
