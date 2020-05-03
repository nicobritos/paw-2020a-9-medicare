package ar.edu.itba.paw.persistence.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapperAlias<R> {
    R mapRow(String columnPrefix, ResultSet resultSet) throws SQLException;
}
