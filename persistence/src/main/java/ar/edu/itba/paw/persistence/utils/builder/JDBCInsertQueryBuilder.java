package ar.edu.itba.paw.persistence.utils.builder;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

public class JDBCInsertQueryBuilder extends JDBCQueryBuilder {
    public static final String DEFAULT_COLUMN_VALUE = "DEFAULT";

    private Map<String, String> values = new HashMap<>();
    private Collection<String> returning = new LinkedList<>();

    private String table;

    public JDBCInsertQueryBuilder into(String tableName) {
        this.table = tableName;
        return this;
    }

    public JDBCInsertQueryBuilder into(String tableName, String alias) {
        return this.into(tableName + " AS " + alias);
    }

    public JDBCInsertQueryBuilder value(String columnName, String paramName) {
        this.values.put(columnName, paramName);
        return this;
    }

    public JDBCInsertQueryBuilder values(Map<String, String> columnNamesParameters) {
        this.values.putAll(columnNamesParameters);
        return this;
    }

    public JDBCInsertQueryBuilder returning(String columnName) {
        this.returning.add(columnName);
        return this;
    }

    public JDBCInsertQueryBuilder returning(Collection<String> columnNames) {
        this.returning.addAll(columnNames);
        return this;
    }

    @Override
    public String getQueryAsString() {
        StringBuilder stringBuilder = new StringBuilder("INSERT INTO ");
        stringBuilder
                .append(this.table)
                .append(this.getValuesAsString());

        if (this.returning.size() > 0) {
            stringBuilder
                    .append(" RETURNING ")
                    .append(this.joinStrings(this.returning));
        }

        stringBuilder.append(";");

        return stringBuilder.toString();
    }

    protected String getValuesAsString() {
        StringBuilder keyStringBuilder = new StringBuilder();
        StringBuilder valueStringBuilder = new StringBuilder();

        for (Entry<String, String> pair : this.values.entrySet()) {
            keyStringBuilder.append(pair.getKey());
            valueStringBuilder.append(pair.getValue());
        }

        return " (" + keyStringBuilder.toString() + ") VALUES (" + valueStringBuilder.toString() + ") ";
    }
}
