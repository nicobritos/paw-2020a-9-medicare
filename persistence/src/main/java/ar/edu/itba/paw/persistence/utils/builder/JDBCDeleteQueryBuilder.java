package ar.edu.itba.paw.persistence.utils.builder;

import java.util.LinkedList;
import java.util.List;

public class JDBCDeleteQueryBuilder extends JDBCQueryBuilder {
    public static final String DEFAULT_COLUMN_VALUE = "DEFAULT";

    private List<String> tables = new LinkedList<>();
    private JDBCWhereClauseBuilder whereClauseBuilder;
    private String table;

    public JDBCDeleteQueryBuilder from(String tableName) {
        this.table = tableName;
        return this;
    }

    public JDBCDeleteQueryBuilder from(String tableName, String alias) {
        return this.from(tableName + " AS " + alias);
    }

    public JDBCDeleteQueryBuilder using(String tableName) {
        this.tables.add(tableName);
        return this;
    }

    public JDBCDeleteQueryBuilder where(JDBCWhereClauseBuilder whereClauseBuilder) {
        this.whereClauseBuilder = whereClauseBuilder;
        return this;
    }

    @Override
    public String getQueryAsString() {
        StringBuilder stringBuilder = new StringBuilder("DELETE FROM ");
        stringBuilder.append(this.table);

        if (this.tables.size() > 0) {
            stringBuilder
                    .append(" USING ")
                    .append(this.joinStrings(this.tables));
        }
        if (this.whereClauseBuilder != null) {
            stringBuilder
                    .append(" WHERE ")
                    .append(this.whereClauseBuilder.getClauseAsString());
        } else {
            throw new IllegalStateException("No where clause defined. Is this an error?");
        }

        stringBuilder.append(";");

        return stringBuilder.toString();
    }

    @Override
    protected String getTable() {
        return this.table;
    }
}
