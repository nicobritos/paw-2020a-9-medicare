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
import ar.edu.itba.paw.persistence.utils.cache.CacheHelper;
import ar.edu.itba.paw.persistence.utils.cache.FilteredCachedCollection;
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

        FilteredCachedCollection<Staff> cachedCollection = this.filterCollection("", "", staffSpecialties, new LinkedList<>());
        if (this.isCacheComplete(cachedCollection)) {
            return cachedCollection.getCollectionAsSet();
        }

        Map<String, Object> parameters = new HashMap<>();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder();
        this.putStaffSpecialtyArguments(staffSpecialties, parameters, whereClauseBuilder);
        parameterSource.addValues(parameters);

        if (!cachedCollection.getCollection().isEmpty()) {
            this.excludeModels(cachedCollection.getCompleteCollection(), parameterSource, whereClauseBuilder);
        }
        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .join("staff_id", this.getSpecialtiesIntermediateTableName(), "staff_id")
                .where(whereClauseBuilder)
                .distinct();

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public Set<Staff> findByNameAndSurname(String name, String surname) {
        if (name.isEmpty() && surname.isEmpty())
            return new HashSet<>();

        name = name.toLowerCase();
        surname = surname.toLowerCase();
        FilteredCachedCollection<Staff> cachedCollection = this.filterCollection(name, surname, new LinkedList<>(), new LinkedList<>());
        if (this.isCacheComplete(cachedCollection)) {
            return cachedCollection.getCollectionAsSet();
        }

        name = name.toLowerCase();
        surname = surname.toLowerCase();
        Map<String, Object> parameters = new HashMap<>();
        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder();

        this.putFirstNameArguments(name, parameters, whereClauseBuilder);
        this.putSurnameArgument(surname, parameters, whereClauseBuilder);

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(parameters);

        if (!cachedCollection.getCollection().isEmpty()) {
            this.excludeModels(cachedCollection.getCompleteCollection(), parameterSource, whereClauseBuilder);
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .where(whereClauseBuilder)
                .distinct();

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public Set<Staff> findByNameAndStaffSpecialties(String name, Collection<StaffSpecialty> staffSpecialties) {
        if (staffSpecialties.isEmpty() && name.isEmpty())
            return new HashSet<>();

        name = name.toLowerCase();
        FilteredCachedCollection<Staff> cachedCollection = this.filterCollection(name, "", staffSpecialties, new LinkedList<>());
        if (this.isCacheComplete(cachedCollection)) {
            return cachedCollection.getCollectionAsSet();
        }

        Map<String, Object> parameters = new HashMap<>();
        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder();

        this.putFirstNameArguments(name, parameters, whereClauseBuilder);
        this.putStaffSpecialtyArguments(staffSpecialties, parameters, whereClauseBuilder);

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(parameters);

        if (!cachedCollection.getCollection().isEmpty()) {
            this.excludeModels(cachedCollection.getCompleteCollection(), parameterSource, whereClauseBuilder);
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .join("staff_id", this.getSpecialtiesIntermediateTableName(), "staff_id")
                .where(whereClauseBuilder)
                .distinct();

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public Set<Staff> findBySurnameAndOffice(String surname, Collection<Office> offices) {
        if (offices.isEmpty() && surname.isEmpty())
            return new HashSet<>();

        surname = surname.toLowerCase();
        FilteredCachedCollection<Staff> cachedCollection = this.filterCollection("", surname, new LinkedList<>(), offices);
        if (this.isCacheComplete(cachedCollection)) {
            return cachedCollection.getCollectionAsSet();
        }

        Map<String, Object> parameters = new HashMap<>();
        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder();
        this.putSurnameArgument(surname, parameters, whereClauseBuilder);
        this.putOfficeArguments(offices, parameters, whereClauseBuilder);

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(parameters);

        if (!cachedCollection.getCollection().isEmpty()) {
            this.excludeModels(cachedCollection.getCompleteCollection(), parameterSource, whereClauseBuilder);
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .where(whereClauseBuilder)
                .distinct();

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public Set<Staff> findBySurnameAndStaffSpecialties(String surname, Collection<StaffSpecialty> staffSpecialties) {
        if (staffSpecialties.isEmpty() && surname.isEmpty())
            return new HashSet<>();

        surname = surname.toLowerCase();
        FilteredCachedCollection<Staff> cachedCollection = this.filterCollection("", surname, staffSpecialties, new LinkedList<>());
        if (this.isCacheComplete(cachedCollection)) {
            return cachedCollection.getCollectionAsSet();
        }

        Map<String, Object> parameters = new HashMap<>();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder();

        this.putSurnameArgument(surname, parameters, whereClauseBuilder);
        this.putStaffSpecialtyArguments(staffSpecialties, parameters, whereClauseBuilder);

        parameterSource.addValues(parameters);

        if (!cachedCollection.getCollection().isEmpty()) {
            this.excludeModels(cachedCollection.getCompleteCollection(), parameterSource, whereClauseBuilder);
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .join("staff_id", this.getSpecialtiesIntermediateTableName(), "staff_id")
                .where(whereClauseBuilder)
                .distinct();

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public Set<Staff> findByNameOfficeAndStaffSpecialties(String name, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties) {
        return this.findByNameSurnameOfficeAndStaffSpecialities(name, "", offices, staffSpecialties);
    }

    @Override
    public Set<Staff> findByNameSurnameAndStaffSpecialties(String name, String surname, Collection<StaffSpecialty> staffSpecialties) {
        if (staffSpecialties.isEmpty() && name.isEmpty())
            return new HashSet<>();

        name = name.toLowerCase();
        surname = surname.toLowerCase();
        FilteredCachedCollection<Staff> cachedCollection = this.filterCollection(name, surname, staffSpecialties, new LinkedList<>());
        if (this.isCacheComplete(cachedCollection)) {
            return cachedCollection.getCollectionAsSet();
        }

        Map<String, Object> parameters = new HashMap<>();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder();

        this.putFirstNameArguments(name, parameters, whereClauseBuilder);
        this.putSurnameArgument(surname, parameters, whereClauseBuilder);
        this.putStaffSpecialtyArguments(staffSpecialties, parameters, whereClauseBuilder);

        parameterSource.addValues(parameters);

        if (!cachedCollection.getCollection().isEmpty()) {
            this.excludeModels(cachedCollection.getCompleteCollection(), parameterSource, whereClauseBuilder);
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .join("staff_id", this.getSpecialtiesIntermediateTableName(), "staff_id")
                .where(whereClauseBuilder)
                .distinct();

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public Set<Staff> findByNameSurnameAndOffice(String name, String surname, Collection<Office> offices) {
        if (offices.isEmpty() && name.isEmpty())
            return new HashSet<>();

        name = name.toLowerCase();
        surname = surname.toLowerCase();
        FilteredCachedCollection<Staff> cachedCollection = this.filterCollection(name, surname, new LinkedList<>(), offices);
        if (this.isCacheComplete(cachedCollection)) {
            return cachedCollection.getCollectionAsSet();
        }

        Map<String, Object> parameters = new HashMap<>();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder();

        this.putFirstNameArguments(name, parameters, whereClauseBuilder);
        this.putSurnameArgument(surname, parameters, whereClauseBuilder);
        this.putOfficeArguments(offices, parameters, whereClauseBuilder);

        parameterSource.addValues(parameters);

        if (!cachedCollection.getCollection().isEmpty()) {
            this.excludeModels(cachedCollection.getCompleteCollection(), parameterSource, whereClauseBuilder);
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .where(whereClauseBuilder)
                .distinct();


        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public Set<Staff> findByNameSurnameOfficeAndStaffSpecialities(String name, String surname, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties) {
        if (staffSpecialties.isEmpty() && offices.isEmpty() && name.isEmpty() && surname.isEmpty())
            return new HashSet<>();

        name = name.toLowerCase();
        surname = surname.toLowerCase();
        FilteredCachedCollection<Staff> cachedCollection = this.filterCollection(name, surname, staffSpecialties, offices);
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

        parameterSource.addValues(parameters);

        if (!cachedCollection.getCollection().isEmpty()) {
            this.excludeModels(cachedCollection.getCompleteCollection(), parameterSource, whereClauseBuilder);
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .join("staff_id", this.getSpecialtiesIntermediateTableName(), "staff_id")
                .where(whereClauseBuilder)
                .distinct();

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public Set<Staff> findByOfficeAndStaffSpecialties(Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties) {
        return this.findByNameOfficeAndStaffSpecialties("", offices, staffSpecialties);
    }

    @Override
    public Set<Staff> findBySurnameOfficeAndStaffSpecialties(String surname, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties) {
        return this.findByNameSurnameOfficeAndStaffSpecialities("", surname, offices, staffSpecialties);
    }

    @Override
    public Set<Staff> findByNameAndOffice(String name, Collection<Office> offices) {
        if (offices.isEmpty() && name.isEmpty())
            return new HashSet<>();

        name = name.toLowerCase();
        FilteredCachedCollection<Staff> cachedCollection = this.filterCollection(name, "", new LinkedList<>(), offices);
        if (this.isCacheComplete(cachedCollection)) {
            return cachedCollection.getCollectionAsSet();
        }

        Map<String, Object> parameters = new HashMap<>();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder();

        this.putFirstNameArguments(name, parameters, whereClauseBuilder);
        this.putOfficeArguments(offices, parameters, whereClauseBuilder);

        parameterSource.addValues(parameters);

        if (!cachedCollection.getCollection().isEmpty()) {
            this.excludeModels(cachedCollection.getCompleteCollection(), parameterSource, whereClauseBuilder);
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .where(whereClauseBuilder)
                .distinct();

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

        argumentsValues.put("firstName", firstName);
        whereClauseBuilder
                .and()
                .where(this.formatColumnFromName("first_name"), Operation.LIKE, ":firstName", ColumnTransformer.LOWER);
    }

    private void putSurnameArgument(String surname, Map<String, Object> argumentsValues, JDBCWhereClauseBuilder whereClauseBuilder) {
        if (surname.isEmpty())
            return;

        argumentsValues.put("surname", surname);
        whereClauseBuilder
                .and()
                .where(this.formatColumnFromName("surname"), Operation.LIKE, ":surname", ColumnTransformer.LOWER);
    }

    private FilteredCachedCollection<Staff> filterCollection(String name, String surname, Collection<StaffSpecialty> staffSpecialties, Collection<Office> offices) {
        if (!name.isEmpty()) {
            if (!surname.isEmpty()) {
                if (!staffSpecialties.isEmpty()) {
                    if (!offices.isEmpty()) {
                        return CacheHelper.filter(
                                Staff.class,
                                Integer.class,
                                staff -> staff.getFirstName().toLowerCase().contains(name) &&
                                        staff.getSurname().toLowerCase().contains(surname) &&
                                        offices.contains(staff.getOffice()) &&
                                        staff.getStaffSpecialties().containsAll(staffSpecialties)
                        );
                    } else {
                        return CacheHelper.filter(
                                Staff.class,
                                Integer.class,
                                staff -> staff.getFirstName().toLowerCase().contains(name) &&
                                        staff.getSurname().toLowerCase().contains(surname) &&
                                        staff.getStaffSpecialties().containsAll(staffSpecialties)
                        );
                    }
                } else {
                    if (!offices.isEmpty()) {
                        return CacheHelper.filter(
                                Staff.class,
                                Integer.class,
                                staff -> staff.getFirstName().toLowerCase().contains(name) &&
                                        staff.getSurname().toLowerCase().contains(surname) &&
                                        offices.contains(staff.getOffice())
                        );
                    } else {
                        return CacheHelper.filter(
                                Staff.class,
                                Integer.class,
                                staff -> staff.getFirstName().toLowerCase().contains(name) &&
                                        staff.getSurname().toLowerCase().contains(surname)
                        );
                    }
                }
            } else {
                if (!staffSpecialties.isEmpty()) {
                    if (!offices.isEmpty()) {
                        return CacheHelper.filter(
                                Staff.class,
                                Integer.class,
                                staff -> staff.getFirstName().toLowerCase().contains(name) &&
                                        offices.contains(staff.getOffice()) &&
                                        staff.getStaffSpecialties().containsAll(staffSpecialties)
                        );
                    } else {
                        return CacheHelper.filter(
                                Staff.class,
                                Integer.class,
                                staff -> staff.getFirstName().toLowerCase().contains(name) &&
                                        staff.getStaffSpecialties().containsAll(staffSpecialties)
                        );
                    }
                } else {
                    if (!offices.isEmpty()) {
                        return CacheHelper.filter(
                                Staff.class,
                                Integer.class,
                                staff -> staff.getFirstName().toLowerCase().contains(name) &&
                                        offices.contains(staff.getOffice())
                        );
                    } else {
                        return CacheHelper.filter(
                                Staff.class,
                                Integer.class,
                                staff -> staff.getFirstName().toLowerCase().contains(name)
                        );
                    }
                }
            }
        } else {
            if (!staffSpecialties.isEmpty()) {
                if (!offices.isEmpty()) {
                    return CacheHelper.filter(
                            Staff.class,
                            Integer.class,
                            staff -> offices.contains(staff.getOffice()) &&
                                    staff.getStaffSpecialties().containsAll(staffSpecialties)
                    );
                } else {
                    return CacheHelper.filter(
                            Staff.class,
                            Integer.class,
                            staff -> staff.getStaffSpecialties().containsAll(staffSpecialties)
                    );
                }
            } else {
                if (!offices.isEmpty()) {
                    return CacheHelper.filter(
                            Staff.class,
                            Integer.class,
                            staff -> offices.contains(staff.getOffice())
                    );
                } else {
                    return CacheHelper.filter(
                            Staff.class,
                            Integer.class,
                            staff -> true
                    );
                }
            }
        }
    }

    private String getSpecialtiesIntermediateTableName() {
        return "system_staff_specialty_staff";
    }
}
