package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.StaffDao;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collection;

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
        return null;
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
}
