package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.PatientDao;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
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
import java.util.Optional;

@Repository
public class PatientDaoImpl extends GenericSearchableDaoImpl<Patient, Integer> implements PatientDao {
    private final RowMapper<Patient> rowMapper = (resultSet, rowNum) -> this.hydrate(resultSet);
    public static final String TABLE_NAME = getTableNameFromModel(Patient.class);
    public static final String PRIMARY_KEY_NAME = getPrimaryKeyNameFromModel(Patient.class);

    @Autowired
    public PatientDaoImpl(DataSource dataSource) {
        super(dataSource, Patient.class, Integer.class);
    }

    @Override
    public Optional<Patient> findByUserAndOffice(User user, Office office) {
        FilteredCachedCollection<Patient> cachedCollection = CacheHelper.filter(
                Patient.class,
                Integer.class,
                patient -> patient.getUser().equals(user) && patient.getOffice().equals(office)
        );
        if (this.isCacheComplete(cachedCollection)) {
            return cachedCollection.getCollectionAsList().stream().findFirst();
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("user_id", user.getId());
        parameterSource.addValue("office_id", office.getId());

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromName("office_id"), Operation.EQ, ":office_id")
                .and()
                .where(this.formatColumnFromName("user_id"), Operation.EQ, ":user_id");

        if (!cachedCollection.getCollection().isEmpty()) {
            this.excludeModels(cachedCollection.getCompleteCollection(), parameterSource, whereClauseBuilder);
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableName())
                .where(whereClauseBuilder);

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource).stream().findFirst();
    }

    @Override
    protected RowMapper<Patient> getRowMapper() {
        return this.rowMapper;
    }
}
