package ar.edu.itba.paw.persistence.generics;

import ar.edu.itba.paw.interfaces.daos.generic.GenericDao;
import ar.edu.itba.paw.models.GenericModel;
import ar.edu.itba.paw.models.JoinedCollection;
import ar.edu.itba.paw.persistence.utils.DAOManager;
import ar.edu.itba.paw.persistence.utils.Pair;
import ar.edu.itba.paw.persistence.utils.ReflectionGetterSetter;
import ar.edu.itba.paw.persistence.utils.builder.*;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.ColumnTransformer;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.Operation;
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
 * This provides a generic DAO abstract class
 * @param <M> the DAO model type
 * @param <I> the Model's id type
 */
public abstract class GenericDaoImpl<M extends GenericModel<I>, I> implements GenericDao<M, I> {
    private static final String ARGUMENT_PREFIX = "_r_";
    protected TransactionTemplate transactionTemplate;
    protected NamedParameterJdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private boolean customPrimaryKey;
    private String primaryKeyName;
    private final Class<M> mClass;
    private String tableName;

    public GenericDaoImpl(DataSource dataSource, Class<M> mClass) {
        this.transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.mClass = mClass;

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
        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .where(new JDBCWhereClauseBuilder()
                        .where(this.formatColumnFromAlias(this.getIdColumnName()), Operation.EQ, ":id")
                );

        MapSqlParameterSource args = new MapSqlParameterSource();
        // Note that "parameterName" should NOT be preceded by a semicolon (as it is in the query)
        args.addValue("id", id);

        return this.selectQuerySingle(queryBuilder.getQueryAsString(), args);
    }

    @Override
    public Collection<M> findByIds(Collection<I> ids) {
        if (ids.isEmpty())
            return new LinkedList<>();

        Map<String, Object> parameters = new HashMap<>();
        Collection<String> idsParameters = new LinkedList<>();

        int i = 0;
        for (I id : ids) {
            String parameter = "_ids_" + i;
            parameters.put(parameter, id);
            idsParameters.add(":" + parameter);
            i++;
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .where(new JDBCWhereClauseBuilder()
                        .in(this.formatColumnFromAlias(this.getIdColumnName()), idsParameters)
                );

        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValues(parameters);

        return this.selectQuery(queryBuilder.getQueryAsString(), args);
    }

    @Override
    public M create(M model) {
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
            return this.findById(newModel.getId());
        }).get();
    }

    /**
     * Updates ALL the information inside the model (with the exception of the ID)
     * Relations are also updated
     * @param model the model
     */
    @Override
    public void update(M model) {
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
            return null;
        });
    }

    @Override
    public void remove(M model) {
        this.remove(model.getId());
    }

    @Override
    public void remove(I id) {
        JDBCQueryBuilder queryBuilder = new JDBCDeleteQueryBuilder()
                .from(this.getTableAlias())
                .where(new JDBCWhereClauseBuilder()
                        .where(this.formatColumnFromAlias(this.getIdColumnName()), Operation.EQ, ":id")
                );

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);

        this.updateQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public List<M> list() {
        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias());

        return this.selectQuery(queryBuilder.getQueryAsString());
    }

    /**
     * Searches for a collection of models that have a columnName equals to the provided object's value
     * @param columnName the db column name
     * @param value the column's value
     * @return a collection of models found
     */
    @Override
    public List<M> findByField(String columnName, Object value) {
        return this.findByField(columnName, Operation.EQ, value);
    }

    @Override
    public Class<M> getModelClass() {
        return this.mClass;
    }

    public List<M> findByField(String columnName, Operation operation, Object value) {
        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .where(new JDBCWhereClauseBuilder()
                        .where(this.formatColumnFromAlias(columnName), operation, ":argument")
                );

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("argument", value);

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    protected List<M> findByFieldIgnoreCase(String columnName, Operation operation, String value) {
        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .where(new JDBCWhereClauseBuilder()
                        .where(this.formatColumnFromAlias(columnName), operation, ":argument", ColumnTransformer.LOWER)
                );

        value = value.toLowerCase();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("argument", value);

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    protected List<M> findByFieldIn(String columnName, Collection<?> values) {
        if (values.isEmpty())
            return new LinkedList<>();

        Map<String, Object> parameters = new HashMap<>();
        int i = 0;
        for (Object value : values) {
            parameters.put("_" + i, value);
            i++;
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .where(new JDBCWhereClauseBuilder()
                        .in(this.formatColumnFromAlias(columnName), parameters.keySet())
                );

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(parameters);

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    protected List<M> selectQuery(String query) {
        return this.jdbcTemplate.query(query, this.getRowMapper());
    }

    protected List<M> selectQuery(String query, MapSqlParameterSource args) {
        return this.jdbcTemplate.query(query, args, this.getRowMapper());
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
        M m;
        try {
            m = this.mClass.newInstance();
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }

        try {
            try {
                ReflectionGetterSetter.set(m, "id", resultSet.getObject(formatColumnFromName(this.primaryKeyName, this.tableName)));
            } catch (SQLException e) {
                ReflectionGetterSetter.set(m, "id", resultSet.getObject(this.primaryKeyName));
            }
        } catch (Exception e) {
            return null;
        }

        // Iterate normal fields
        ReflectionGetterSetter.iterateFields(this.mClass, Column.class, field -> {
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
        ReflectionGetterSetter.iterateFields(this.mClass, OneToOne.class, field -> {
            OneToOne relation = field.getAnnotation(OneToOne.class);
            this.processOneToOneRelation(resultSet, m, field, relation.name());
        });
        ReflectionGetterSetter.iterateFields(this.mClass, OneToMany.class, field -> {
            OneToMany relation = field.getAnnotation(OneToMany.class);
            Class<? extends GenericModel<Object>> otherClass;
            try {
                otherClass = (Class<? extends GenericModel<Object>>) relation.className();
            } catch (ClassCastException e) {
                // TODO
                throw new RuntimeException(e);
            }


            GenericDao<? extends GenericModel<Object>, Object> otherDao = DAOManager.getDaoForModel(otherClass);
            Collection<? extends GenericModel<Object>> otherInstances = otherDao.findByField(relation.name(), m.getId());
            Collection<? extends GenericModel<Object>> otherInstancesCopy = new LinkedList<>(otherInstances);

            JoinedCollection<GenericModel<Object>> collection = new JoinedCollection<>();
            collection.setModels((Collection<GenericModel<Object>>) otherInstances);
            try {
                ReflectionGetterSetter.set(collection, JoinedCollection._PRIVATE_COLLECTION_NAME, otherInstancesCopy);
            } catch (NoSuchFieldException e) {
                // TODO
                e.printStackTrace();
            }
            ReflectionGetterSetter.set(m, field, otherInstances);
        });
        ReflectionGetterSetter.iterateFields(this.mClass, ManyToOne.class, field -> {
            // This is similar to processing a OneToOne relation
            ManyToOne relation = field.getAnnotation(ManyToOne.class);
            this.processOneToOneRelation(resultSet, m, field, relation.name());
        });
        ReflectionGetterSetter.iterateFields(this.mClass, ManyToMany.class, field -> {
            // This is similar to processing a OneToMany relation but with IDs present in a different table
            ManyToMany relation = field.getAnnotation(ManyToMany.class);
            Class<? extends GenericModel<Object>> otherClass;
            try {
                otherClass = (Class<? extends GenericModel<Object>>) relation.className();
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
            Collection<Object> otherIds = new LinkedList<>();
            this.selectQuery(
                    queryBuilder.getQueryAsString(),
                    parameterSource,
                    rs -> {
                        try {
                            otherIds.add(rs.getObject(formatColumnFromName(relation.otherName(), relation.tableName())));
                        } catch (SQLException e) {
                            otherIds.add(rs.getObject(relation.otherName()));
                        }
                    }
            );

            GenericDao<? extends GenericModel<Object>, Object> otherDao = DAOManager.getDaoForModel(otherClass);
            Collection<? extends GenericModel<Object>> otherInstances = otherDao.findByIds(otherIds);
            Collection<? extends GenericModel<Object>> otherInstancesCopy = new LinkedList<>(otherInstances);

            JoinedCollection<GenericModel<Object>> collection = new JoinedCollection<>();
            collection.setModels((Collection<GenericModel<Object>>) otherInstances);
            try {
                ReflectionGetterSetter.set(collection, JoinedCollection._PRIVATE_COLLECTION_NAME, otherInstancesCopy);
            } catch (NoSuchFieldException e) {
                // TODO
                e.printStackTrace();
            }
            ReflectionGetterSetter.set(m, field, otherInstances);
        });

        return m;
    }

    /**
     * Saves all the relations present in this model that have the fk present in a different table (OneToMany and
     * ManyToMany). Should be run inside a transaction to avoid race conditions
     * The model must be present in the database
     * @param model the model
     * @return the model fetched from the DB
     */
    protected void saveRelations(M model) {
        ReflectionGetterSetter.iterateValues(model, OneToMany.class, true, (field, o) -> {
            OneToMany relation = field.getAnnotation(OneToMany.class);

            JoinedCollection<GenericModel<Object>> joinedCollectionGenericModel = (JoinedCollection<GenericModel<Object>>) o;
            if (relation.required() && (o == null || joinedCollectionGenericModel.getModels().isEmpty())) {
                throw new IllegalStateException("This field is marked as required but its value is null or empty");
            }

            Class<? extends GenericModel<Object>> otherClass;
            try {
                otherClass = (Class<? extends GenericModel<Object>>) relation.className();
            } catch (ClassCastException e) {
                // TODO
                throw new RuntimeException(e);
            }

            Collection<GenericModel<Object>> previousModels;
            try {
                previousModels = (Collection<GenericModel<Object>>) ReflectionGetterSetter.get(
                        joinedCollectionGenericModel,
                        JoinedCollection._PRIVATE_COLLECTION_NAME
                );
                if (previousModels == null)
                    previousModels = joinedCollectionGenericModel.getModels();
            } catch (NoSuchFieldException e) {
                // TODO
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            MapSqlParameterSource removedParameterSource = new MapSqlParameterSource();
            Collection<String> removedArguments = new LinkedList<>();
            MapSqlParameterSource addedParameterSource = new MapSqlParameterSource();
            Collection<String> addedArguments = new LinkedList<>();
            int i = 0;
            for (GenericModel<Object> genericModel : joinedCollectionGenericModel.getModels()) {
                if (!previousModels.contains(genericModel)) {
                    removedParameterSource.addValue("_removed_" + i, genericModel.getId());
                    removedArguments.add(":_removed_" + i);
                }
            }
            i = 0;
            for (GenericModel<Object> genericModel : previousModels) {
                if (!joinedCollectionGenericModel.getModels().contains(genericModel)) {
                    addedParameterSource.addValue("_added_" + i, genericModel.getId());
                    addedArguments.add(":_added_" + i);
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

            try {
                ReflectionGetterSetter.set(
                        joinedCollectionGenericModel,
                        JoinedCollection._PRIVATE_COLLECTION_NAME,
                        new LinkedList<>(joinedCollectionGenericModel.getModels())
                );
            } catch (NoSuchFieldException e) {
                // TODO
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        ReflectionGetterSetter.iterateValues(model, ManyToMany.class, (field, o) -> {
            ManyToMany relation = field.getAnnotation(ManyToMany.class);

            JoinedCollection<GenericModel<Object>> joinedCollectionGenericModel = (JoinedCollection<GenericModel<Object>>) o;
            if (relation.required() && (o == null || joinedCollectionGenericModel.getModels().isEmpty())) {
                throw new IllegalStateException("This field is marked as required but its value is null");
            }

            Class<? extends GenericModel<Object>> otherClass;
            try {
                otherClass = (Class<? extends GenericModel<Object>>) relation.className();
            } catch (ClassCastException e) {
                // TODO
                throw new RuntimeException(e);
            }

            Collection<GenericModel<Object>> previousModels;
            try {
                previousModels = (Collection<GenericModel<Object>>) ReflectionGetterSetter.get(
                        joinedCollectionGenericModel,
                        JoinedCollection._PRIVATE_COLLECTION_NAME
                );
                if (previousModels == null)
                    previousModels = joinedCollectionGenericModel.getModels();
            } catch (NoSuchFieldException e) {
                // TODO
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            MapSqlParameterSource removedParameterSource = new MapSqlParameterSource();
            Collection<String> removedArguments = new LinkedList<>();
            int i = 0;
            for (GenericModel<Object> genericModel : joinedCollectionGenericModel.getModels()) {
                if (!previousModels.contains(genericModel)) {
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
            for (GenericModel<Object> genericModel : previousModels) {
                if (!joinedCollectionGenericModel.getModels().contains(genericModel)) {
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

            try {
                ReflectionGetterSetter.set(
                        joinedCollectionGenericModel,
                        JoinedCollection._PRIVATE_COLLECTION_NAME,
                        new LinkedList<>(joinedCollectionGenericModel.getModels())
                );
            } catch (NoSuchFieldException e) {
                // TODO
                e.printStackTrace();
                throw new RuntimeException(e);
            }
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

                GenericModel<Object> genericModel = (GenericModel<Object>) o;
                map.put(relation.name(), new Pair<>(":" + prefix + relation.name(), genericModel.getId()));
            });
            ReflectionGetterSetter.iterateValues(model, ManyToOne.class, (field, o) -> {
                ManyToOne relation = field.getAnnotation(ManyToOne.class);
                if (relation.required() && o == null)
                    throw new IllegalStateException("This field is marked as required but its value is null");

                GenericModel<Object> genericModel = (GenericModel<Object>) o;
                map.put(relation.name(), new Pair<>(":" + prefix + relation.name(), genericModel.getId()));
            });
        } else {
            ReflectionGetterSetter.iterateValues(model, Column.class, (field, o) -> {
                Column column = field.getAnnotation(Column.class);
                map.put(column.name(), new Pair<>(":" + prefix + column.name(), o));
            });
            ReflectionGetterSetter.iterateValues(model, OneToOne.class, (field, o) -> {
                OneToOne relation = field.getAnnotation(OneToOne.class);
                GenericModel<Object> genericModel = (GenericModel<Object>) o;
                map.put(relation.name(), new Pair<>(":" + prefix + relation.name(), genericModel.getId()));
            });
            ReflectionGetterSetter.iterateValues(model, ManyToOne.class, (field, o) -> {
                ManyToOne relation = field.getAnnotation(ManyToOne.class);
                GenericModel<Object> genericModel = (GenericModel<Object>) o;
                map.put(relation.name(), new Pair<>(":" + prefix + relation.name(), genericModel.getId()));
            });
        }

        return map;
    }

    private void processOneToOneRelation(ResultSet resultSet, M m, Field field, String columnName) {
        Class<? extends GenericModel<Object>> otherClass;
        try {
            otherClass = (Class<? extends GenericModel<Object>>) field.getType();
        } catch (ClassCastException e) {
            // TODO
            throw new RuntimeException(e);
        }

        GenericDao<? extends GenericModel<?>, Object> otherDao = DAOManager.getDaoForModel(otherClass);
        GenericModel<Object> otherInstance;
        try {
            try {
                otherInstance = otherDao.findById(resultSet.getObject(formatColumnFromName(columnName, this.tableName))).orElse(null);
            } catch (SQLException e) {
                otherInstance = otherDao.findById(resultSet.getObject(columnName)).orElse(null);
            }

            ReflectionGetterSetter.set(m, field, otherInstance);
        } catch (SQLException e) {
            // TODO
//                e.printStackTrace();
        }
    }

    protected abstract RowMapper<M> getRowMapper();

    protected static String formatColumnFromName(String columnName, String tableName) {
        return tableName + "." + columnName;
    }

    protected static <M extends GenericModel<I>, I> String getTableNameFromModel(Class<M> mClass) {
        if (mClass.isAnnotationPresent(Table.class)) {
            return mClass.getAnnotation(Table.class).name();
        }
        return null;
    }
}
