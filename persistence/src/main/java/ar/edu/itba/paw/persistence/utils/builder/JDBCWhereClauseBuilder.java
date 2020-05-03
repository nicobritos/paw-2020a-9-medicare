package ar.edu.itba.paw.persistence.utils.builder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class JDBCWhereClauseBuilder {
    private static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-mm-dd HH:mm:ss");

    public enum Operation {
        EQ(" = ", (o1, o2) -> (o1 == null && o2 == null) || (o1 != null && o1.equals(o2))),
        NEQ(" <> ", (o1, o2) -> (o1 == null && o2 != null) || (o1 != null && !o1.equals(o2))),
        LT(" < ", (o1, o2) -> Double.compare((Double) o1, (Double) o2) < 0),
        LEQ(" <= ", (o1, o2) -> Double.compare((Double) o1, (Double) o2) <= 0),
        GT(" > ", (o1, o2) -> Double.compare((Double) o1, (Double) o2) > 0),
        GEQ(" >= ", (o1, o2) -> Double.compare((Double) o1, (Double) o2) >= 0),
        LIKE(" LIKE ", (o1, o2) -> o1.toString().equals(o2.toString()));

        private String operation;
        private BiPredicate<Object, Object> predicate;

        Operation(String operation, BiPredicate<Object, Object> predicate) {
            this.operation = operation;
            this.predicate = predicate;
        }

        public String getOperation() {
            return this.operation;
        }

        public boolean operate(Object o1, Object o2) {
            return this.predicate.test(o1, o2);
        }

        @Override
        public String toString() {
            return this.operation;
        }
    }

    public enum ColumnTransformer {
        LENGTH(" LENGTH(", ") "),
        LOWER(" unaccent(LOWER(", "))"),
        DAY("EXTRACT(DAY FROM ",")"),
        MONTH("EXTRACT(MONTH FROM ",")"),
        YEAR("EXTRACT(YEAR FROM ",")");

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
        if (this.clause.length() > 0)
            this.clause.append(" AND ");
        return this;
    }

    public JDBCWhereClauseBuilder and(JDBCWhereClauseBuilder whereClauseBuilder) {
        String otherWhereClause = whereClauseBuilder.getClauseAsString();
        if (otherWhereClause.isEmpty())
            return this;

        if (this.clause.length() > 0)
            this.clause.append(" AND ");
        this.clause
                .append(" (")
                .append(otherWhereClause)
                .append(") ");
        return this;
    }

    public JDBCWhereClauseBuilder or() {
        if (this.clause.length() > 0)
            this.clause.append(" OR ");
        return this;
    }

    public JDBCWhereClauseBuilder or(JDBCWhereClauseBuilder whereClauseBuilder) {
        String otherWhereClause = whereClauseBuilder.getClauseAsString();
        if (otherWhereClause.isEmpty())
            return this;

        if (this.clause.length() > 0)
            this.clause.append(" OR ");
        this.clause
                .append(" (")
                .append(otherWhereClause)
                .append(") ");
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

    public JDBCWhereClauseBuilder between(String columnName, DateTime fromDate, DateTime toDate) {
        return this.between(columnName, fromDate, toDate, null);
    }

    public JDBCWhereClauseBuilder between(String columnName, DateTime fromDate, DateTime toDate, ColumnTransformer columnTransformer) {
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
                .append(dateTimeFormatter.print(fromDate))
                .append(" AND ")
                .append(dateTimeFormatter.print(toDate));

        return this;
    }

    public String getClauseAsString() {
        return this.clause.toString().isEmpty()? "" : this.clause.toString()+ " ";
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
