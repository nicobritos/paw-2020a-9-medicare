package ar.edu.itba.paw.persistence.generics;

import ar.edu.itba.paw.interfaces.daos.generic.GenericDao;
import ar.edu.itba.paw.models.GenericModel;
import ar.edu.itba.paw.persistence.utils.Pair;
import ar.edu.itba.paw.persistence.utils.ReflectionGetterSetter;
import ar.edu.itba.paw.persistence.utils.builder.*;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.ColumnTransformer;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.Operation;
import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.Table;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
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
    private String tableName;
    private String primaryKeyName;

    public GenericDaoImpl(DataSource dataSource, Class<M> mClass) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

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
        args.addValue("id", id);

        List<M> list = this.query(queryBuilder.getQueryAsString(), args);
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public Collection<M> findByIds(Collection<I> ids) {
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
    public Collection<M> list() {
        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableName());

        return this.query(queryBuilder.getQueryAsString());
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

    protected List<M> findByField(String columnName, Operation operation, Object value) {
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

    protected List<M> findByFieldIn(String columnName, Collection<?> values) {
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
        System.out.println(query);
        return this.jdbcTemplate.query(query, this.getRowMapper());
    }

    protected List<M> query(String query, MapSqlParameterSource args) {
        return this.jdbcTemplate.query(query, args, this.getRowMapper());
    }

    protected M querySingle(String query) {
        List<M> list = this.jdbcTemplate.query(query, this.getRowMapper());
        return list.size() > 0 ? list.get(0) : null;
    }

    protected M querySingle(String query, MapSqlParameterSource args) {
        List<M> list = this.jdbcTemplate.query(query, args, this.getRowMapper());
        return list.size() > 0 ? list.get(0) : null;
    }

    protected String getTableAlias() {
        return this.getTableName();
    }

    protected String formatColumnFromName(String columnName) {
        return formatColumnFromName(columnName, this.getTableName());
    }

    protected String formatColumnFromAlias(String columnName) {
        return formatColumnFromName(columnName, this.getTableAlias());
    }

    protected abstract RowMapper<M> getRowMapper();

    protected String getTableName() {
        return this.tableName;
    }

    protected String getIdColumnName() {
        return this.primaryKeyName;
    }

    protected Map<String, Pair<String, Object>> getModelColumnsArgumentValue(M model, String prefix) {
        Map<String, Pair<String, Object>> map = new HashMap<>();

        ReflectionGetterSetter.iterateValues(model, Column.class, (field, o) -> {
            Column column = field.getAnnotation(Column.class);
            map.put(column.name(), new Pair<>(":" + prefix + column.name(), o));
        });

        return map;
    }

    private M createOrUpdate(M model, boolean create) {
        String prefix = "_r_";
        Map<String, Pair<String, Object>> columnsArgumentValue = this.getModelColumnsArgumentValue(model, prefix);

        if (!create) columnsArgumentValue.remove(this.getIdColumnName());

        Map<String, String> columnsArguments = new HashMap<>();
        Map<String, Object> argumentsValues = new HashMap<>();
        for (Entry<String, Pair<String, Object>> columnArgumentValue : columnsArgumentValue.entrySet()) {
            columnsArguments.put(columnArgumentValue.getKey(), columnArgumentValue.getValue().getLeft());
            argumentsValues.put(prefix + columnArgumentValue.getKey(), columnArgumentValue.getValue().getRight());
        }

        JDBCQueryBuilder queryBuilder = new JDBCInsertQueryBuilder()
                .into(this.getTableName())
                .values(columnsArguments)
                .returning(JDBCSelectQueryBuilder.ALL);

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValues(argumentsValues);

        return this.querySingle(queryBuilder.getQueryAsString(), parameterSource);
    }

    protected static String formatColumnFromName(String columnName, String tableName) {
        return tableName + "." + columnName;
    }

    protected static <M> M hydrate(Class<M> mClass, ResultSet resultSet) {
        M m;
        try {
            m = mClass.newInstance();
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }

        Table table = mClass.getAnnotation(Table.class);
        String tableName = table.name();
        try {
            try {
                ReflectionGetterSetter.set(m, "id", resultSet.getObject(formatColumnFromName(table.primaryKey(), tableName)));
            } catch (SQLException e) {
                ReflectionGetterSetter.set(m, "id", resultSet.getObject(table.primaryKey()));
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        ReflectionGetterSetter.iterateFields(mClass, Column.class, field -> {
            Column column = field.getAnnotation(Column.class);
            try {
                try {
                    ReflectionGetterSetter.set(m, field, resultSet.getObject(formatColumnFromName(column.name(), tableName)));
                } catch (SQLException e) {
                    ReflectionGetterSetter.set(m, field, resultSet.getObject(column.name()));
                }
            } catch (SQLException e) {
                // TODO
                e.printStackTrace();
            }
        });
        return m;
    }

    protected static <M> String getTableNameFromModel(Class<M> mClass) {
        if (mClass.isAnnotationPresent(Table.class)) {
            return mClass.getAnnotation(Table.class).name();
        }
        return null;
    }
}
