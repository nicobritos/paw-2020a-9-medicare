package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.StaffDao;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import ar.edu.itba.paw.persistence.utils.Pair;
import ar.edu.itba.paw.persistence.utils.builder.JDBCQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCSelectQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class StaffDaoImpl extends GenericSearchableDaoImpl<Staff, Integer> implements StaffDao {
    private static final RowMapper<Staff> ROW_MAPPER = (resultSet, rowNum) -> {
        // TODO: Fix office hydration
        Office office = new Office();
        office.setId(resultSet.getInt("office_id"));

        Staff staff = new Staff();
        staff.setId(resultSet.getInt("staff_id"));
        staff.setOffice(office);
        staff.setFirstName(resultSet.getString("first_name"));
        staff.setSurname(resultSet.getString("surname"));
        staff.setPhone(resultSet.getString("phone"));
        staff.setEmail(resultSet.getString("email"));
        staff.setRegistrationNumber(resultSet.getInt("registration_number"));

        return staff;
    };

    @Autowired
    public StaffDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    // TODO
    @Override
    public Collection<Staff> findByStaffSpecialties(Collection<StaffSpecialty> staffSpecialties) {
        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .select(JDBCSelectQueryBuilder.ALL)
                .from(this.getSpecialtiesTableName())
                .join(this.getTableName(), "staff_id", "staff_id")
                .where(new JDBCWhereClauseBuilder()
                        .in(
                                this.formatColumnFromName("specialty_id", this.getSpecialtiesTableName()),
                                staffSpecialties.stream().map(staffSpecialty -> staffSpecialty.getId().toString()).collect(Collectors.toList())
                        )
                )
                .distinct();

        return this.query(queryBuilder.getQueryAsString());
    }

    @Override
    protected RowMapper<Staff> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getTableName() {
        return "staff";
    }

    @Override
    protected String getIdColumnName() {
        return "staff_id";
    }

    private String getSpecialtiesTableName() {
        return "system_staff_specialty_staff";
    }

    @Override
    protected Map<String, Pair<String, Object>> getModelColumnsArgumentValue(Staff model) {
        Map<String, Pair<String, Object>> map = new HashMap<>();

        map.put("staff_id", new Pair<>(":staff_id", model.getId()));
        map.put("office_id", new Pair<>(":office_id", model.getOffice().getId()));
        map.put("first_name", new Pair<>(":first_name", model.getFirstName()));
        map.put("surname", new Pair<>(":surname", model.getSurname()));
        map.put("registration_number", new Pair<>(":registration_number", model.getRegistrationNumber()));
        map.put("phone", new Pair<>(":phone", model.getPhone()));
        map.put("email", new Pair<>(":email", model.getEmail()));

        return map;
    }
}
