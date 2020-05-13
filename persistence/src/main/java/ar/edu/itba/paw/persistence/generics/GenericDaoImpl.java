package ar.edu.itba.paw.persistence.generics;

import ar.edu.itba.paw.interfaces.daos.generic.GenericDao;
import ar.edu.itba.paw.models.GenericModel;
import ar.edu.itba.paw.persistence.utils.JDBCArgumentValue;
import ar.edu.itba.paw.persistence.utils.ReflectionGetterSetter;
import ar.edu.itba.paw.persistence.utils.StringSearchType;
import ar.edu.itba.paw.persistence.utils.builder.*;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.ColumnTransformer;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.Operation;
import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.OrderBy;
import ar.edu.itba.paw.persistenceAnnotations.Table;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.Map.Entry;

/**
 * This provides a generic DAO implementation with lots of useful methods
 * @param <M> the DAO model type
 * @param <I> the Model's id type
 */
public abstract class GenericDaoImpl<M extends GenericModel<I>, I> implements GenericDao<M, I> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericDaoImpl.class);
    private static final String ARGUMENT_PREFIX = "_r_";
    protected TransactionTemplate transactionTemplate;
    protected NamedParameterJdbcTemplate jdbcTemplate;
    protected SimpleJdbcInsert jdbcInsert;
    private boolean customPrimaryKey;
    private String primaryKeyName;
    private final Class<M> mClass;
    private String tableName;

    public GenericDaoImpl(DataSource dataSource, Class<M> mClass) {
        this.transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.mClass = mClass;

        if (mClass.isAnnotationPresent(Table.class)) {
            Table table = mClass.getAnnotation(Table.class);
            this.tableName = table.name();
            this.primaryKeyName = table.primaryKey();
            this.customPrimaryKey = table.manualPrimaryKey();
            this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(this.tableName);
            if (!this.customPrimaryKey)
                this.jdbcInsert.usingGeneratedKeyColumns(this.primaryKeyName);
        }
    }

    @Override
    public Optional<M> findById(I id) {
        JDBCSelectQueryBuilder selectQueryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(this.mClass)
                .from(this.getTableAlias())
                .where(new JDBCWhereClauseBuilder()
                        .where(this.formatColumnFromAlias(this.getIdColumnName()), Operation.EQ, ":id")
                );

        MapSqlParameterSource args = new MapSqlParameterSource();
        // Note that "parameterName" should NOT be preceded by a semicolon (as it is in the query)
        args.addValue("id", id);

        return this.selectQuerySingle(selectQueryBuilder, args);
    }

    @Override
    public List<M> findByIds(Collection<I> ids) {
        if (ids.isEmpty())
            return new LinkedList<>();

        Map<String, Object> parameters = new HashMap<>();
        Collection<String> idsParameters = new LinkedList<>();
        int i = 0;
        for (I id : ids) {
            String parameter = "_id_" + i;
            idsParameters.add(":" + parameter);
            parameters.put(parameter, id);
            i++;
        }

        JDBCSelectQueryBuilder selectQueryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(this.mClass)
                .from(this.getTableAlias())
                .where(new JDBCWhereClauseBuilder()
                        .in(this.formatColumnFromAlias(this.getIdColumnName()), idsParameters)
                );

        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValues(parameters);

        return this.selectQuery(selectQueryBuilder, args);
    }

    @Override
    public synchronized M create(M model) {
        Map<String, JDBCArgumentValue> columnsArgumentValue = this.getModelColumnsArgumentValue(model, "", true);
        if (this.customPrimaryKey) {
            columnsArgumentValue.put(this.getIdColumnName(), new JDBCArgumentValue(this.getIdColumnName(), model.getId()));
        }

        Map<String, Object> argumentsValues = new HashMap<>();
        for (Entry<String, JDBCArgumentValue> columnArgumentValue : columnsArgumentValue.entrySet()) {
            argumentsValues.put(columnArgumentValue.getKey(), columnArgumentValue.getValue().getValue());
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(argumentsValues);

        return this.transactionTemplate.execute(transactionStatus -> {
            M newModel = this.insertQuery(model, parameterSource);
            return this.findById(newModel.getId()).get();
        });
    }

    /**
     * Updates ALL the information inside the model (with the exception of the ID) including relations
     * @param model the model
     */
    @Override
    public synchronized void update(M model) {
        Map<String, JDBCArgumentValue> columnsArgumentValue = this.getModelColumnsArgumentValue(model, ARGUMENT_PREFIX, true);

        Map<String, String> columnsArguments = new HashMap<>();
        Map<String, Object> argumentsValues = new HashMap<>();
        for (Entry<String, JDBCArgumentValue> columnArgumentValue : columnsArgumentValue.entrySet()) {
            columnsArguments.put(columnArgumentValue.getKey(), columnArgumentValue.getValue().getArgument());
            argumentsValues.put(ARGUMENT_PREFIX + columnArgumentValue.getKey(), columnArgumentValue.getValue().getValue());
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

        this.updateQuery(queryBuilder.getQueryAsString(), parameterSource);
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
    }

    @Override
    public List<M> list() {
        JDBCSelectQueryBuilder selectQueryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(this.mClass)
                .from(this.getTableAlias());
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        return this.selectQuery(selectQueryBuilder, parameterSource);
    }

    @Override
    public Class<M> getModelClass() {
        return this.mClass;
    }

    @Override
    public List<M> findByField(String field, Object value) {
        return this.findByField(field, Operation.EQ, value);
    }

    @Override
    public Optional<?> findFieldById(I id, String field) {
        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromAlias(this.getIdColumnName()), Operation.EQ, ":id");

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);

        JDBCSelectQueryBuilder selectQueryBuilder = new JDBCSelectQueryBuilder()
                .select(field)
                .from(this.getTableAlias())
                .where(whereClauseBuilder);

        Collection<Object> values = new LinkedList<>();
        this.selectQuery(
                selectQueryBuilder,
                parameterSource,
                rs -> values.add(rs.getObject(field))
        );

        return values.stream().findFirst();
    }

    /**
     * @param columnName
     * @param operation
     * @param value if value is a generic model then it will use its id
     * @return
     */
    public List<M> findByField(String columnName, Operation operation, Object value) {
        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromAlias(columnName), operation, ":argument");

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        if (value instanceof GenericModel) {
            parameterSource.addValue("argument", ((GenericModel) value).getId());
        } else {
            parameterSource.addValue("argument", value);
        }

        JDBCSelectQueryBuilder selectQueryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(this.mClass)
                .from(this.getTableAlias())
                .where(whereClauseBuilder);

        return this.selectQuery(selectQueryBuilder, parameterSource);
    }

    protected List<M> findByFieldIgnoreCase(String columnName, Operation operation, String value) {
        return this.findByFieldIgnoreCase(columnName, operation, value, StringSearchType.CONTAINS_NO_ACC);
    }

    protected List<M> findByFieldIgnoreCase(String columnName, Operation operation, String value, StringSearchType stringSearchType) {
        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromAlias(columnName), operation, ":argument", ColumnTransformer.LOWER);

        value = value.toLowerCase();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("argument", stringSearchType.transform(value));

        JDBCSelectQueryBuilder selectQueryBuilder = new JDBCSelectQueryBuilder()
                .selectAll(this.mClass)
                .from(this.getTableAlias())
                .where(whereClauseBuilder);

        return this.selectQuery(selectQueryBuilder, parameterSource);
    }

    protected List<M> selectQuery(JDBCSelectQueryBuilder selectQueryBuilder) {
        selectQueryBuilder = this.wrapSelectQueryBuilder(selectQueryBuilder);
        return new LinkedList<>(this.jdbcTemplate.query(selectQueryBuilder.getQueryAsString(), this.getResultSetExtractor()));
    }

    /**
     * Fixes LIMIT selects wrapping it inside of other select
     */
    private JDBCSelectQueryBuilder wrapSelectQueryBuilder(JDBCSelectQueryBuilder selectQueryBuilder) {
        JDBCSelectQueryBuilder wrapper;
        if (selectQueryBuilder.hasLimit() || selectQueryBuilder.hasOffset()) {
            wrapper = new JDBCSelectQueryBuilder();
            wrapper.from(selectQueryBuilder);
            this.insertOrderBy(selectQueryBuilder);  // We need them in both
        } else {
            wrapper = selectQueryBuilder;
        }

        this.insertOrderBy(wrapper);
        this.populateJoins(wrapper);
        return wrapper;
    }

    protected List<M> selectQuery(JDBCSelectQueryBuilder selectQueryBuilder, MapSqlParameterSource args) {
        selectQueryBuilder = this.wrapSelectQueryBuilder(selectQueryBuilder);
        return new LinkedList<>(this.jdbcTemplate.query(selectQueryBuilder.getQueryAsString(), args, this.getResultSetExtractor()));
    }

    /**
     * Runs a query handled by a specific handler
     * @param selectQueryBuilder the query
     * @param args the arguments (can be empty)
     * @param callbackHandler the ResultSet handler
     */
    protected void selectQuery(JDBCSelectQueryBuilder selectQueryBuilder, MapSqlParameterSource args, RowCallbackHandler callbackHandler) {
        selectQueryBuilder = this.wrapSelectQueryBuilder(selectQueryBuilder);
        this.jdbcTemplate.query(selectQueryBuilder.getQueryAsString(), args, callbackHandler);
    }

    protected Optional<M> selectQuerySingle(JDBCSelectQueryBuilder selectQueryBuilder) {
        selectQueryBuilder = this.wrapSelectQueryBuilder(selectQueryBuilder);
        return this.jdbcTemplate.query(selectQueryBuilder.getQueryAsString(), this.getResultSetExtractor()).stream().findFirst();
    }

    protected Optional<M> selectQuerySingle(JDBCSelectQueryBuilder selectQueryBuilder, MapSqlParameterSource args) {
        selectQueryBuilder = this.wrapSelectQueryBuilder(selectQueryBuilder);
        return this.jdbcTemplate.query(selectQueryBuilder.getQueryAsString(), args, this.getResultSetExtractor()).stream().findFirst();
    }

    /**
     * Runs an Insert/Update/Delete query
     * @param query the query
     * @param args the arguments
     */
    protected void updateQuery(String query, MapSqlParameterSource args) {
        this.jdbcTemplate.update(query, args);
    }

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

    protected boolean exists(Map<String, ?> columnsValues) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(columnsValues);
        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder();
        for (String column : columnsValues.keySet()) {
            whereClauseBuilder
                    .and()
                    .where(column, Operation.EQ, ":" + column);
        }

        JDBCSelectQueryBuilder selectQueryBuilder = new JDBCSelectQueryBuilder()
                .select(this.getIdColumnName())
                .from(this.getTableName())
                .where(whereClauseBuilder)
                .limit(1);

        return this.jdbcTemplate.query(selectQueryBuilder.getQueryAsString(), parameterSource, ResultSet::next);
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
     * associated with that field. The arguments may be prefixed to avoid name collisions.
     * Only Column fields are included
     * @param model the model
     * @param prefix the arguments' prefix (can be empty)
     * @param checkRequired if true then an exception will be thrown when a column has the "required" column argument
     *                     set to true and the model has a null value associated with that field
     * @return the map
     */
    protected Map<String, JDBCArgumentValue> getModelColumnsArgumentValue(M model, String prefix, boolean checkRequired) {
        Map<String, JDBCArgumentValue> map = new HashMap<>();

        // This code is duplicated so as to not be checking another variable in every loop, thus making it more
        // time efficient at the expense of having duplicated code.
        if (checkRequired) {
            ReflectionGetterSetter.iterateValues(model, Column.class, (field, o) -> {
                Column column = field.getAnnotation(Column.class);
                if (column.required() && o == null)
                    throw new IllegalStateException("This field is marked as required but its value is null");

                if (field.getType().equals(DateTime.class) && o != null) {
                    map.put(column.name(), new JDBCArgumentValue(prefix + column.name(), Timestamp.from(Instant.ofEpochMilli(((DateTime) o).getMillis()))));
                } else {
                    map.put(column.name(), new JDBCArgumentValue(prefix + column.name(), o));
                }
            });
        } else {
            ReflectionGetterSetter.iterateValues(model, Column.class, (field, o) -> {
                Column column = field.getAnnotation(Column.class);

                if (field.getType().equals(DateTime.class) && o != null) {
                    map.put(column.name(), new JDBCArgumentValue(prefix + column.name(), Timestamp.from(Instant.ofEpochMilli(((DateTime) o).getMillis()))));
                } else {
                    map.put(column.name(), new JDBCArgumentValue(prefix + column.name(), o));
                }
            });
        }

        Map<String, JDBCArgumentValue> relationsMap = this.getModelRelationsArgumentValue(model, prefix);
        if (relationsMap != null)
            map.putAll(relationsMap);
        return map;
    }

    private void insertOrderBy(JDBCSelectQueryBuilder selectQueryBuilder) {
        if (selectQueryBuilder.hasOrderBy())
            return;

        ReflectionGetterSetter.iterateFields(this.mClass, OrderBy.class, field -> {
            OrderBy orderBy = field.getAnnotation(OrderBy.class);
            Column column = field.getAnnotation(Column.class);
            if (column == null)
                return;

            selectQueryBuilder.orderBy(this.formatColumnFromAlias(column.name()), orderBy.value(), orderBy.priority());
        });
    }

    protected abstract ResultSetExtractor<List<M>> getResultSetExtractor();

    protected abstract void populateJoins(JDBCSelectQueryBuilder selectQueryBuilder);

    protected abstract Map<String, JDBCArgumentValue> getModelRelationsArgumentValue(M model, String prefix);

    protected static String formatColumnFromName(String columnName, String tableName) {
        return tableName + "." + columnName;
    }

    protected static <M extends GenericModel<I>, I> String getTableNameFromModel(Class<M> mClass) {
        if (mClass.isAnnotationPresent(Table.class)) {
            return mClass.getAnnotation(Table.class).name();
        }
        return null;
    }

    protected static <M extends GenericModel<I>, I> String getPrimaryKeyNameFromModel(Class<M> mClass) {
        if (mClass.isAnnotationPresent(Table.class)) {
            return mClass.getAnnotation(Table.class).primaryKey();
        }
        return null;
    }

    protected static <M extends GenericModel<I>, I> void populateEntity(M model, ResultSet resultSet, String columnPrefix) {
        ReflectionGetterSetter.iterateFields(model.getClass(), Column.class, field -> {
            Column column = field.getAnnotation(Column.class);
            Object value;
            try {
                if (field.getType().equals(DateTime.class)) {
                    Timestamp sqlTimestamp = resultSet.getTimestamp(formatColumnFromName(column.name(), columnPrefix));
                    value = new DateTime(sqlTimestamp);
                } else {
                    value = resultSet.getObject(formatColumnFromName(column.name(), columnPrefix));
                }

                ReflectionGetterSetter.set(model, field, value);
            } catch (SQLException e) {
                try {
                    if (field.getType().equals(DateTime.class)) {
                        Timestamp sqlTimestamp = resultSet.getTimestamp(column.name());
                        value = new DateTime(sqlTimestamp);
                    } else {
                        value = resultSet.getObject(column.name());
                    }

                    ReflectionGetterSetter.set(model, field, value);
                } catch (SQLException e2) {
                    LOGGER.error("Column name {} not found in resultset for model classname: {}", column.name(), model.getClass().toString());
                }
            }
        });
    }
}
