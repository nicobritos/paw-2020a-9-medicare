package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.PatientDao;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

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
    protected RowMapper<Patient> getRowMapper() {
        return this.rowMapper;
    }
}
