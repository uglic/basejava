package ru.javawebinar.basejava.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface SqlExceptionFunction<R> {
    R apply(PreparedStatement t) throws SQLException;
}
