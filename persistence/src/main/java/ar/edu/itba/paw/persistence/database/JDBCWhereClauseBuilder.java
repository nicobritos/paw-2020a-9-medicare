package ar.edu.itba.paw.persistence.database;

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

    private StringBuilder clause = new StringBuilder();

    public JDBCWhereClauseBuilder where(String columnName, Operation operation, String operand) {
        return this.where(columnName, operation, operand, false);
    }

    public JDBCWhereClauseBuilder where(String columnName, Operation operation, String operand, boolean ignoreCase) {
        if (ignoreCase) {
            operand = operand.toLowerCase();

            this.clause
                    .append("LOWER(")
                    .append(columnName)
                    .append(")");
        } else {
            this.clause.append(columnName);
        }

        this.clause
                .append(operation.getOperation())
                .append(operand);

        return this;
    }

    public JDBCWhereClauseBuilder in(String operand1, Collection<String> operand2) {
        this.clause
                .append(operand1)
                .append(" IN (")
                .append(this.joinCollection(operand2))
                .append(")");

        return this;
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
        this.clause
                .append(columnName)
                .append(" BETWEEN ")
                .append(min)
                .append(" AND ")
                .append(max);
        return this;
    }

    public JDBCWhereClauseBuilder lengthBetween(String columnName, Integer min, Integer max) {
        this.clause
                .append(" LENGTH(")
                .append(columnName)
                .append(") BETWEEN ")
                .append(min)
                .append(" AND ")
                .append(max);
        return this;
    }

    public String getClauseAsString() {
        return this.clause.append(" ").toString();
    }

    @Override
    public String toString() {
        return this.getClauseAsString();
    }

    private String joinCollection(Collection<String> strings) {
        return strings.stream().map(s -> "'" + s + "'").collect(Collectors.joining(", "));
    }
}
