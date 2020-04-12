package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.StaffSpecialtyDao;
import ar.edu.itba.paw.models.StaffSpecialty;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class StaffSpecialtyDaoImpl extends GenericSearchableDaoImpl<StaffSpecialty, Integer> implements StaffSpecialtyDao {
    private final RowMapper<StaffSpecialty> rowMapper = (resultSet, rowNum) -> this.hydrate(resultSet);
    public static final String TABLE_NAME = getTableNameFromModel(StaffSpecialty.class);

    @Autowired
    public StaffSpecialtyDaoImpl(DataSource dataSource) {
        super(dataSource, StaffSpecialty.class);
    }

    @Override
    protected RowMapper<StaffSpecialty> getRowMapper() {
        return rowMapper;
    }
}
