package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.StaffDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import ar.edu.itba.paw.persistence.utils.JDBCArgumentValue;
import ar.edu.itba.paw.persistence.utils.RowMapperAlias;
import ar.edu.itba.paw.persistence.utils.StringSearchType;
import ar.edu.itba.paw.persistence.utils.builder.JDBCInsertQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCInsertQueryBuilder.OnConflictPolicy;
import ar.edu.itba.paw.persistence.utils.builder.JDBCSelectQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCSelectQueryBuilder.JoinType;
import ar.edu.itba.paw.persistence.utils.builder.JDBCUpdateQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.ColumnTransformer;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class StaffDaoImpl extends GenericSearchableDaoImpl<Staff, Integer> implements StaffDao {
    private static final int DEFAULT_PAGE_SIZE = 10;
    public static final RowMapperAlias<Staff> ROW_MAPPER = (prefix, resultSet) -> {
        Staff staff = new Staff();
        try {
            staff.setId(resultSet.getInt(formatColumnFromName(StaffDaoImpl.PRIMARY_KEY_NAME, prefix)));
        } catch (SQLException e) {
            staff.setId(resultSet.getInt(StaffDaoImpl.PRIMARY_KEY_NAME));
        }
        populateEntity(staff, resultSet, prefix);
        return staff;
    };
    public static final String TABLE_NAME = getTableNameFromModel(Staff.class);
    public static final String PRIMARY_KEY_NAME = getPrimaryKeyNameFromModel(Staff.class);

    @Autowired
    public StaffDaoImpl(DataSource dataSource) {
        super(dataSource, Staff.class);
    }

    @Override
    public List<Staff> findBy(String name, String surname, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties, Collection<Locality> localities) {
        if (name == null) {
            name = "";
        } else {
            name = StringUtils.stripAccents(name.toLowerCase());
        }
        if (surname == null) {
            surname = "";
        } else {
            surname = StringUtils.stripAccents(surname.toLowerCase());
        }
        if (offices == null) {
            offices = Collections.emptyList();
        }
        if (staffSpecialties == null) {
            staffSpecialties = Collections.emptyList();
        }
        if(localities == null){
            localities = Collections.emptyList();
        }
        if (staffSpecialties.isEmpty() && name.isEmpty() && surname.isEmpty() && offices.isEmpty() && localities.isEmpty())
            return this.list();

        Map<String, Object> parameters = new HashMap<>();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder();

        this.putFirstNameArguments(name, parameters, whereClauseBuilder);
        this.putSurnameArgument(surname, parameters, whereClauseBuilder);
        this.putOfficeArguments(offices, parameters, whereClauseBuilder);
        this.putStaffSpecialtyArguments(staffSpecialties, parameters, whereClauseBuilder);
        this.putLocalityArguments(localities, parameters, whereClauseBuilder);

        parameterSource.addValues(parameters);

        JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(Staff.class)
                .from(this.getTableAlias())
                .where(whereClauseBuilder)
                .distinct();
        if(!staffSpecialties.isEmpty()) {
            queryBuilder.join("staff_id", this.getSpecialtiesIntermediateTableName(), "staff_id", null);
        }
        if(!localities.isEmpty()){
            queryBuilder.join("office_id", OfficeDaoImpl.TABLE_NAME, "office_id", Office.class);
        }
        return this.selectQuery(queryBuilder, parameterSource);
    }

    @Override
    public Paginator<Staff> findBy(String name, String surname, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties, Collection<Locality> localities, int page, int pageSize) {
        if(page<=0)
            page = 1;

        if(pageSize<=0)
            pageSize=DEFAULT_PAGE_SIZE;

        if (name == null) {
            name = "";
        } else {
            name = name.toLowerCase();
        }
        if (surname == null) {
            surname = "";
        } else {
            surname = surname.toLowerCase();
        }
        if (offices == null) {
            offices = Collections.emptyList();
        }
        if (staffSpecialties == null) {
            staffSpecialties = Collections.emptyList();
        }
        if(localities == null){
            localities = Collections.emptyList();
        }

        Map<String, Object> parameters = new HashMap<>();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder();

        this.putFirstNameArguments(name, parameters, whereClauseBuilder);
        this.putSurnameArgument(surname, parameters, whereClauseBuilder);
        this.putOfficeArguments(offices, parameters, whereClauseBuilder);
        this.putStaffSpecialtyArguments(staffSpecialties, parameters, whereClauseBuilder);
        this.putLocalityArguments(localities, parameters, whereClauseBuilder);

        parameterSource.addValues(parameters);

        JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(Staff.class)
                .from(this.getTableAlias())
                .where(whereClauseBuilder)
                .distinct()
                .limit(pageSize)
                .offset((page-1)*pageSize);
        if(!staffSpecialties.isEmpty()) {
            queryBuilder.join("staff_id", this.getSpecialtiesIntermediateTableName(), "staff_id", null);
        }
        if(!localities.isEmpty()){
            queryBuilder.join("office_id", OfficeDaoImpl.TABLE_NAME, "office_id", Office.class);
        }
        List<Staff> staffs = this.selectQuery(queryBuilder, parameterSource);

        JDBCSelectQueryBuilder countQueryBuilder = new JDBCSelectQueryBuilder()
                .count(StaffDaoImpl.PRIMARY_KEY_NAME)
                .from(this.getTableAlias())
                .where(whereClauseBuilder)
                .distinct();
        if(!staffSpecialties.isEmpty()) {
            countQueryBuilder.join("staff_id", this.getSpecialtiesIntermediateTableName(), "staff_id", null);
        }
        if(!localities.isEmpty()){
            countQueryBuilder.join("office_id", OfficeDaoImpl.TABLE_NAME, "office_id", Office.class);
        }

        return new Paginator<>(staffs, page, pageSize, this.selectQueryMetadata(countQueryBuilder, parameterSource).getCount());
    }

    @Override
    public Paginator<Staff> findBy(Collection<String> names, Collection<String> surnames, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties, Collection<Locality> localities, int page, int pageSize) {
        if(page<=0)
            page = 1;

        if(pageSize<=0)
            pageSize = DEFAULT_PAGE_SIZE;

        if (names == null) {
            names = Collections.emptyList();
        } else {
            names = names.stream().map(name->{
                name = name.toLowerCase();
                name = StringUtils.stripAccents(name);
                return name;
            }).collect(Collectors.toList());
        }
        if (surnames == null) {
            surnames = Collections.emptyList();
        } else {
            surnames = surnames.stream().map(surname->{
                surname = surname.toLowerCase();
                surname = StringUtils.stripAccents(surname);
                return surname;
            }).collect(Collectors.toList());
        }
        if (offices == null) {
            offices = Collections.emptyList();
        }
        if (staffSpecialties == null) {
            staffSpecialties = Collections.emptyList();
        }
        if(localities == null){
            localities = Collections.emptyList();
        }

        Map<String, Object> parameters = new HashMap<>();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        JDBCWhereClauseBuilder otherWhereClause = new JDBCWhereClauseBuilder();
        JDBCWhereClauseBuilder namesWhereClause = new JDBCWhereClauseBuilder();

        this.putFirstNamesArguments(names, parameters, namesWhereClause);
        this.putSurnamesArguments(surnames, parameters, namesWhereClause);
        this.putOfficeArguments(offices, parameters, otherWhereClause);
        this.putStaffSpecialtyArguments(staffSpecialties, parameters, otherWhereClause);
        this.putLocalityArguments(localities, parameters, otherWhereClause);

        parameterSource.addValues(parameters);

        JDBCWhereClauseBuilder whereClauseBuilder;
        if(!otherWhereClause.toString().isEmpty()) {
            whereClauseBuilder = otherWhereClause;
            if(!namesWhereClause.toString().isEmpty()){
                whereClauseBuilder.and(namesWhereClause);
            }
        } else {
            whereClauseBuilder = namesWhereClause;
        }

        JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(Staff.class)
                .from(this.getTableAlias())
                .where(whereClauseBuilder)
                .distinct()
                .limit(pageSize)
                .offset((page-1)*pageSize);
        if(!staffSpecialties.isEmpty()) {
            queryBuilder.join("staff_id", this.getSpecialtiesIntermediateTableName(), "staff_id", null);
        }
        if(!localities.isEmpty()){
            queryBuilder.join("office_id", OfficeDaoImpl.TABLE_NAME, "office_id", Office.class);
        }
        List<Staff> staffs = this.selectQuery(queryBuilder, parameterSource);

        JDBCSelectQueryBuilder countQueryBuilder = new JDBCSelectQueryBuilder()
                .count(StaffDaoImpl.PRIMARY_KEY_NAME)
                .from(this.getTableAlias())
                .where(whereClauseBuilder)
                .distinct();
        if(!staffSpecialties.isEmpty()) {
            countQueryBuilder.join("staff_id", this.getSpecialtiesIntermediateTableName(), "staff_id", null);
        }
        if(!localities.isEmpty()){
            countQueryBuilder.join("office_id", OfficeDaoImpl.TABLE_NAME, "office_id", Office.class);
        }

        return new Paginator<>(staffs, page, pageSize, this.selectQueryMetadata(countQueryBuilder, parameterSource).getCount());
    }

    @Override
    public List<Staff> findBy(Collection<String> names, Collection<String> surnames, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties, Collection<Locality> localities) {
        if (names == null) {
            names = Collections.emptyList();
        } else {
            names = names.stream().map(name -> {
                name = name.toLowerCase();
                name = StringUtils.stripAccents(name);
                return name;
            }).collect(Collectors.toList());
        }
        if (surnames == null) {
            surnames = Collections.emptyList();
        } else {
            surnames = surnames.stream().map(surname -> {
                surname = surname.toLowerCase();
                surname = StringUtils.stripAccents(surname);
                return surname;
            }).collect(Collectors.toList());
        }
        if (offices == null) {
            offices = Collections.emptyList();
        }
        if (staffSpecialties == null) {
            staffSpecialties = Collections.emptyList();
        }
        if(localities == null){
            localities = Collections.emptyList();
        }

        Map<String, Object> parameters = new HashMap<>();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        JDBCWhereClauseBuilder otherWhereClause = new JDBCWhereClauseBuilder();
        JDBCWhereClauseBuilder namesWhereClause = new JDBCWhereClauseBuilder();

        this.putFirstNamesArguments(names, parameters, namesWhereClause);
        this.putSurnamesArguments(surnames, parameters, namesWhereClause);
        this.putOfficeArguments(offices, parameters, otherWhereClause);
        this.putStaffSpecialtyArguments(staffSpecialties, parameters, otherWhereClause);
        this.putLocalityArguments(localities, parameters, otherWhereClause);

        parameterSource.addValues(parameters);

        JDBCWhereClauseBuilder whereClauseBuilder;
        if(!otherWhereClause.toString().isEmpty()) {
            whereClauseBuilder = otherWhereClause;
            if(!otherWhereClause.toString().isEmpty()){
                whereClauseBuilder.and(namesWhereClause);
            }
        } else {
            whereClauseBuilder = namesWhereClause;
        }

        JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(Staff.class)
                .from(this.getTableAlias())
                .where(whereClauseBuilder)
                .distinct();

        if(!staffSpecialties.isEmpty()) {
            queryBuilder.join("staff_id", this.getSpecialtiesIntermediateTableName(), "staff_id", null);
        }
        if(!localities.isEmpty()){
            queryBuilder.join("office_id", OfficeDaoImpl.TABLE_NAME, "office_id", Office.class);
        }
        return this.selectQuery(queryBuilder, parameterSource);
    }

    @Override
    public void setOffice(Staff staff, Office office) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("staff", staff.getId());
        parameterSource.addValue("office", office.getId());

        JDBCUpdateQueryBuilder updateQueryBuilder = new JDBCUpdateQueryBuilder()
                .update(this.getTableName(), this.getTableAlias())
                .value("office_id", ":office")
                .where(new JDBCWhereClauseBuilder()
                        .where(PRIMARY_KEY_NAME, Operation.EQ, "staff")
                );
        this.updateQuery(updateQueryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public void addStaffSpecialties(Staff staff, Collection<StaffSpecialty> staffSpecialties) {
        for (StaffSpecialty staffSpecialty : staffSpecialties) {
            this.addStaffSpecialty(staff, staffSpecialty);
        }
    }

    @Override
    public void addStaffSpecialty(Staff staff, StaffSpecialty staffSpecialty) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("staff_id", staff.getId());
        parameterSource.addValue("specialty_id", staffSpecialty.getId());
        JDBCInsertQueryBuilder insertQueryBuilder = new JDBCInsertQueryBuilder()
                .into(this.getTableName())
                .value("staff_id", ":staff_id")
                .value("specialty_id", ":specialty_id")
                .onConflict(OnConflictPolicy.NOTHING);
        this.updateQuery(insertQueryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    protected ResultSetExtractor<List<Staff>> getResultSetExtractor() {
        return resultSet -> {
            Map<Integer, Staff> entitiesMap = new HashMap<>();

            Map<Integer, Office> officeMap = new HashMap<>();
            Map<Integer, Locality> localityMap = new HashMap<>();
            Map<Integer, Province> provinceMap = new HashMap<>();
            Map<String, Country> countryMap = new HashMap<>();
            Map<Integer, User> userMap = new HashMap<>();
            Map<Integer, StaffSpecialty> staffSpecialtyMap = new HashMap<>();

            List<Staff> sortedEntities = new LinkedList<>();
            while (resultSet.next()) {
                int id;
                String idString;
                User user = null;
                Office office = null;
                StaffSpecialty staffSpecialty = null;
                Locality locality = null;
                Province province = null;
                Country country = null;

                try {
                    id = resultSet.getInt(this.formatColumnFromAlias(this.getIdColumnName()));
                } catch (SQLException e) {
                    id = resultSet.getInt(this.getIdColumnName());
                }
                if (resultSet.wasNull())
                    continue;
                Staff staff = entitiesMap.computeIfAbsent(id, integer -> {
                    try {
                        Staff newEntity = ROW_MAPPER.mapRow(this.getTableAlias(), resultSet);
                        sortedEntities.add(newEntity);
                        return newEntity;
                    } catch (SQLException e) {
                        return null;
                    }
                });
                if (staff == null) {
                    continue;
                }

                id = resultSet.getInt(formatColumnFromName(UserDaoImpl.PRIMARY_KEY_NAME, "us"));
                if (!resultSet.wasNull()) {
                    user = userMap.computeIfAbsent(id, integer -> {
                        try {
                            return UserDaoImpl.ROW_MAPPER.mapRow("us", resultSet);
                        } catch (SQLException e) {
                            return null;
                        }
                    });
                }
                id = resultSet.getInt(formatColumnFromName(OfficeDaoImpl.PRIMARY_KEY_NAME, "os"));
                if (!resultSet.wasNull()) {
                    office = officeMap.computeIfAbsent(id, integer -> {
                        try {
                            return OfficeDaoImpl.ROW_MAPPER.mapRow("os", resultSet);
                        } catch (SQLException throwables) {
                            return null;
                        }
                    });
                }
                id = resultSet.getInt(formatColumnFromName(LocalityDaoImpl.PRIMARY_KEY_NAME, "l"));
                if (!resultSet.wasNull()) {
                    locality = localityMap.computeIfAbsent(id, integer -> {
                        try {
                            return LocalityDaoImpl.ROW_MAPPER.mapRow("l", resultSet);
                        } catch (SQLException throwables) {
                            return null;
                        }
                    });
                }
                id = resultSet.getInt(formatColumnFromName(ProvinceDaoImpl.PRIMARY_KEY_NAME, "ps"));
                if (!resultSet.wasNull()) {
                    province = provinceMap.computeIfAbsent(id, integer -> {
                        try {
                            return ProvinceDaoImpl.ROW_MAPPER.mapRow("ps", resultSet);
                        } catch (SQLException throwables) {
                            return null;
                        }
                    });
                }
                idString = resultSet.getString(formatColumnFromName(CountryDaoImpl.PRIMARY_KEY_NAME, "cs"));
                if (!resultSet.wasNull()) {
                    country = countryMap.computeIfAbsent(idString, integer -> {
                        try {
                            return CountryDaoImpl.ROW_MAPPER.mapRow("cs", resultSet);
                        } catch (SQLException throwables) {
                            return null;
                        }
                    });
                }
                id = resultSet.getInt(formatColumnFromName(StaffSpecialtyDaoImpl.PRIMARY_KEY_NAME, "ss"));
                if (!resultSet.wasNull()) {
                    staffSpecialty = staffSpecialtyMap.computeIfAbsent(id, integer -> {
                        try {
                            return StaffSpecialtyDaoImpl.ROW_MAPPER.mapRow("ss", resultSet);
                        } catch (SQLException throwables) {
                            return null;
                        }
                    });
                }

                if (office != null) {
                    staff.setOffice(office);
                    office.setLocality(locality);
                }
                if (user != null)
                    staff.setUser(user);
                if (staffSpecialty != null && !staff.getStaffSpecialties().contains(staffSpecialty))
                    staff.getStaffSpecialties().add(staffSpecialty);
                if (locality != null && province != null) {
                    locality.setProvince(province);
                }
                if (province != null && country != null) {
                    province.setCountry(country);
                }
            }
            return sortedEntities;
        };
    }

    @Override
    protected void populateJoins(JDBCSelectQueryBuilder selectQueryBuilder) {
        selectQueryBuilder
                .joinAlias("user_id", UserDaoImpl.TABLE_NAME, "us", UserDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, User.class)
                .joinAlias("office_id", OfficeDaoImpl.TABLE_NAME, "os", OfficeDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Office.class)
                .joinAlias("os", "locality_id", LocalityDaoImpl.TABLE_NAME, "l", LocalityDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Locality.class)
                .joinAlias("l", "province_id", ProvinceDaoImpl.TABLE_NAME, "ps", ProvinceDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Province.class)
                .joinAlias("ps", "country_id", CountryDaoImpl.TABLE_NAME, "cs", CountryDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, Country.class)
                .joinAlias("staff_id", "system_staff_specialty_staff", "sss", "staff_id", JoinType.LEFT, null)
                .joinAlias("sss", "specialty_id", StaffSpecialtyDaoImpl.TABLE_NAME, "ss", StaffSpecialtyDaoImpl.PRIMARY_KEY_NAME, JoinType.LEFT, StaffSpecialty.class);
    }

    @Override
    protected Map<String, JDBCArgumentValue> getModelRelationsArgumentValue(Staff model, String prefix) {
        Map<String, JDBCArgumentValue> map = new HashMap<>();
        map.put("office_id", new JDBCArgumentValue(prefix + "office_id", model.getOffice() != null ? model.getOffice().getId() : null));
        map.put("user_id", new JDBCArgumentValue(prefix + "user_id", model.getUser() != null ? model.getUser().getId() : null));
        return map;
    }

    private void putLocalityArguments(Collection<Locality> localities, Map<String, Object> argumentsValues, JDBCWhereClauseBuilder whereClauseBuilder) {
        if (localities.isEmpty())
            return;

        Collection<String> arguments = new HashSet<>();
        int i = 0;
        for (Locality locality : localities) {
            String parameter = "_locality_" + i;
            argumentsValues.put(parameter, locality.getId());
            arguments.add(":" + parameter);
            i++;
        }
        whereClauseBuilder
                .and()
                .in(
                        formatColumnFromName("locality_id", OfficeDaoImpl.TABLE_NAME),
                        arguments
                );
    }

    private void putStaffSpecialtyArguments(Collection<StaffSpecialty> staffSpecialties, Map<String, Object> argumentsValues, JDBCWhereClauseBuilder whereClauseBuilder) {
        if (staffSpecialties.isEmpty())
            return;

        Collection<String> arguments = new HashSet<>();
        int i = 0;
        for (StaffSpecialty staffSpecialty : staffSpecialties) {
            String parameter = "_specialty_" + i;
            argumentsValues.put(parameter, staffSpecialty.getId());
            arguments.add(":" + parameter);
            i++;
        }
        whereClauseBuilder
                .and()
                .in(
                        formatColumnFromName("specialty_id", this.getSpecialtiesIntermediateTableName()),
                        arguments
                );
    }

    private void putOfficeArguments(Collection<Office> offices, Map<String, Object> argumentsValues, JDBCWhereClauseBuilder whereClauseBuilder) {
        if (offices.isEmpty())
            return;

        Collection<String> arguments = new HashSet<>();
        int i = 0;
        for (Office office : offices) {
            String parameter = "_office_" + i;
            argumentsValues.put(parameter, office.getId());
            arguments.add(":" + parameter);
            i++;
        }
        whereClauseBuilder
                .and()
                .in(
                        formatColumnFromName("office_id", this.getTableAlias()),
                        arguments
                );
    }

    private void putFirstNamesArguments(Collection<String> names, Map<String, Object> argumentsValues, JDBCWhereClauseBuilder whereClauseBuilder) {
        if (names.isEmpty())
            return;

        int i = 0;
        for (String name : names) {
            if (!name.isEmpty()) {
                String parameter = "_firstName_" + i;
                argumentsValues.put(parameter, StringSearchType.CONTAINS_NO_ACC.transform(name));

                whereClauseBuilder
                        .or()
                        .where(this.formatColumnFromName("first_name"), Operation.LIKE, ":_firstName_" + i, ColumnTransformer.LOWER);
                i++;
            }
        }
    }

    private void putSurnamesArguments(Collection<String> surnames, Map<String, Object> argumentsValues, JDBCWhereClauseBuilder whereClauseBuilder) {
        if (surnames.isEmpty())
            return;

        int i = 0;
        for (String surname : surnames) {
            if (!surname.isEmpty()) {
                String parameter = "_surname_" + i;
                argumentsValues.put(parameter, StringSearchType.CONTAINS_NO_ACC.transform(surname));

                whereClauseBuilder
                        .or()
                        .where(this.formatColumnFromName("surname"), Operation.LIKE, ":_surname_" + i, ColumnTransformer.LOWER);
                i++;
            }
        }
    }

    private void putFirstNameArguments(String firstName, Map<String, Object> argumentsValues, JDBCWhereClauseBuilder whereClauseBuilder) {
        if (firstName.isEmpty())
            return;

        argumentsValues.put("firstName", StringSearchType.CONTAINS_NO_ACC.transform(firstName));
        whereClauseBuilder
                .and()
                .where(this.formatColumnFromName("first_name"), Operation.LIKE, ":firstName", ColumnTransformer.LOWER);
    }

    private void putSurnameArgument(String surname, Map<String, Object> argumentsValues, JDBCWhereClauseBuilder whereClauseBuilder) {
        if (surname.isEmpty())
            return;

        argumentsValues.put("surname", StringSearchType.CONTAINS_NO_ACC.transform(surname));
        whereClauseBuilder
                .and()
                .where(this.formatColumnFromName("surname"), Operation.LIKE, ":surname", ColumnTransformer.LOWER);
    }

    private String getSpecialtiesIntermediateTableName() {
        return "system_staff_specialty_staff";
    }
}