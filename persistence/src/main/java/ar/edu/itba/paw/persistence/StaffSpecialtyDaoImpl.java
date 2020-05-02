package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.StaffSpecialtyDao;
import ar.edu.itba.paw.models.StaffSpecialty;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
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
public class StaffSpecialtyDaoImpl extends GenericSearchableDaoImpl<StaffSpecialty, Integer> implements StaffSpecialtyDao {
    public static final RowMapperAlias<StaffSpecialty> ROW_MAPPER = (prefix, resultSet) -> {
        StaffSpecialty staffSpecialty = new StaffSpecialty();
        staffSpecialty.setId(resultSet.getInt(formatColumnFromName(StaffSpecialtyDaoImpl.PRIMARY_KEY_NAME, prefix)));
        populateEntity(staffSpecialty, resultSet, prefix);
        return staffSpecialty;
    };
    public static final String TABLE_NAME = getTableNameFromModel(StaffSpecialty.class);
    public static final String PRIMARY_KEY_NAME = getPrimaryKeyNameFromModel(StaffSpecialty.class);

    @Autowired
    public StaffSpecialtyDaoImpl(DataSource dataSource) {
        super(dataSource, StaffSpecialty.class, Integer.class);
    }

    @Override
    protected ResultSetExtractor<List<StaffSpecialty>> getResultSetExtractor() {
        return resultSet -> {
            Map<Integer, StaffSpecialty> entityMap = new HashMap<>();
            List<StaffSpecialty> sortedEntities = new LinkedList<>();
            while (resultSet.next()) {
                entityMap.computeIfAbsent(resultSet.getInt(this.formatColumnFromAlias(this.getIdColumnName())), string -> {
                    try {
                        StaffSpecialty newEntity = ROW_MAPPER.mapRow(this.getTableAlias(), resultSet);
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
}
