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
    private static final RowMapper<StaffSpecialty> ROW_MAPPER = (resultSet, rowNum) -> {
        StaffSpecialty staffSpecialty = new StaffSpecialty();
        staffSpecialty.setId(resultSet.getInt("specialty_id"));
        staffSpecialty.setName(resultSet.getString("name"));

        return staffSpecialty;
    };

    @Autowired
    public StaffSpecialtyDaoImpl(DataSource dataSource) {
        super(dataSource, StaffSpecialty.class);
    }

    @Override
    protected RowMapper<StaffSpecialty> getRowMapper() {
        return ROW_MAPPER;
    }
}
