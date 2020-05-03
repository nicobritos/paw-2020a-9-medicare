package ar.edu.itba.paw.persistence.utils.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class JDBCInsertQueryBuilder extends JDBCQueryBuilder {
    public static final String DEFAULT_COLUMN_VALUE = "DEFAULT";

    public static enum OnConflictPolicy { NOTHING, UPDATE }

    private Map<String, String> values = new HashMap<>();

    private OnConflictPolicy conflictPolicy;
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

    public JDBCInsertQueryBuilder onConflict(OnConflictPolicy conflictPolicy) {
        this.conflictPolicy = conflictPolicy;
        return this;
    }

    @Override
    public String getQueryAsString() {
        StringBuilder stringBuilder = new StringBuilder(
                "INSERT INTO " +
                this.table +
                this.getValuesAsString()
        );
        if (this.conflictPolicy != null) {
            stringBuilder
                    .append(" ON CONFLICT DO ")
                    .append(this.conflictPolicy.name());
        }
        return stringBuilder.toString();
    }

    @Override
    protected String getTable() {
        return this.table;
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
