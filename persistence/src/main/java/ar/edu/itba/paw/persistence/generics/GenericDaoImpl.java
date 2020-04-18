package ar.edu.itba.paw.persistence.generics;

import ar.edu.itba.paw.interfaces.daos.generic.GenericDao;
import ar.edu.itba.paw.models.GenericModel;
import ar.edu.itba.paw.persistence.utils.Pair;
import ar.edu.itba.paw.persistence.utils.ReflectionGetterSetter;
import ar.edu.itba.paw.persistence.utils.StringSearchType;
import ar.edu.itba.paw.persistence.utils.builder.*;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.ColumnTransformer;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.Operation;
import ar.edu.itba.paw.persistence.utils.cache.CacheHelper;
import ar.edu.itba.paw.persistence.utils.cache.CachedCollection;
import ar.edu.itba.paw.persistence.utils.cache.CachedCollectionStatus;
import ar.edu.itba.paw.persistence.utils.cache.FilteredCachedCollection;
import ar.edu.itba.paw.persistence.utils.proxy.NotManagedByDAOException;
import ar.edu.itba.paw.persistence.utils.proxy.ProxyHelper;
import ar.edu.itba.paw.persistenceAnnotations.*;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

/**
 * This provides a generic DAO implementation with lots of useful methods
 * @param <M> the DAO model type
 * @param <I> the Model's id type
 */
public abstract class GenericDaoImpl<M extends GenericModel<M, I>, I> implements GenericDao<M, I> {
    private static final String ARGUMENT_PREFIX = "_r_";
    protected TransactionTemplate transactionTemplate;
    protected NamedParameterJdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private boolean customPrimaryKey;
    private boolean haveBeenListed;
    private String primaryKeyName;
    private final Class<M> mClass;
    private final Class<I> iClass;
    private String tableName;

    public GenericDaoImpl(DataSource dataSource, Class<M> mClass, Class<I> iClass) {
        this.transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.mClass = mClass;
        this.iClass = iClass;

        // Get the table's name and primary key's column's name from the annotation that is
        // (or should be) set in the model's class
        if (mClass.isAnnotationPresent(Table.class)) {
            Table table = mClass.getAnnotation(Table.class);
            this.tableName = table.name();
            this.primaryKeyName = table.primaryKey();
            this.customPrimaryKey = table.customPrimaryKey();
            this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(this.tableName);
            if (!this.customPrimaryKey)
                this.jdbcInsert.usingGeneratedKeyColumns(this.primaryKeyName);
        }
    }

    @Override
    public Optional<M> findById(I id) {
        M model = this.getFromCache(id);
        if (model != null) {
            return Optional.of(model);
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .where(new JDBCWhereClauseBuilder()
                        .where(this.formatColumnFromAlias(this.getIdColumnName()), Operation.EQ, ":id")
                );

        MapSqlParameterSource args = new MapSqlParameterSource();
        // Note that "parameterName" should NOT be preceded by a semicolon (as it is in the query)
        args.addValue("id", id);

        Optional<M> mOptional = this.selectQuerySingle(queryBuilder.getQueryAsString(), args);
        mOptional.ifPresent(this::setToCache);
        return mOptional;
    }

    @Override
    public Set<M> findByIds(Collection<I> ids) {
        if (ids.isEmpty())
            return new HashSet<>();

        Map<String, Object> parameters = new HashMap<>();
        Collection<String> idsParameters = new LinkedList<>();
        Set<M> foundModels = new HashSet<>();

        int i = 0;
        for (I id : ids) {
            M foundModel = this.getFromCache(id);
            if (foundModel != null) {
                foundModels.add(foundModel);
            } else {
                String parameter = "_ids_" + i;
                parameters.put(parameter, id);
                idsParameters.add(":" + parameter);
                i++;
            }
        }
        if (foundModels.size() == ids.size()) {
            return foundModels;
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .where(new JDBCWhereClauseBuilder()
                        .in(this.formatColumnFromAlias(this.getIdColumnName()), idsParameters)
                );

        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValues(parameters);

        Collection<M> queriedModels = this.selectQuery(queryBuilder.getQueryAsString(), args);
        if (!queriedModels.isEmpty()) {
            this.setToCache(queriedModels);
            foundModels.addAll(queriedModels);
        }
        return foundModels;
    }

    @Override
    public synchronized M create(M model) {
        Map<String, Pair<String, Object>> columnsArgumentValue = this.getModelColumnsArgumentValue(model, "", true);
        if (this.customPrimaryKey) {
            columnsArgumentValue.put(this.getIdColumnName(), new Pair<>(":" + this.getIdColumnName(), model.getId()));
        }

        Map<String, Object> argumentsValues = new HashMap<>();
        for (Entry<String, Pair<String, Object>> columnArgumentValue : columnsArgumentValue.entrySet()) {
            argumentsValues.put(columnArgumentValue.getKey(), columnArgumentValue.getValue().getRight());
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(argumentsValues);

        return this.transactionTemplate.execute(transactionStatus -> {
            M newModel = this.insertQuery(model, parameterSource);
            this.saveRelations(newModel);

            newModel = this.findById(newModel.getId()).get();
            this.setToCache(newModel);
            return newModel;
        });
    }

    /**
     * Updates ALL the information inside the model (with the exception of the ID) including relations
     * @param model the model
     */
    @Override
    public synchronized void update(M model) {
        this.assertModelUpdate(model);

        Map<String, Pair<String, Object>> columnsArgumentValue = this.getModelColumnsArgumentValue(model, ARGUMENT_PREFIX, true);

        Map<String, String> columnsArguments = new HashMap<>();
        Map<String, Object> argumentsValues = new HashMap<>();
        for (Entry<String, Pair<String, Object>> columnArgumentValue : columnsArgumentValue.entrySet()) {
            columnsArguments.put(columnArgumentValue.getKey(), columnArgumentValue.getValue().getLeft());
            argumentsValues.put(ARGUMENT_PREFIX + columnArgumentValue.getKey(), columnArgumentValue.getValue().getRight());
        }

        String argumentName = ARGUMENT_PREFIX + "id";
        JDBCQueryBuilder queryBuilder = new JDBCUpdateQueryBuilder()
                .update(this.getTableAlias())
                .values(columnsArguments)
                .where(new JDBCWhereClauseBuilder()
                        .where(formatColumnFromName(this.getIdColumnName(), this.getTableAlias()), Operation.EQ, ":" + argumentName)
                );

        argumentsValues.put(argumentName, model.getId());

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(argumentsValues);

        this.transactionTemplate.execute(transactionStatus -> {
            this.updateQuery(queryBuilder.getQueryAsString(), parameterSource);
            this.saveRelations(model);
            this.setToCache(model);
            return null;
        });
    }

    @Override
    public synchronized void remove(M model) {
        this.remove(model.getId());
    }

    @Override
    public synchronized void remove(I id) {
        JDBCQueryBuilder queryBuilder = new JDBCDeleteQueryBuilder()
                .from(this.getTableAlias())
                .where(new JDBCWhereClauseBuilder()
                        .where(this.formatColumnFromAlias(this.getIdColumnName()), Operation.EQ, ":id")
                );

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);

        this.updateQuery(queryBuilder.getQueryAsString(), parameterSource);
        this.removeFromCache(id);
    }

    @Override
    public Set<M> list() {
        CachedCollection<M> cachedModels = this.listFromCache();
        if (this.isCacheComplete(cachedModels)) {
            return cachedModels.getCollectionAsSet();
        }
        Set<M> previousModels = cachedModels.getCollectionAsSet();

        JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias());

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        if (!previousModels.isEmpty()) {
            JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder();
            this.excludeModels(previousModels, parameterSource, whereClauseBuilder);
            queryBuilder.where(whereClauseBuilder);
        }

        Set<M> newModels = this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
        this.setToCache(newModels);
        this.haveBeenListed = true;
        newModels.addAll(previousModels);
        return newModels;
    }

    /**
     * Searches for a collection of models that have a columnName equals to the provided object's value
     * @param columnName the db column name
     * @param value the column's value. If it extends GenericModel then its ID will be used
     * @return a collection of models found
     */
    @Override
    public Set<M> findByField(String columnName, Object value) {
        return this.findByField(columnName, Operation.EQ, value);
    }

    /**
     * Searches for a collection of models that have a columnName equals to the provided string (case insensitive)
     * @param columnName the db column name
     * @param value the column's value
     * @return a collection of models found
     */
    @Override
    public Set<M> findByFieldIgnoreCase(String columnName, String value) {
        return this.findByFieldIgnoreCase(columnName, Operation.EQ, value, StringSearchType.EQUALS);
    }

    @Override
    public Class<M> getModelClass() {
        return this.mClass;
    }

    /**
     * TODO
     * @param columnName
     * @param operation
     * @param value if value is a generic model then it will use its id
     * @return
     */
    public Set<M> findByField(String columnName, Operation operation, Object value) {
        FilteredCachedCollection<M> cachedModels = this.filterFromCache(columnName, operation, value);
        if (this.isCacheComplete(cachedModels)) {
            return cachedModels.getCollectionAsSet();
        }
        Set<M> models = cachedModels.getCollectionAsSet();
        Collection<M> allCachedModels = cachedModels.getCompleteCollection();

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromAlias(columnName), operation, ":argument");

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        if (value instanceof GenericModel) {
            parameterSource.addValue("argument", ((GenericModel) value).getId());
        } else {
            parameterSource.addValue("argument", value);
        }
        if (!allCachedModels.isEmpty()) {
            this.excludeModels(allCachedModels, parameterSource, whereClauseBuilder);
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .where(whereClauseBuilder);

        Set<M> newModels = this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
        newModels.addAll(models);
        return newModels;
    }

    protected Set<M> findByFieldIgnoreCase(String columnName, Operation operation, String value) {
        return this.findByFieldIgnoreCase(columnName, operation, value, StringSearchType.CONTAINS);
    }

    protected Set<M> findByFieldIgnoreCase(String columnName, Operation operation, String value, StringSearchType stringSearchType) {
        FilteredCachedCollection<M> cachedModels = this.filterFromCacheIgnoreCase(columnName, value, stringSearchType);
        if (this.isCacheComplete(cachedModels)) {
            return cachedModels.getCollectionAsSet();
        }
        Set<M> models = cachedModels.getCollectionAsSet();
        Collection<M> allCachedModels = cachedModels.getCompleteCollection();

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromAlias(columnName), operation, ":argument", ColumnTransformer.LOWER);

        value = value.toLowerCase();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("argument", stringSearchType.transform(value));
        if (!allCachedModels.isEmpty()) {
            this.excludeModels(allCachedModels, parameterSource, whereClauseBuilder);
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .where(whereClauseBuilder);

        Set<M> newModels = this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
        newModels.addAll(models);
        return newModels;
    }

    protected Set<M> findByFieldIn(String columnName, Collection<?> values) {
        if (values.isEmpty())
            return new HashSet<>();

        FilteredCachedCollection<M> cachedModels = this.filterFromCacheIn(columnName, values);
        if (this.isCacheComplete(cachedModels)) {
            return cachedModels.getCollectionAsSet();
        }
        Set<M> models = cachedModels.getCollectionAsSet();
        Collection<M> allCachedModels = cachedModels.getCompleteCollection();

        Map<String, Object> parameters = new HashMap<>();
        int i = 0;
        for (Object value : values) {
            if (value instanceof GenericModel) {
                parameters.put("_" + i, ((GenericModel) value).getId());
            } else {
                parameters.put("_" + i, value);
            }
            i++;
        }

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .in(this.formatColumnFromAlias(columnName), parameters.keySet());

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(parameters);
        if (!allCachedModels.isEmpty()) {
            this.excludeModels(allCachedModels, parameterSource, whereClauseBuilder);
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .where(whereClauseBuilder);

        Set<M> newModels = this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
        newModels.addAll(models);
        return newModels;
    }

    protected Set<M> selectQuery(String query) {
        return new HashSet<>(this.jdbcTemplate.query(query, this.getRowMapper()));
    }

    protected Set<M> selectQuery(String query, MapSqlParameterSource args) {
        return new HashSet<>(this.jdbcTemplate.query(query, args, this.getRowMapper()));
    }

    /**
     * Runs a query handled by a specific handler
     * @param query the query
     * @param args the arguments (can be empty)
     * @param callbackHandler the ResultSet handler
     */
    protected void selectQuery(String query, MapSqlParameterSource args, RowCallbackHandler callbackHandler) {
        this.jdbcTemplate.query(query, args, callbackHandler);
    }

    /**
     * Runs an Insert/Update/Delete query
     * @param query the query
     * @param args the arguments
     */
    protected void updateQuery(String query, MapSqlParameterSource args) {
        this.jdbcTemplate.update(query, args);
    }

    protected Optional<M> selectQuerySingle(String query) {
        List<M> list = this.jdbcTemplate.query(query, this.getRowMapper());
        return list.size() > 0 ? Optional.of(list.get(0)) : Optional.empty();
    }

    protected Optional<M> selectQuerySingle(String query, MapSqlParameterSource args) {
        List<M> list = this.jdbcTemplate.query(query, args, this.getRowMapper());
        return list.size() > 0 ? Optional.of(list.get(0)) : Optional.empty();
    }

    /**
     * Workaround to return key with HSQLDB.
     * Note that it runs two queries: it creates the entity, and then it retrieves the full model
     * from the DB. This is done to fill in fields which have default values in the DB
     * @param args the arguments
     * @return the newly inserted model
     */
    protected M insertQuery(M model, MapSqlParameterSource args) {
        I id;
        if (!this.customPrimaryKey) {
            id = (I) this.jdbcInsert.executeAndReturnKey(args);
        } else {
            this.jdbcInsert.execute(args);
            id = model.getId();
        }
        return this.findById(id).get();
    }

    /**
     * It returns the table name
     * Can be overwritten to return a table alias
     * @return the table alias
     */
    protected String getTableAlias() {
        return this.getTableName();
    }

    protected String formatColumnFromAlias(String columnName) {
        return formatColumnFromName(columnName, this.getTableAlias());
    }

    protected String formatColumnFromName(String columnName) {
        return formatColumnFromName(columnName, this.getTableName());
    }

    protected String getTableName() {
        return this.tableName;
    }

    protected String getIdColumnName() {
        return this.primaryKeyName;
    }

    /**
     * Returns a map associating column name with an argument name and the value in the model
     * associated with that field. The arguments may be prefixed to avoid name collisions
     * @param model the model
     * @param prefix the arguments' prefix (can be empty)
     * @return the map
     */
    protected Map<String, Pair<String, Object>> getModelColumnsArgumentValue(M model, String prefix) {
        return this.getModelColumnsArgumentValue(model, prefix, false);
    }

    /**
     * Process a ResultSet (a row returned by the DB) and instantiates the model associated with this DAO.
     * It sets all its fields using reflection {@link ReflectionGetterSetter}.
     * If a field is annotated with any representing a Table relation, then it will try to get the model(s)
     * associated with that ID (it acts like a "emulated" join) by running a "findById" or similar in
     * the model's associated DAO. The instance for this is provided by Spring.
     * We do this because
     *      1: we may support something similar to an EntityManager later on which acts like a cache between the
     *      WebApp and the DB, and this will be easier to make when making single Selects with no join tables.
     *      2: it is fairly easier to create new models from the JDBC responses with this way. As this runs a handler
     *      or a mapper per row, it ends up being a lot harder to maintain all the references to already previously
     *      created entities in a query than to run multiple queries and obtain the models "one reference at a time"
     * Also, we know that by using reflection our code becomes a lot more harder to read and comprehend,
     * but in this case it provides a really simple and fast solution to an otherwise big, messy and complex problem.
     *
     * Note: It does NOT have the ability to detect circular references and will most probably enter in an infinite
     * recursion and will run out of memory (stack overflow exception). Relation references should be avoided in the
     * same field of matching models to avoid these circular references.
     * @param resultSet the query result set
     * @return a model
     */
    protected M hydrate(ResultSet resultSet) {
        M nonFinalM;
        try {
            nonFinalM = this.mClass.newInstance();
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }

        try {
            try {
                nonFinalM.setId((I) resultSet.getObject(formatColumnFromName(this.getIdColumnName(), this.getTableAlias())));
            } catch (SQLException e) {
                nonFinalM.setId((I) resultSet.getObject(this.getIdColumnName()));
            }
        } catch (Exception e) {
            return null;
        }

        M m = ProxyHelper.createInstance(nonFinalM);
        Class<? extends GenericModel> mClass = m.getClass();
        // Iterate normal fields
        ReflectionGetterSetter.iterateFields(mClass, Column.class, field -> {
            Column column = field.getAnnotation(Column.class);
            try {
                try {
                    ReflectionGetterSetter.set(m, field, resultSet.getObject(formatColumnFromName(column.name(), this.tableName)));
                } catch (SQLException e) {
                    ReflectionGetterSetter.set(m, field, resultSet.getObject(column.name()));
                }
            } catch (SQLException e) {
                // TODO
//                e.printStackTrace();
            }
        });

        // Iterate all types of relations
        ReflectionGetterSetter.iterateFields(mClass, OneToOne.class, field -> {
            OneToOne relation = field.getAnnotation(OneToOne.class);
            this.processOneToOneRelation(resultSet, m, field, relation.name());
        });
        ReflectionGetterSetter.iterateFields(mClass, OneToMany.class, field -> {
            OneToMany relation = field.getAnnotation(OneToMany.class);
            Class<GenericModel> otherClass;
            try {
                otherClass = (Class<GenericModel>) relation.className();
            } catch (ClassCastException e) {
                // TODO
                throw new RuntimeException(e);
            }

            Table table = otherClass.getAnnotation(Table.class);
            // The other element's IDs are in a different table
            JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                    .select(table.primaryKey())
                    .from(table.name())
                    .where(new JDBCWhereClauseBuilder()
                            .where(formatColumnFromName(relation.name(), table.name()), Operation.EQ, ":id")
                    )
                    .distinct();
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("id", m.getId());
            Set<GenericModel<Object, Object>> otherInstances = new HashSet<>();
            this.selectQuery(
                    queryBuilder.getQueryAsString(),
                    parameterSource,
                    rs -> {
                        try {
                            GenericModel<Object, Object> otherInstance = ProxyHelper.createInstance(otherClass.newInstance());
                            try {
                                otherInstance.setId(rs.getObject(formatColumnFromName(table.primaryKey(), table.name())));
                            } catch (SQLException e) {
                                otherInstance.setId(rs.getObject(table.primaryKey()));
                            }
                            otherInstances.add(otherInstance);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            );

            ProxyHelper.setField(m, field, otherInstances);
            ProxyHelper.setPreviousCollection(m, field, otherInstances);
        });
        ReflectionGetterSetter.iterateFields(mClass, ManyToOne.class, field -> {
            // This is similar to processing a OneToOne relation
            ManyToOne relation = field.getAnnotation(ManyToOne.class);
            this.processOneToOneRelation(resultSet, m, field, relation.name());
        });
        ReflectionGetterSetter.iterateFields(mClass, ManyToMany.class, field -> {
            // This is similar to processing a OneToMany relation but with IDs present in a different table
            ManyToMany relation = field.getAnnotation(ManyToMany.class);
            Class<GenericModel> otherClass;
            try {
                otherClass = (Class<GenericModel>) relation.className();
            } catch (ClassCastException e) {
                // TODO
                throw new RuntimeException(e);
            }

            // The other element's IDs are in a different table
            JDBCSelectQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                    .select(relation.otherName())
                    .from(relation.tableName())
                    .where(new JDBCWhereClauseBuilder()
                            .where(formatColumnFromName(relation.name(), relation.tableName()), Operation.EQ, ":id")
                    )
                    .distinct();
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("id", m.getId());
            Set<GenericModel<Object, Object>> otherInstances = new HashSet<>();
            this.selectQuery(
                    queryBuilder.getQueryAsString(),
                    parameterSource,
                    rs -> {
                        try {
                            GenericModel<Object, Object> otherInstance = ProxyHelper.createInstance(otherClass.newInstance());
                            try {
                                otherInstance.setId(rs.getObject(formatColumnFromName(relation.otherName(), relation.tableName())));
                            } catch (SQLException e) {
                                otherInstance.setId(rs.getObject(relation.otherName()));
                            }
                            otherInstances.add(otherInstance);
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
            );

            ProxyHelper.setField(m, field, otherInstances);
            ProxyHelper.setPreviousCollection(m, field, otherInstances);
        });

        return m;
    }

    // TODO Force set cache on update

    /**
     * TODO
     * Saves all the relations present in this model that have the FK present in a different table (OneToMany and
     * ManyToMany). Should be run inside a transaction to avoid race conditions or orphans in the DB
     * The model must be present in the database
     * @param model the model
     */
    protected void saveRelations(M model) {
        ReflectionGetterSetter.iterateFields(model.getClass(), OneToMany.class, field -> {
            OneToMany relation = field.getAnnotation(OneToMany.class);
            if (relation.inverse())
                return;

            Object o = ReflectionGetterSetter.get(model, field, false);
            Set<GenericModel<Object, Object>> actualModels = (Set<GenericModel<Object, Object>>) o;
            if (relation.required() && (o == null || actualModels.isEmpty())) {
                throw new IllegalStateException("This field is marked as required but its value is null or empty");
            }

            Class<? extends GenericModel<?, Object>> otherClass;
            try {
                otherClass = (Class<? extends GenericModel<?, Object>>) relation.className();
            } catch (ClassCastException e) {
                // TODO
                throw new RuntimeException(e);
            }

            Set<GenericModel<Object, Object>> previousModels = ProxyHelper.getPreviousCollection(model, field);

            MapSqlParameterSource removedParameterSource = new MapSqlParameterSource();
            Collection<String> removedArguments = new LinkedList<>();
            MapSqlParameterSource addedParameterSource = new MapSqlParameterSource();
            Collection<String> addedArguments = new LinkedList<>();
            int i = 0;
            for (GenericModel<Object, Object> genericModel : actualModels) {
                if (!previousModels.contains(genericModel)) {
                    addedParameterSource.addValue("_added_" + i, genericModel.getId());
                    addedArguments.add(":_added_" + i);
                }
            }
            i = 0;
            for (GenericModel<Object, Object> genericModel : previousModels) {
                if (!actualModels.contains(genericModel)) {
                    removedParameterSource.addValue("_removed_" + i, genericModel.getId());
                    removedArguments.add(":_removed_" + i);
                }
            }

            if (removedArguments.size() == 0 && addedArguments.size() == 0)
                return;

            Table otherTable = otherClass.getAnnotation(Table.class);
            // Process removedModels
            if (removedArguments.size() > 0) {
                removedParameterSource.addValue("null", null);
                JDBCUpdateQueryBuilder updateQueryBuilder = new JDBCUpdateQueryBuilder()
                        .update(otherTable.name())
                        .value(relation.name(), ":null")
                        .where(new JDBCWhereClauseBuilder()
                                .in(otherTable.primaryKey(), removedArguments)
                        );
                this.updateQuery(updateQueryBuilder.getQueryAsString(), removedParameterSource);
            }
            // Process addedModels
            if (addedArguments.size() > 0) {
                addedParameterSource.addValue("id", model.getId());
                JDBCUpdateQueryBuilder updateQueryBuilder = new JDBCUpdateQueryBuilder()
                        .update(otherTable.name())
                        .value(relation.name(), ":id")
                        .where(new JDBCWhereClauseBuilder()
                                .in(otherTable.primaryKey(), addedArguments)
                        );
                this.updateQuery(updateQueryBuilder.getQueryAsString(), addedParameterSource);
            }

            ProxyHelper.setPreviousCollection(model, field, actualModels);
        });

        ReflectionGetterSetter.iterateValues(model, OneToMany.class, (field, o) -> {
            OneToMany relation = field.getAnnotation(OneToMany.class);
            if (relation.inverse())
                return;

            Set<GenericModel<Object, Object>> actualModels = (Set<GenericModel<Object, Object>>) o;
            if (relation.required() && (o == null || actualModels.isEmpty())) {
                throw new IllegalStateException("This field is marked as required but its value is null or empty");
            }

            Class<? extends GenericModel<?, Object>> otherClass;
            try {
                otherClass = (Class<? extends GenericModel<?, Object>>) relation.className();
            } catch (ClassCastException e) {
                // TODO
                throw new RuntimeException(e);
            }

            Set<GenericModel<Object, Object>> previousModels = ProxyHelper.getPreviousCollection(model, field);

            MapSqlParameterSource removedParameterSource = new MapSqlParameterSource();
            Collection<String> removedArguments = new LinkedList<>();
            MapSqlParameterSource addedParameterSource = new MapSqlParameterSource();
            Collection<String> addedArguments = new LinkedList<>();
            int i = 0;
            for (GenericModel<Object, Object> genericModel : actualModels) {
                if (!previousModels.contains(genericModel)) {
                    addedParameterSource.addValue("_added_" + i, genericModel.getId());
                    addedArguments.add(":_added_" + i);
                }
            }
            i = 0;
            for (GenericModel<Object, Object> genericModel : previousModels) {
                if (!actualModels.contains(genericModel)) {
                    removedParameterSource.addValue("_removed_" + i, genericModel.getId());
                    removedArguments.add(":_removed_" + i);
                }
            }

            if (removedArguments.size() == 0 && addedArguments.size() == 0)
                return;

            Table otherTable = otherClass.getAnnotation(Table.class);
            // Process removedModels
            if (removedArguments.size() > 0) {
                removedParameterSource.addValue("null", null);
                JDBCUpdateQueryBuilder updateQueryBuilder = new JDBCUpdateQueryBuilder()
                        .update(otherTable.name())
                        .value(relation.name(), ":null")
                        .where(new JDBCWhereClauseBuilder()
                                .in(otherTable.primaryKey(), removedArguments)
                        );
                this.updateQuery(updateQueryBuilder.getQueryAsString(), removedParameterSource);
            }
            // Process addedModels
            if (addedArguments.size() > 0) {
                addedParameterSource.addValue("id", model.getId());
                JDBCUpdateQueryBuilder updateQueryBuilder = new JDBCUpdateQueryBuilder()
                        .update(otherTable.name())
                        .value(relation.name(), ":id")
                        .where(new JDBCWhereClauseBuilder()
                                .in(otherTable.primaryKey(), addedArguments)
                        );
                this.updateQuery(updateQueryBuilder.getQueryAsString(), addedParameterSource);
            }

            ProxyHelper.setPreviousCollection(model, field, actualModels);
        });
        ReflectionGetterSetter.iterateValues(model, ManyToMany.class, (field, o) -> {
            ManyToMany relation = field.getAnnotation(ManyToMany.class);
            if (relation.inverse())
                return;

            Set<GenericModel<Object, Object>> actualModels = (Set<GenericModel<Object, Object>>) o;
            if (relation.required() && (o == null || actualModels.isEmpty())) {
                throw new IllegalStateException("This field is marked as required but its value is null or empty");
            }

            Set<GenericModel<Object, Object>> previousModels = ProxyHelper.getPreviousCollection(model, field);

            MapSqlParameterSource removedParameterSource = new MapSqlParameterSource();
            Collection<String> removedArguments = new LinkedList<>();
            int i = 0;
            for (GenericModel<Object, Object> genericModel : previousModels) {
                if (!actualModels.contains(genericModel)) {
                    removedParameterSource.addValue("_removed_" + i, genericModel.getId());
                    removedArguments.add(":_removed_" + i);
                }
            }

            // Process removedModels
            if (removedArguments.size() > 0) {
                removedParameterSource.addValue("null", null);
                JDBCDeleteQueryBuilder deleteQueryBuilder = new JDBCDeleteQueryBuilder()
                        .from(relation.tableName())
                        .where(new JDBCWhereClauseBuilder()
                                .in(relation.otherName(), removedArguments)
                        );
                this.updateQuery(deleteQueryBuilder.getQueryAsString(), removedParameterSource);
            }

            i = 0;
            for (GenericModel<Object, Object> genericModel : actualModels) {
                if (!previousModels.contains(genericModel)) {
                    MapSqlParameterSource addedParameterSource = new MapSqlParameterSource();
                    Map<String, String> addedColumnNamesArguments = new HashMap<>();

                    addedParameterSource.addValue("_added_my_id_" + i, model.getId());
                    addedParameterSource.addValue("_added_other_id_" + i, genericModel.getId());
                    addedColumnNamesArguments.put(relation.name(), ":_added_my_id_" + i);
                    addedColumnNamesArguments.put(relation.otherName(), ":_added_other_id_" + i);

                    JDBCInsertQueryBuilder insertQueryBuilder = new JDBCInsertQueryBuilder()
                            .into(relation.tableName())
                            .values(addedColumnNamesArguments);
                    this.updateQuery(insertQueryBuilder.getQueryAsString(), addedParameterSource);
                }
            }

            ProxyHelper.setPreviousCollection(model, field, actualModels);
        });
    }

    /**
     * Returns a map associating column name with an argument name and the value in the model
     * associated with that field. The arguments may be prefixed to avoid name collisions.
     * Only OneToOne and ManyToOne fields are included, as OneToMany has the ID in the other table, and
     * ManyToMany uses an intermediate table (which is saved with {@link #saveRelations}
     * @param model the model
     * @param prefix the arguments' prefix (can be empty)
     * @param checkRequired if true then an exception will be thrown when a column has the "required" column argument
     *                     set to true and the model has a null value associated with that field
     * @return the map
     */
    protected Map<String, Pair<String, Object>> getModelColumnsArgumentValue(M model, String prefix, boolean checkRequired) {
        Map<String, Pair<String, Object>> map = new HashMap<>();

        // This code is duplicated so as to not be checking another variable in every loop, thus making it more
        // time efficient at the expense of having duplicated code.
        if (checkRequired) {
            ReflectionGetterSetter.iterateValues(model, Column.class, (field, o) -> {
                Column column = field.getAnnotation(Column.class);
                if (column.required() && o == null)
                    throw new IllegalStateException("This field is marked as required but its value is null");

                map.put(column.name(), new Pair<>(":" + prefix + column.name(), o));
            });
            ReflectionGetterSetter.iterateValues(model, OneToOne.class, (field, o) -> {
                OneToOne relation = field.getAnnotation(OneToOne.class);
                if (relation.required() && o == null)
                    throw new IllegalStateException("This field is marked as required but its value is null");
                if (relation.inverse())
                    return;

                GenericModel<Object, Object> genericModel = (GenericModel<Object, Object>) o;
                map.put(relation.name(), new Pair<>(":" + prefix + relation.name(), genericModel.getId()));
            });
            ReflectionGetterSetter.iterateValues(model, ManyToOne.class, (field, o) -> {
                ManyToOne relation = field.getAnnotation(ManyToOne.class);
                if (relation.required() && o == null)
                    throw new IllegalStateException("This field is marked as required but its value is null");
                if (relation.inverse())
                    return;

                GenericModel<Object, Object> genericModel = (GenericModel<Object, Object>) o;
                map.put(relation.name(), new Pair<>(":" + prefix + relation.name(), genericModel.getId()));
            });
        } else {
            ReflectionGetterSetter.iterateValues(model, Column.class, (field, o) -> {
                Column column = field.getAnnotation(Column.class);
                map.put(column.name(), new Pair<>(":" + prefix + column.name(), o));
            });
            ReflectionGetterSetter.iterateValues(model, OneToOne.class, (field, o) -> {
                OneToOne relation = field.getAnnotation(OneToOne.class);
                if (relation.inverse())
                    return;

                GenericModel<Object, Object> genericModel = (GenericModel<Object, Object>) o;
                map.put(relation.name(), new Pair<>(":" + prefix + relation.name(), genericModel.getId()));
            });
            ReflectionGetterSetter.iterateValues(model, ManyToOne.class, (field, o) -> {
                ManyToOne relation = field.getAnnotation(ManyToOne.class);
                if (relation.inverse())
                    return;

                GenericModel<Object, Object> genericModel = (GenericModel<Object, Object>) o;
                map.put(relation.name(), new Pair<>(":" + prefix + relation.name(), genericModel.getId()));
            });
        }

        return map;
    }

    protected boolean isCacheComplete(CachedCollection<M> cachedCollection) {
        return this.haveBeenListed && cachedCollection.getStatus() == CachedCollectionStatus.COMPLETE;
    }

    protected synchronized M getFromCache(I id) {
        return CacheHelper.get(this.mClass, this.iClass, id);
    }

    protected synchronized void setToCache(M model) {
        CacheHelper.set(this.mClass, this.iClass, model);
    }

    protected synchronized void setToCache(Collection<M> models) {
        CacheHelper.set(this.mClass, this.iClass, models);
    }

    protected synchronized void removeFromCache(I id) {
        CacheHelper.remove(this.mClass, this.iClass, id);
    }

    protected synchronized CachedCollection<M> listFromCache() {
        return CacheHelper.getAll(this.mClass, this.iClass);
    }

    protected synchronized FilteredCachedCollection<M> filterFromCache(String columnName, Operation operation, Object value) {
        return CacheHelper.filter(this.mClass, this.iClass, m -> {
            Object mValue = ReflectionGetterSetter.getValueAnnotatedWith(m, Column.class, column -> column.name().equals(columnName));
            return operation.operate(mValue, value);
        });
    }

    protected synchronized FilteredCachedCollection<M> filterFromCacheIgnoreCase(String columnName, String value, StringSearchType stringSearchType) {
        return CacheHelper.filter(this.mClass, this.iClass, m -> {
            Object mValue = ReflectionGetterSetter.getValueAnnotatedWith(m, Column.class, column -> column.name().equals(columnName));
            if (mValue == null) return value == null;
            return stringSearchType.operate(mValue.toString().toLowerCase(), value.toLowerCase());
        });
    }

    protected synchronized FilteredCachedCollection<M> filterFromCacheIn(String columnName, Collection<? extends Object> values) {
        return this.filterFromCache(columnName, Operation.EQ, values);
    }

    protected void excludeModels(Collection<M> models, MapSqlParameterSource parameterSource, JDBCWhereClauseBuilder whereClauseBuilder) {
        List<String> arguments = new LinkedList<>();
        int i = 0;
        for (M m : models) {
            String prefix = "_excluded_id_" + i;
            arguments.add(":" + prefix);
            parameterSource.addValue(prefix, m.getId());
            i++;
        }

        whereClauseBuilder
                .and()
                .notIn(this.formatColumnFromAlias(this.getIdColumnName()), arguments);
    }

    private void processOneToOneRelation(ResultSet resultSet, M m, Field field, String columnName) {
        Class<? extends GenericModel<?, Object>> otherClass;
        try {
            otherClass = (Class<? extends GenericModel<?, Object>>) field.getType();
        } catch (ClassCastException e) {
            // TODO
            throw new RuntimeException(e);
        }
        GenericModel<? extends GenericModel<?, Object>, Object> otherInstance;
        try {
            otherInstance = (GenericModel<? extends GenericModel<?, Object>, Object>) otherClass.newInstance();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }

        try {
            Object id;
            try {
                id = resultSet.getObject(formatColumnFromName(columnName, this.getTableAlias()));
            } catch (SQLException e) {
                id = resultSet.getObject(columnName);
            }

            if (id != null) {
                otherInstance.setId(id);
                ProxyHelper.setField(m, field, otherInstance);
            }
        } catch (SQLException e) {
            // TODO
//                e.printStackTrace();
        }
    }

    private void assertModelUpdate(M model) throws NotManagedByDAOException {
        if (!ProxyHelper.isProxied(model))
            throw new NotManagedByDAOException();
    }

    protected abstract RowMapper<M> getRowMapper();

    protected static String formatColumnFromName(String columnName, String tableName) {
        return tableName + "." + columnName;
    }

    protected static <M extends GenericModel<M, I>, I> String getTableNameFromModel(Class<M> mClass) {
        if (mClass.isAnnotationPresent(Table.class)) {
            return mClass.getAnnotation(Table.class).name();
        }
        return null;
    }

    protected static <M extends GenericModel<M, I>, I> String getPrimaryKeyNameFromModel(Class<M> mClass) {
        if (mClass.isAnnotationPresent(Table.class)) {
            return mClass.getAnnotation(Table.class).primaryKey();
        }
        return null;
    }
}
