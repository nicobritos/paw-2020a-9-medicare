package ar.edu.itba.paw.persistence.utils.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class JDBCWhereClauseBuilder {
    public enum Operation {
        EQ(" = "),
        NEQ(" <> "),
        LT(" < "),
        LEQ(" <= "),
        GT(" > "),
        GEQ(" >= "),
        LIKE(" LIKE ");

        private String operation;

        Operation(String operation) {
            this.operation = operation;
        }

        public String getOperation() {
            return this.operation;
        }

        @Override
        public String toString() {
            return this.operation;
        }
    }

    public enum ColumnTransformer {
        LENGTH(" LENGTH(", ") "),
        LOWER(" LOWER(", ") ");

        private String prefix;
        private String suffix;

        ColumnTransformer(String prefix, String suffix) {
            this.prefix = prefix;
            this.suffix = suffix;
        }

        public String getPrefix() {
            return this.prefix;
        }

        public String getSuffix() {
            return this.suffix;
        }

        public String toString() {
            return "";
        }
    }

    private StringBuilder clause = new StringBuilder();

    public JDBCWhereClauseBuilder where(String columnName, Operation operation, String paramName) {
        return this.where(columnName, operation, paramName, null);
    }

    public JDBCWhereClauseBuilder where(String columnName, Operation operation, String paramName, ColumnTransformer columnTransformer) {
        if (columnTransformer != null) {
            this.clause
                    .append(columnTransformer.getPrefix())
                    .append(columnName)
                    .append(columnTransformer.getSuffix());
        } else {
            this.clause.append(columnName);
        }

        this.clause
                .append(operation.getOperation())
                .append(paramName);

        return this;
    }

    public JDBCWhereClauseBuilder in(String columnName, Collection<String> paramNames) {
        return this.in(columnName, paramNames, false);
    }

    public JDBCWhereClauseBuilder in(String columnName, String nestedSelectQuery) {
        Collection<String> stringCollection = new ArrayList<>(1);
        stringCollection.add(nestedSelectQuery);
        return this.in(columnName, new ArrayList<>(stringCollection), false);
    }

    public JDBCWhereClauseBuilder notIn(String columnName, Collection<String> paramNames) {
        return this.in(columnName, paramNames, true);
    }

    public JDBCWhereClauseBuilder notIn(String columnName, String nestedSelectQuery) {
        Collection<String> stringCollection = new ArrayList<>(1);
        stringCollection.add(nestedSelectQuery);
        return this.in(columnName, new ArrayList<>(stringCollection), true);
    }

    public JDBCWhereClauseBuilder and() {
        this.clause.append(" AND ");
        return this;
    }

    public JDBCWhereClauseBuilder or() {
        this.clause.append(" OR ");
        return this;
    }

    public JDBCWhereClauseBuilder between(String columnName, Integer min, Integer max) {
        return this.between(columnName, min, max, null);
    }

    public JDBCWhereClauseBuilder between(String columnName, Integer min, Integer max, ColumnTransformer columnTransformer) {
        if (columnTransformer != null) {
            this.clause
                    .append(columnTransformer.getPrefix())
                    .append(columnName)
                    .append(columnTransformer.getSuffix());
        } else {
            this.clause.append(columnName);
        }

        this.clause
                .append(" BETWEEN ")
                .append(min)
                .append(" AND ")
                .append(max);

        return this;
    }

    public String getClauseAsString() {
        return this.clause.toString() + " ";
    }

    @Override
    public String toString() {
        return this.getClauseAsString();
    }

    private String joinCollection(Collection<String> strings) {
        return strings.stream().map(Object::toString).collect(Collectors.joining(", "));
    }

    private JDBCWhereClauseBuilder in(String columnName, Collection<String> paramNames, boolean not) {
        if (paramNames.isEmpty())
            return this;

        this.clause.append(columnName);

        if (not) this.clause.append(" NOT ");

        this.clause
                .append(" IN (")
                .append(this.joinCollection(paramNames))
                .append(")");

        return this;
    }
}
