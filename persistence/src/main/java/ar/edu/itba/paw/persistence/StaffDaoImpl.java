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
import java.util.*;

@Repository
public class StaffDaoImpl extends GenericSearchableDaoImpl<Staff, Integer> implements StaffDao {
    private final RowMapper<Staff> rowMapper = (resultSet, rowNum) -> this.hydrate(resultSet);
    public static final String TABLE_NAME = getTableNameFromModel(Staff.class);
    public static final String PRIMARY_KEY_NAME = getPrimaryKeyNameFromModel(Staff.class);

    @Autowired
    public StaffDaoImpl(DataSource dataSource) {
        super(dataSource, Staff.class, Integer.class);
    }

    @Override
    public Set<Staff> findBySurname(String surname) {
        return this.findByFieldIgnoreCase("surname", Operation.LIKE, surname);
    }

    @Override
    public Set<Staff> findByName(String name) {
        return this.findByFieldIgnoreCase("first_name", Operation.LIKE, name);
    }

    @Override
    public Set<Staff> findByStaffSpecialties(Collection<StaffSpecialty> staffSpecialties) {
        if (staffSpecialties.isEmpty())
            return new HashSet<>();

        Map<String, Object> parameters = new HashMap<>();
        Collection<String> specialtyParameters = new HashSet<>();

        int i = 0;
        for (StaffSpecialty staffSpecialty : staffSpecialties) {
            String parameter = "_specialty_" + i;
            parameters.put(parameter, staffSpecialty.getId());
            specialtyParameters.add(":" + parameter);
            i++;
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .join("staff_id", this.getSpecialtiesIntermediateTableName(), "staff_id")
                .where(new JDBCWhereClauseBuilder()
                        .in(
                                formatColumnFromName("specialty_id", this.getSpecialtiesIntermediateTableName()),
                                specialtyParameters
                        )
                )
                .distinct();

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(parameters);

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public Set<Staff> findByNameAndSurname(String name, String surname) {
        if (name.isEmpty() && surname.isEmpty())
            return new HashSet<>();

        Map<String, Object> parameters = new HashMap<>();
        name = name.toLowerCase();
        surname = surname.toLowerCase();
        parameters.put("firstName", '%' + name + '%');
        parameters.put("surname", '%' + surname + '%');

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(parameters);

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .where(new JDBCWhereClauseBuilder()
                        .where(this.formatColumnFromName("surname"), Operation.LIKE, ":surname", ColumnTransformer.LOWER)
                        .and()
                        .where(this.formatColumnFromName("first_name"), Operation.LIKE, ":firstName", ColumnTransformer.LOWER)
                )
                .distinct();

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public Set<Staff> findByNameAndStaffSpecialties(String name, Collection<StaffSpecialty> staffSpecialties) {
        if (staffSpecialties.isEmpty() && name.isEmpty())
            return new HashSet<>();

        Map<String, Object> parameters = new HashMap<>();
        Collection<String> specialtyParameters = new HashSet<>();
        name = name.toLowerCase();
        parameters.put("firstName", '%' + name + '%');
        int i = 0;
        for (StaffSpecialty staffSpecialty : staffSpecialties) {
            String parameter = "_specialty_" + i;
            parameters.put(parameter, staffSpecialty.getId());
            specialtyParameters.add(":" + parameter);
            i++;
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(parameters);


        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .join("staff_id", this.getSpecialtiesIntermediateTableName(), "staff_id")
                .where(new JDBCWhereClauseBuilder()
                        .in(
                                formatColumnFromName("specialty_id", this.getSpecialtiesIntermediateTableName()),
                                specialtyParameters
                        )
                        .and()
                        .where(new JDBCWhereClauseBuilder()
                                .where(this.formatColumnFromName("first_name"), Operation.LIKE, ":firstName", ColumnTransformer.LOWER)
                        )
                )
                .distinct();

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public Set<Staff> findBySurnameAndOffice(String surname, Collection<Office> offices) {
        if (offices.isEmpty() && surname.isEmpty())
            return new HashSet<>();

        Map<String, Object> parameters = new HashMap<>();
        Collection<String> officeParameters = new HashSet<>();

        int i = 0;
        for (Office office : offices) {
            String parameter = "_office_" + i;
            parameters.put(parameter, office.getId());
            officeParameters.add(":" + parameter);
            i++;
        }


        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .in(
                        formatColumnFromName("office_id", this.getTableAlias()),
                        officeParameters
                );

        if (!surname.isEmpty()) {
            surname = surname.toLowerCase();
            parameters.put("surname", '%' + surname + '%');

            whereClauseBuilder
                    .and()
                    .where(new JDBCWhereClauseBuilder()
                            .where(this.formatColumnFromName("surname"), Operation.LIKE, ":surname", ColumnTransformer.LOWER)
                    );
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .where(whereClauseBuilder)
                .distinct();

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(parameters);

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public Set<Staff> findBySurnameAndStaffSpecialties(String surname, Collection<StaffSpecialty> staffSpecialties) {
        if (staffSpecialties.isEmpty() && surname.isEmpty())
            return new HashSet<>();

        Map<String, Object> parameters = new HashMap<>();
        Collection<String> specialtyParameters = new LinkedList<>();
        surname = surname.toLowerCase();
        parameters.put("surname", '%' + surname + '%');

        int i = 0;
        for (StaffSpecialty staffSpecialty : staffSpecialties) {
            String parameter = "_specialty_" + i;
            parameters.put(parameter, staffSpecialty.getId());
            specialtyParameters.add(":" + parameter);
            i++;
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(parameters);


        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .join("staff_id", this.getSpecialtiesIntermediateTableName(), "staff_id")
                .where(new JDBCWhereClauseBuilder()
                        .in(
                                formatColumnFromName("specialty_id", this.getSpecialtiesIntermediateTableName()),
                                specialtyParameters
                        )
                        .and()
                        .where(new JDBCWhereClauseBuilder()
                                .where(this.formatColumnFromName("surname"), Operation.LIKE, ":surname", ColumnTransformer.LOWER)
                        )
                )
                .distinct();

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public Set<Staff> findByNameOfficeAndStaffSpecialties(String name, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties) {
        return findByNameSurnameOfficeAndStaffSpecialities(name, "", offices, staffSpecialties);
    }

    @Override
    public Set<Staff> findByNameSurnameAndStaffSpecialties(String name, String surname, Collection<StaffSpecialty> staffSpecialties) {
        if (staffSpecialties.isEmpty() && name.isEmpty())
            return new HashSet<>();

        Map<String, Object> parameters = new HashMap<>();
        Collection<String> specialtyParameters = new LinkedList<>();
        name = name.toLowerCase();
        surname = surname.toLowerCase();
        parameters.put("firstName", '%' + name + '%');
        parameters.put("surname", '%' + surname + '%');

        int i = 0;
        for (StaffSpecialty staffSpecialty : staffSpecialties) {
            String parameter = "_specialty_" + i;
            parameters.put(parameter, staffSpecialty.getId());
            specialtyParameters.add(":" + parameter);
            i++;
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(parameters);


        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .join("staff_id", this.getSpecialtiesIntermediateTableName(), "staff_id")
                .where(new JDBCWhereClauseBuilder()
                        .in(
                                formatColumnFromName("specialty_id", this.getSpecialtiesIntermediateTableName()),
                                specialtyParameters
                        )
                        .and()
                        .where(new JDBCWhereClauseBuilder()
                                .where(this.formatColumnFromName("first_name"), Operation.LIKE, ":firstName", ColumnTransformer.LOWER)
                                .and()
                                .where(this.formatColumnFromName("surname"), Operation.LIKE, ":surname", ColumnTransformer.LOWER)

                        )
                )
                .distinct();

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public Set<Staff> findByNameSurnameAndOffice(String name, String surname, Collection<Office> offices) {
        if (offices.isEmpty() && name.isEmpty())
            return new HashSet<>();

        Map<String, Object> parameters = new HashMap<>();
        Collection<String> officeParameters = new LinkedList<>();

        int i = 0;
        for (Office office : offices) {
            String parameter = "_office_" + i;
            parameters.put(parameter, office.getId());
            officeParameters.add(":" + parameter);
            i++;
        }


        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .in(
                        formatColumnFromName("office_id", this.getTableAlias()),
                        officeParameters
                );

        if (!name.isEmpty()) {
            name = name.toLowerCase();
            surname = surname.toLowerCase();
            parameters.put("firstName", '%' + name + '%');
            parameters.put("surname", '%' + surname + '%');


            whereClauseBuilder
                    .and()
                    .where(new JDBCWhereClauseBuilder()
                            .where(this.formatColumnFromName("first_name"), Operation.LIKE, ":firstName", ColumnTransformer.LOWER)
                            .and()
                            .where(this.formatColumnFromName("surname"), Operation.LIKE, ":surname", ColumnTransformer.LOWER)

                    );
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .where(whereClauseBuilder)
                .distinct();

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(parameters);

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public Set<Staff> findByNameSurnameOfficeAndStaffSpecialities(String name, String surname, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties) {
        if (staffSpecialties.isEmpty() && offices.isEmpty() && name.isEmpty() && surname.isEmpty())
            return new HashSet<>();

        Map<String, Object> parameters = new HashMap<>();
        Collection<String> officeParameters = new LinkedList<>();
        Collection<String> specialtyParameters = new LinkedList<>();

        int i = 0;
        for (Office office : offices) {
            String parameter = "_office_" + i;
            parameters.put(parameter, office.getId());
            officeParameters.add(":" + parameter);
            i++;
        }

        i = 0;
        for (StaffSpecialty staffSpecialty : staffSpecialties) {
            String parameter = "_specialty_" + i;
            parameters.put(parameter, staffSpecialty.getId());
            specialtyParameters.add(":" + parameter);
            i++;
        }

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .in(
                        formatColumnFromName("specialty_id", this.getSpecialtiesIntermediateTableName()),
                        specialtyParameters
                )
                .and()
                .in(
                        formatColumnFromName("office_id", this.getTableAlias()),
                        officeParameters
                );

        if (!name.isEmpty()) {
            name = name.toLowerCase();
            surname = surname.toLowerCase();
            parameters.put("firstName", '%' + name + '%');
            parameters.put("surname", '%' + surname + '%');

            whereClauseBuilder
                    .and()
                    .where(new JDBCWhereClauseBuilder()
                            .where(this.formatColumnFromName("surname"), Operation.LIKE, ":surname", ColumnTransformer.LOWER)
                            .and()
                            .where(this.formatColumnFromName("first_name"), Operation.LIKE, ":firstName", ColumnTransformer.LOWER)
                    );
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .join("staff_id", this.getSpecialtiesIntermediateTableName(), "staff_id")
                .where(whereClauseBuilder)
                .distinct();

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(parameters);

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public Set<Staff> findByOfficeAndStaffSpecialties(Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties) {
        return this.findByNameOfficeAndStaffSpecialties("", offices, staffSpecialties);
    }

    @Override
    public Set<Staff> findBySurnameOfficeAndStaffSpecialties(String surname, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties) {
        return findByNameSurnameOfficeAndStaffSpecialities("", surname, offices, staffSpecialties);
    }

    @Override
    public Set<Staff> findByNameAndOffice(String name, Collection<Office> offices) {
        if (offices.isEmpty() && name.isEmpty())
            return new HashSet<>();

        Map<String, Object> parameters = new HashMap<>();
        Collection<String> officeParameters = new LinkedList<>();

        int i = 0;
        for (Office office : offices) {
            String parameter = "_office_" + i;
            parameters.put(parameter, office.getId());
            officeParameters.add(":" + parameter);
            i++;
        }


        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .in(
                        formatColumnFromName("office_id", this.getTableAlias()),
                        officeParameters
                );

        if (!name.isEmpty()) {
            name = name.toLowerCase();
            parameters.put("firstName", '%' + name + '%');

            whereClauseBuilder
                    .and()
                    .where(new JDBCWhereClauseBuilder()
                            .where(this.formatColumnFromName("first_name"), Operation.LIKE, ":firstName", ColumnTransformer.LOWER)
                    );
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .where(whereClauseBuilder)
                .distinct();

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(parameters);

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public Set<Staff> findByOffice(Collection<Office> offices) {
        return this.findByFieldIn("office_id", offices);
    }

    @Override
    protected RowMapper<Staff> getRowMapper() {
        return this.rowMapper;
    }

    private String getSpecialtiesIntermediateTableName() {
        return "system_staff_specialty_staff";
    }
}
