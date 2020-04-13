package ar.edu.itba.paw.persistence.generics;

import ar.edu.itba.paw.interfaces.daos.generic.GenericDao;
import ar.edu.itba.paw.models.GenericModel;
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
    protected NamedParameterJdbcTemplate jdbcTemplate;
    private String primaryKeyName;
    private final Class<M> mClass;
    private String tableName;

    public GenericDaoImpl(DataSource dataSource, Class<M> mClass) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.mClass = mClass;

        // Get the table's name and primary key's column's name from the annotation that is
        // (or should be) set in the model's class
        if (mClass.isAnnotationPresent(Table.class)) {
            Table table = mClass.getAnnotation(Table.class);
            this.tableName = table.name();
            this.primaryKeyName = table.primaryKey();
        }
    }

    @Override
    public M findById(I id) {
        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableName())
                .where(new JDBCWhereClauseBuilder()
                        .where(this.formatColumnFromName(this.getIdColumnName()), Operation.EQ, ":id")
                );

        MapSqlParameterSource args = new MapSqlParameterSource();
        // Note that "parameterName" should NOT be preceded by a semicolon (as it is in the query)
        args.addValue("id", id);

        List<M> list = this.query(queryBuilder.getQueryAsString(), args);
        return list.size() > 0 ? list.get(0) : null;
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
                .from(this.getTableName())
                .where(new JDBCWhereClauseBuilder()
                        .in(this.formatColumnFromName(this.getIdColumnName()), idsParameters)
                );

        MapSqlParameterSource args = new MapSqlParameterSource();
        args.addValues(parameters);

        return this.query(queryBuilder.getQueryAsString(), args);
    }

    @Override
    public M create(M model) {
        return this.createOrUpdate(model, true);
    }

    @Override
    public void update(M model) {
        this.createOrUpdate(model, false);
    }

    @Override
    public void remove(M model) {
        this.remove(model.getId());
    }

    @Override
    public void remove(I id) {
        JDBCQueryBuilder queryBuilder = new JDBCDeleteQueryBuilder()
                .from(this.getTableName())
                .where(new JDBCWhereClauseBuilder()
                        .where(this.formatColumnFromName(this.getIdColumnName()), Operation.EQ, ":id")
                );

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);

        this.query(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public List<M> list() {
        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableName());

        return this.query(queryBuilder.getQueryAsString());
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
                .from(this.getTableName())
                .where(new JDBCWhereClauseBuilder()
                        .where(this.formatColumnFromName(columnName), operation, ":argument")
                );

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("argument", value);

        return this.query(queryBuilder.getQueryAsString(), parameterSource);
    }

    protected List<M> findByFieldIgnoreCase(String columnName, Operation operation, String value) {
        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableName())
                .where(new JDBCWhereClauseBuilder()
                        .where(this.formatColumnFromName(columnName), operation, ":argument", ColumnTransformer.LOWER)
                );

        value = value.toLowerCase();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("argument", value);

        return this.query(queryBuilder.getQueryAsString(), parameterSource);
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
                .from(this.getTableName())
                .where(new JDBCWhereClauseBuilder()
                        .in(this.formatColumnFromName(columnName), parameters.keySet())
                );

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(parameters);

        return this.query(queryBuilder.getQueryAsString(), parameterSource);
    }

    protected List<M> query(String query) {
        return this.jdbcTemplate.query(query, this.getRowMapper());
    }

    protected List<M> query(String query, MapSqlParameterSource args) {
        return this.jdbcTemplate.query(query, args, this.getRowMapper());
    }

    /**
     * Runs a query handled by a specific handler
     * @param query the query
     * @param args the arguments (can be empty)
     * @param callbackHandler the ResultSet handler
     */
    protected void query(String query, MapSqlParameterSource args, RowCallbackHandler callbackHandler) {
        this.jdbcTemplate.query(query, args, callbackHandler);
    }

    protected M querySingle(String query) {
        List<M> list = this.jdbcTemplate.query(query, this.getRowMapper());
        return list.size() > 0 ? list.get(0) : null;
    }

    protected M querySingle(String query, MapSqlParameterSource args) {
        List<M> list = this.jdbcTemplate.query(query, args, this.getRowMapper());
        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * It returns the table name
     * Can be overwritten to return a table alias
     * @return the table alias
     */
    protected String getTableAlias() {
        return this.getTableName();
    }

    protected String formatColumnFromName(String columnName) {
        return formatColumnFromName(columnName, this.getTableName());
    }

    protected String formatColumnFromAlias(String columnName) {
        return formatColumnFromName(columnName, this.getTableAlias());
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
     * It sets all its fields using reflection (@link #
     * @param resultSet
     * @return
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
//                e.printStackTrace();
            }
        } catch (Exception e) {
//            e.printStackTrace();
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
            ReflectionGetterSetter.set(m, field, otherInstances);
        });
        ReflectionGetterSetter.iterateFields(this.mClass, ManyToOne.class, field -> {
            // Esto es como tratar un OneToOne
            ManyToOne relation = field.getAnnotation(ManyToOne.class);
            this.processOneToOneRelation(resultSet, m, field, relation.name());
        });
        ReflectionGetterSetter.iterateFields(this.mClass, ManyToMany.class, field -> {
            // Esto es como tratar un OneToMany pero en una tabla diferente
            ManyToMany relation = field.getAnnotation(ManyToMany.class);
            Class<? extends GenericModel<Object>> otherClass;
            try {
                otherClass = (Class<? extends GenericModel<Object>>) relation.className();
            } catch (ClassCastException e) {
                // TODO
                throw new RuntimeException(e);
            }

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
            this.query(
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
            ReflectionGetterSetter.set(m, field, otherInstances);
        });

        return m;
    }

    private Map<String, Pair<String, Object>> getModelColumnsArgumentValue(M model, String prefix, boolean checkRequired) {
        Map<String, Pair<String, Object>> map = new HashMap<>();

        // TODO: Save relations (one-many, many-many, one-one)
        // Separamos para mas performance a costa de codigo duplicado
        if (checkRequired) {
            ReflectionGetterSetter.iterateValues(model, Column.class, (field, o) -> {
                Column column = field.getAnnotation(Column.class);
                if (column.required() && o == null)
                    throw new IllegalStateException("This field is marked as required but its value is null");

                map.put(column.name(), new Pair<>(":" + prefix + column.name(), o));
            });
        } else {
            ReflectionGetterSetter.iterateValues(model, Column.class, (field, o) -> {
                Column column = field.getAnnotation(Column.class);
                map.put(column.name(), new Pair<>(":" + prefix + column.name(), o));
            });
        }

        return map;
    }

    private M createOrUpdate(M model, boolean create) {
        String prefix = "_r_";
        Map<String, Pair<String, Object>> columnsArgumentValue = this.getModelColumnsArgumentValue(model, prefix, true);

        Map<String, String> columnsArguments = new HashMap<>();
        Map<String, Object> argumentsValues = new HashMap<>();
        for (Entry<String, Pair<String, Object>> columnArgumentValue : columnsArgumentValue.entrySet()) {
            columnsArguments.put(columnArgumentValue.getKey(), columnArgumentValue.getValue().getLeft());
            argumentsValues.put(prefix + columnArgumentValue.getKey(), columnArgumentValue.getValue().getRight());
        }

        JDBCQueryBuilder queryBuilder;
        if (create) {
             queryBuilder = new JDBCInsertQueryBuilder()
                    .into(this.getTableName())
                    .values(columnsArguments)
                    .returning(JDBCSelectQueryBuilder.ALL);
        } else {
            String argumentName = prefix + "id";

            queryBuilder = new JDBCUpdateQueryBuilder()
                    .update(this.getTableName())
                    .values(columnsArguments)
                    .where(new JDBCWhereClauseBuilder()
                            .where(formatColumnFromName(this.getIdColumnName(), this.getTableName()), Operation.EQ, ":" + argumentName)
                    )
                    .returning(JDBCSelectQueryBuilder.ALL);

            argumentsValues.put(argumentName, model.getId());
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(argumentsValues);

        return this.querySingle(queryBuilder.getQueryAsString(), parameterSource);
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
                otherInstance = otherDao.findById(resultSet.getObject(formatColumnFromName(columnName, this.tableName)));
            } catch (SQLException e) {
                otherInstance = otherDao.findById(resultSet.getObject(columnName));
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
