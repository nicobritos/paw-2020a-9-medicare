package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.StaffDao;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import ar.edu.itba.paw.persistence.utils.builder.JDBCQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCSelectQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.ColumnTransformer;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class StaffDaoImpl extends GenericSearchableDaoImpl<Staff, Integer> implements StaffDao {
    private static final String SPECIALTIES_TABLE_NAME = getTableNameFromModel(StaffSpecialty.class);
    private static final RowMapper<Staff> ROW_MAPPER = (resultSet, rowNum) -> {
        // TODO: Fix office hydration
//        Office office = new Office();
//        office.setId(resultSet.getInt("office_id"));
        return hydrate(Staff.class, resultSet);
    };

    @Autowired
    public StaffDaoImpl(DataSource dataSource) {
        super(dataSource, Staff.class);
    }

    @Override
    public Collection<Staff> findByStaffSpecialties(Collection<StaffSpecialty> staffSpecialties) {
        Map<String, Object> parameters = new HashMap<>();
        Collection<String> specialtyParameters = new LinkedList<>();

        int i = 0;
        for (StaffSpecialty staffSpecialty : staffSpecialties) {
            String parameter = "_specialty_" + i;
            parameters.put("_specialty_" + i, staffSpecialty.getId());
            specialtyParameters.add(parameter);
            i++;
        }


        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .select(JDBCSelectQueryBuilder.ALL)
                .from(this.getSpecialtiesTableName())
                .join(this.getTableName(), "staff_id", "staff_id")
                .where(new JDBCWhereClauseBuilder()
                        .in(
                                formatColumnFromName("specialty_id", this.getSpecialtiesTableName()),
                                specialtyParameters
                        )
                )
                .distinct();

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(parameters);

        return this.query(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public Collection<Staff> findByNameAndStaffSpecialties(String name, Collection<StaffSpecialty> staffSpecialties) {
        Map<String, Object> parameters = new HashMap<>();
        Collection<String> specialtyParameters = new LinkedList<>();
        parameters.put("name", name.toLowerCase());

        int i = 0;
        for (StaffSpecialty staffSpecialty : staffSpecialties) {
            String parameter = "_specialty_" + i;
            parameters.put("_specialty_" + i, staffSpecialty.getId());
            specialtyParameters.add(parameter);
            i++;
        }


        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .select(JDBCSelectQueryBuilder.ALL)
                .from(this.getSpecialtiesTableName())
                .join(this.getTableName(), "staff_id", "staff_id")
                .where(new JDBCWhereClauseBuilder()
                        .in(
                                formatColumnFromName("specialty_id", this.getSpecialtiesTableName()),
                                specialtyParameters
                        )
                        .and()
                        .where(this.formatColumnFromName("name"), Operation.LIKE, ":name", ColumnTransformer.LOWER)
                )
                .distinct();

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(parameters);

        return this.query(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public Collection<Staff> findByNameOfficeAndStaffSpecialties(String name, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties) {
        Map<String, Object> parameters = new HashMap<>();
        Collection<String> officeParameters = new LinkedList<>();
        Collection<String> specialtyParameters = new LinkedList<>();

        int i = 0;
        for (Office office : offices) {
            String parameter = "_office_" + i;
            parameters.put(parameter, office.getId());
            officeParameters.add(parameter);
            i++;
        }

        i = 0;
        for (StaffSpecialty staffSpecialty : staffSpecialties) {
            String parameter = "_specialty_" + i;
            parameters.put("_specialty_" + i, staffSpecialty.getId());
            specialtyParameters.add(parameter);
            i++;
        }


        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder();
        whereClauseBuilder
                .in(
                        formatColumnFromName("specialty_id", this.getSpecialtiesTableName()),
                        specialtyParameters
                )
                .and()
                .in(
                        formatColumnFromName("office_id", this.getSpecialtiesTableName()),
                        officeParameters
                );

        if (name != null && !name.isEmpty()) {
            parameters.put("name", name.toLowerCase());
            whereClauseBuilder
                    .and()
                    .where(this.formatColumnFromName("name"), Operation.LIKE, ":name", ColumnTransformer.LOWER);
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .select(JDBCSelectQueryBuilder.ALL)
                .from(this.getSpecialtiesTableName())
                .join(this.getTableName(), "staff_id", "staff_id")
                .where(whereClauseBuilder)
                .distinct();

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(parameters);

        return this.query(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public Collection<Staff> findByOfficeAndStaffSpecialties(Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties) {
        return this.findByNameOfficeAndStaffSpecialties("", offices, staffSpecialties);
    }

    @Override
    public Collection<Staff> findByOffice(Collection<Office> offices) {
        return this.findByFieldIn("office_id", offices.stream().map(Office::getId).collect(Collectors.toList()));
    }

    @Override
    protected RowMapper<Staff> getRowMapper() {
        return ROW_MAPPER;
    }

    private String getSpecialtiesTableName() {
        return SPECIALTIES_TABLE_NAME;
    }
}
