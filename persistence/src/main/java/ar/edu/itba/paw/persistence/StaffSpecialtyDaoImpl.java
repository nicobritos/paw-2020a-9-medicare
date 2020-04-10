package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.StaffSpecialtyDao;
import ar.edu.itba.paw.models.StaffSpecialty;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import ar.edu.itba.paw.persistence.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

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
        super(dataSource);
    }

    @Override
    protected RowMapper<StaffSpecialty> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getTableName() {
        return "system_staff_specialty";
    }

    @Override
    protected String getIdColumnName() {
        return "specialty_id";
    }

    @Override
    protected Map<String, Pair<String, Object>> getModelColumnsArgumentValue(StaffSpecialty model) {
        Map<String, Pair<String, Object>> map = new HashMap<>();

        map.put("specialty_id", new Pair<>(":specialty_id", model.getId()));
        map.put("name", new Pair<>(":name", model.getName()));

        return map;
    }
}
