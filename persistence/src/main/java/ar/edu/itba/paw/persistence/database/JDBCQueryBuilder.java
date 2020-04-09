package ar.edu.itba.paw.persistence.database;

import java.util.Collection;
import java.util.List;

public abstract class JDBCQueryBuilder {
    protected List<Object> arguments;

    public List<Object> getArguments() {
        return this.arguments;
    }

    public JDBCQueryBuilder addArgument(Object o) {
        this.arguments.add(o);
        return this;
    }

    @Override
    public String toString() {
        return this.getQueryAsString();
    }

    public abstract String getQueryAsString();

    protected String joinStrings(Collection<String> strings) {
        return String.join(",", strings);
    }
}
