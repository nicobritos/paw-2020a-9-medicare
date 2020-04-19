package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.StaffDao;
import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import ar.edu.itba.paw.persistence.utils.StringSearchType;
import ar.edu.itba.paw.persistence.utils.builder.JDBCQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCSelectQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.ColumnTransformer;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.Operation;
import ar.edu.itba.paw.persistence.utils.cache.CacheHelper;
import ar.edu.itba.paw.persistence.utils.cache.FilteredCachedCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;
import java.util.function.Predicate;

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
    public Set<Staff> findBy(String name, String surname, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties, Collection<Locality> localities) {
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
        if (staffSpecialties.isEmpty() && name.isEmpty() && surname.isEmpty() && offices.isEmpty() && localities.isEmpty())
            return this.list();

        FilteredCachedCollection<Staff> cachedCollection = this.filterCache(name, surname, staffSpecialties, offices, localities);
        if (this.isCacheComplete(cachedCollection)) {
            return cachedCollection.getCollectionAsSet();
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

        if (!cachedCollection.getCollection().isEmpty()) {
            this.excludeModels(cachedCollection.getCompleteCollection(), parameterSource, whereClauseBuilder);
        }

        JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .where(whereClauseBuilder)
                .distinct();
        if(!staffSpecialties.isEmpty()) {
            queryBuilder.join("staff_id", this.getSpecialtiesIntermediateTableName(), "staff_id");
        }
        if(!localities.isEmpty()){
            queryBuilder.join("office_id", this.getOfficeTable(), "office_id");
        }
        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    protected RowMapper<Staff> getRowMapper() {
        return this.rowMapper;
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
                        formatColumnFromName("locality_id", this.getOfficeTable()),
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

    private void putFirstNameArguments(String firstName, Map<String, Object> argumentsValues, JDBCWhereClauseBuilder whereClauseBuilder) {
        if (firstName.isEmpty())
            return;

        argumentsValues.put("firstName", StringSearchType.CONTAINS.transform(firstName));
        whereClauseBuilder
                .and()
                .where(this.formatColumnFromName("first_name"), Operation.LIKE, ":firstName", ColumnTransformer.LOWER);
    }

    private void putSurnameArgument(String surname, Map<String, Object> argumentsValues, JDBCWhereClauseBuilder whereClauseBuilder) {
        if (surname.isEmpty())
            return;

        argumentsValues.put("surname", StringSearchType.CONTAINS.transform(surname));
        whereClauseBuilder
                .and()
                .where(this.formatColumnFromName("surname"), Operation.LIKE, ":surname", ColumnTransformer.LOWER);
    }

    private FilteredCachedCollection<Staff> filterCache(String name, String surname, Collection<StaffSpecialty> staffSpecialties, Collection<Office> offices, Collection<Locality> localities) {
        Predicate<Staff> p = staff -> true; // sirve como default porque son todos ANDs (true && other = other)
        if (!name.isEmpty()) {
            p = p.and(staff -> staff.getFirstName().toLowerCase().contains(name));
        }
        if (!surname.isEmpty()) {
            p = p.and(staff -> staff.getSurname().toLowerCase().contains(surname));
        }
        if (!staffSpecialties.isEmpty()) {
            p = p.and(staff -> staff.getStaffSpecialties().containsAll(staffSpecialties));
        }
        if (!offices.isEmpty()) {
            p = p.and(staff -> offices.contains(staff.getOffice()));
        }
        if (!localities.isEmpty()) {
            p = p.and(staff -> localities.contains(staff.getOffice().getLocality()));
        }
        // else p = staff -> true, quiero que no me filtre nada si todos son empty
        return CacheHelper.filter(Staff.class, Integer.class, p);
    }

    private String getSpecialtiesIntermediateTableName() {
        return "system_staff_specialty_staff";
    }

    private String getOfficeTable() {
        return "office";
    }
}