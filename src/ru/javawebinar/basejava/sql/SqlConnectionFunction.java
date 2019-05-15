package ru.javawebinar.basejava.sql;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface SqlConnectionFunction<R> {
    R apply(Connection connection) throws SQLException;
}
