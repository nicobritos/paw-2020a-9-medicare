package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.OfficeDao;
import ar.edu.itba.paw.models.Address;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Province;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import ar.edu.itba.paw.persistence.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class OfficeDaoImpl extends GenericSearchableDaoImpl<Office, Integer> implements OfficeDao {
    private static final RowMapper<Office> ROW_MAPPER = (resultSet, rowNum) -> {
        // TODO: Fix hydration
        Province province = new Province();
        province.setId(resultSet.getInt("province_id"));

        Address address = new Address();
        address.setProvince(province);
        address.setStreet(resultSet.getString("street"));
        address.setStreetNumber(resultSet.getInt("street_number"));

        Office office = new Office();
        office.setAddress(address);
        office.setId(resultSet.getInt("office_id"));
        office.setName(resultSet.getString("name"));
        office.setPhone(resultSet.getString("phone"));
        office.setEmail(resultSet.getString("email"));

        return office;
    };

    @Override
    public Collection<Office> findByCountry(Country country) {
        return null;
    }

    @Autowired
    public OfficeDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected RowMapper<Office> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getTableName() {
        return "office";
    }

    @Override
    protected String getIdColumnName() {
        return "office_id";
    }

    @Override
    protected Map<String, Pair<String, Object>> getModelColumnsArgumentValue(Office model) {
        Map<String, Pair<String, Object>> map = new HashMap<>();

        map.put("office_id", new Pair<>(":office_id", model.getId()));
        map.put("province_id", new Pair<>(":province_id", model.getAddress().getProvince().getId()));
        map.put("street", new Pair<>(":street", model.getAddress().getStreet()));
        map.put("street_number", new Pair<>(":street_number", model.getAddress().getStreetNumber()));
        map.put("name", new Pair<>(":name", model.getName()));
        map.put("phone", new Pair<>(":phone", model.getPhone()));
        map.put("email", new Pair<>(":email", model.getEmail()));

        return map;
    }
}
