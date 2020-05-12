package ar.edu.itba.paw.persistence.utils.builder;

import ar.edu.itba.paw.models.GenericModel;
import ar.edu.itba.paw.persistence.utils.ReflectionGetterSetter;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.Operation;
import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.OrderCriteria;
import ar.edu.itba.paw.persistenceAnnotations.Table;

import java.util.*;

public class JDBCSelectQueryBuilder extends JDBCQueryBuilder {
    public static final String ALL = " * ";

    public enum JoinType {
        INNER(" "),
        LEFT(" LEFT OUTER "),
        RIGHT(" RIGHT OUTER "),
        FULL_OUTER(" FULL OUTER "),
        CROSS_OUTER(" CROSS OUTER ");

        private final String joinType;

        JoinType(String joinType) {
            this.joinType = joinType;
        }

        public String getJoinType() {
            return this.joinType;
        }

        @Override
        public String toString() {
            return this.joinType;
        }
    }

    private final Map<String, Class<? extends GenericModel<?>>> joinColumns = new HashMap<>();
    private final SortedSet<OrderBy> orderBy = new TreeSet<>();
    private final List<String> columns = new LinkedList<>();
    private final List<String> joins = new LinkedList<>();

    private JDBCWhereClauseBuilder whereClauseBuilder;
    private JDBCSelectQueryBuilder fromQueryBuilder;
    private Class<? extends GenericModel<?>> mClass;
    private boolean selectAll;
    private boolean distinct;
    private String table;
    private String alias;
    private int limit;
    private int offset;

    public JDBCSelectQueryBuilder selectAll(Class<? extends GenericModel<?>> mClass) {
        this.selectAll = true;
        this.mClass = mClass;
        return this;
    }

    public JDBCSelectQueryBuilder select(String columnName) {
        this.columns.add(columnName);
        return this;
    }

    public JDBCSelectQueryBuilder from(String tableName) {
        return this.from(tableName, tableName);
    }

    public JDBCSelectQueryBuilder from(String tableName, String alias) {
        this.table = tableName + " AS " + alias;
        this.alias = alias;
        return this;
    }

    public JDBCSelectQueryBuilder from(JDBCSelectQueryBuilder selectQueryBuilder) {
        this.from(selectQueryBuilder.getTable(), selectQueryBuilder.getAlias());
        this.fromQueryBuilder = selectQueryBuilder;
        return this;
    }

    public String getAlias() {
        return this.alias;
    }

    public JDBCSelectQueryBuilder join(String columnLeft, String tableRight, String columnRight, Class<? extends GenericModel<?>> mClass) {
        return this.join(columnLeft, Operation.EQ, tableRight, columnRight, JoinType.INNER, mClass);
    }

    public JDBCSelectQueryBuilder join(String columnLeft, String tableRight, String columnRight, JoinType joinType, Class<? extends GenericModel<?>> mClass) {
        return this.join(columnLeft, Operation.EQ, tableRight, columnRight, joinType, mClass);
    }

    public JDBCSelectQueryBuilder join(String columnLeft, Operation operation, String tableRight, String columnRight, Class<? extends GenericModel<?>> mClass) {
        return this.join(columnLeft, operation, tableRight, columnRight, JoinType.INNER, mClass);
    }

    public JDBCSelectQueryBuilder join(String columnLeft, Operation operation, String tableRight, String columnRight, JoinType joinType, Class<? extends GenericModel<?>> mClass) {
        return this.join(this.alias, columnLeft, operation, tableRight, columnRight, joinType, mClass);
    }

    public JDBCSelectQueryBuilder join(String tableLeft, String columnLeft, String tableRight, String columnRight, Class<? extends GenericModel<?>> mClass) {
        return this.join(tableLeft, columnLeft, Operation.EQ, tableRight, columnRight, JoinType.INNER, mClass);
    }

    public JDBCSelectQueryBuilder join(String tableLeft, String columnLeft, String tableRight, String columnRight, JoinType joinType, Class<? extends GenericModel<?>> mClass) {
        return this.join(tableLeft, columnLeft, Operation.EQ, tableRight, columnRight, joinType, mClass);
    }

    public JDBCSelectQueryBuilder join(String tableLeft, String columnLeft, Operation operation, String tableRight, String columnRight, Class<? extends GenericModel<?>> mClass) {
        return this.join(tableLeft, columnLeft, operation, tableRight, columnRight, JoinType.INNER, mClass);
    }

    public JDBCSelectQueryBuilder join(String tableLeft, String columnLeft, Operation operation, String tableRight, String columnRight, JoinType joinType, Class<? extends GenericModel<?>> mClass) {
        return this.joinAlias(tableLeft, columnLeft, operation, tableRight, tableRight, columnRight, joinType, mClass);
    }

    public JDBCSelectQueryBuilder joinAlias(String columnLeft, String tableRight, String aliasRight, String columnRight, Class<? extends GenericModel<?>> mClass) {
        return this.joinAlias(this.alias, columnLeft, Operation.EQ, tableRight, aliasRight, columnRight, JoinType.INNER, mClass);
    }

    public JDBCSelectQueryBuilder joinAlias(String columnLeft, String tableRight, String aliasRight, String columnRight, JoinType joinType, Class<? extends GenericModel<?>> mClass) {
        return this.joinAlias(this.alias, columnLeft, Operation.EQ, tableRight, aliasRight, columnRight, joinType, mClass);
    }

    public JDBCSelectQueryBuilder joinAlias(String tableLeft, String columnLeft, String tableRight, String aliasRight, String columnRight, JoinType joinType, Class<? extends GenericModel<?>> mClass) {
        return this.joinAlias(tableLeft, columnLeft, Operation.EQ, tableRight, aliasRight, columnRight, joinType, mClass);
    }

    public JDBCSelectQueryBuilder joinAlias(String tableLeft, String columnLeft, Operation operation, String tableRight, String aliasRight, String columnRight, JoinType joinType, Class<? extends GenericModel<?>> mClass) {
        String string = joinType.getJoinType() +
                " JOIN " +
                tableRight +
                " " +
                aliasRight +
                " ON " +
                tableLeft + "." + columnLeft +
                operation.getOperation() +
                aliasRight + "." + columnRight;

        if (mClass != null)
            this.joinColumns.put(aliasRight, mClass);

        this.joins.add(string);
        return this;
    }

    public JDBCSelectQueryBuilder orderBy(String columnName, OrderCriteria orderCriteria, int priority) {
        this.orderBy.add(new OrderBy(columnName, orderCriteria, priority));
        return this;
    }

    public boolean hasOrderBy() {
        return !this.orderBy.isEmpty();
    }

    public JDBCSelectQueryBuilder distinct() {
        this.distinct = true;
        return this;
    }

    public JDBCSelectQueryBuilder limit(int limit) {
        this.limit = limit;
        return this;
    }

    public boolean hasLimit() {
        return this.limit > 0;
    }

    public JDBCSelectQueryBuilder offset(int offset) {
        this.offset = offset;
        return this;
    }

    public boolean hasOffset() {
        return this.offset > 0;
    }

    public JDBCSelectQueryBuilder where(JDBCWhereClauseBuilder whereClauseBuilder) {
        if(whereClauseBuilder.toString().isEmpty()) {
            this.whereClauseBuilder = null;
        } else {
            this.whereClauseBuilder = whereClauseBuilder;
        }
        return this;
    }

    @Override
    public String getQueryAsString() {
        return this.getQueryAsSubqueryString(false) + ";";
    }

    @Override
    public String getTable() {
        return this.table;
    }

    protected List<String> getColumnsAsList() {
        if (this.mClass != null && !this.joinColumns.containsKey(this.alias))
            this.joinColumns.put(this.alias, this.mClass);

        List<String> columns = new LinkedList<>();
        for (Map.Entry<String, Class<? extends GenericModel<?>>> entry : this.joinColumns.entrySet()) {
            Table table = entry.getValue().getAnnotation(Table.class);
            columns.add(entry.getKey() + "." + table.primaryKey() + " AS " + "\"" + entry.getKey() + "." + table.primaryKey() + "\"");
            ReflectionGetterSetter.iterateFields(entry.getValue(), Column.class, field -> {
                Column column = field.getAnnotation(Column.class);
                columns.add(entry.getKey() + "." + column.name() + " AS " + "\"" + entry.getKey() + "." + column.name() + "\"");
            });
        }
        return columns;
    }

    protected String getQueryAsSubqueryString(boolean onlyColumnsFromTable) {
        StringBuilder stringBuilder = new StringBuilder("SELECT ");

        if (this.distinct) stringBuilder.append(" DISTINCT ");

        if (onlyColumnsFromTable) {
            stringBuilder
                    .append(this.alias)
                    .append(".*");
        } else {
            if (this.columns.size() > 0) {
                stringBuilder.append(this.joinStrings(this.columns));
            } else if (this.selectAll || this.fromQueryBuilder != null) {
                stringBuilder.append(this.generateColumns());
            } else {
                return null;
            }
        }

        stringBuilder.append(" FROM ");
        if (this.fromQueryBuilder != null) {
            stringBuilder
                    .append(" ( ")
                    .append(this.fromQueryBuilder.getQueryAsSubqueryString(true))
                    .append(" ) AS ")
                    .append(this.fromQueryBuilder.getAlias());
        } else {
            stringBuilder.append(this.table);
        }

        if (this.joins.size() > 0) {
            stringBuilder.append(this.joinStrings(this.joins, " "));
        }

        if (this.whereClauseBuilder != null) {
            stringBuilder
                    .append(" WHERE ")
                    .append(this.whereClauseBuilder.getClauseAsString());
        }

        if (!this.orderBy.isEmpty()) {
            stringBuilder
                    .append(" ORDER BY ")
                    .append(this.generateOrderBy());
        }

        if (this.limit > 0) {
            stringBuilder
                    .append(" LIMIT ")
                    .append(this.limit);
        }

        if (this.offset > 0){
            stringBuilder
                    .append(" OFFSET ")
                    .append(this.offset);
        }

        return stringBuilder.toString();
    }

    private String generateColumns() {
        List<String> columns = this.getColumnsAsList();
        if (this.fromQueryBuilder != null) {
            columns.addAll(this.fromQueryBuilder.getColumnsAsList());
        }
        return this.joinStrings(columns);
    }

    private String generateOrderBy() {
        List<String> strings = new LinkedList<>();
        for (OrderBy orderBy : this.orderBy) {
            strings.add(orderBy.getColumnName() + " " + orderBy.getCriteria().getCriteria());
        }
        return this.joinStrings(strings, ", ");
    }

    private static class OrderBy implements Comparable<OrderBy> {
        private final OrderCriteria criteria;
        private final String columnName;
        private final int priority;

        public OrderBy(String columnName, OrderCriteria criteria, int priority) {
            this.columnName = columnName;
            this.criteria = criteria;
            this.priority = priority;
        }

        public OrderCriteria getCriteria() {
            return this.criteria;
        }

        public String getColumnName() {
            return this.columnName;
        }

        public int getPriority() {
            return this.priority;
        }

        @Override
        public int compareTo(OrderBy o) {
            if (o == null)
                throw new NullPointerException();
            return Integer.compare(this.priority, o.priority);
        }
    }
}
