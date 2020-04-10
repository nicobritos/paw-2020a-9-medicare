package ar.edu.itba.paw.persistence.utils.builder;

import java.util.Collection;

public abstract class JDBCQueryBuilder {
    @Override
    public String toString() {
        return this.getQueryAsString();
    }

    public abstract String getQueryAsString();

    protected String joinStrings(Collection<String> strings) {
        return String.join(",", strings);
    }
}
