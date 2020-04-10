package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.OfficeDao;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collection;

@Repository
public class OfficeDaoImpl extends GenericSearchableDaoImpl<Office, Integer> implements OfficeDao {
    private static final RowMapper<Office> ROW_MAPPER = (resultSet, rowNum) -> {
        // TODO: Fix hydration
//        Province province = new Province();
//        province.setId(resultSet.getInt("province_id"));

        return hydrate(Office.class, resultSet);
    };

    @Autowired
    public OfficeDaoImpl(DataSource dataSource) {
        super(dataSource, Office.class);
    }

    @Override
    public Collection<Office> findByCountry(Country country) {
        return null;
    }

    @Override
    protected RowMapper<Office> getRowMapper() {
        return ROW_MAPPER;
    }
}
