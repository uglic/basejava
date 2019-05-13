package ru.javawebinar.basejava.util;

import java.sql.SQLException;

public interface SqlExceptionFunction<PreparedStatement, R> {
    R apply(PreparedStatement t) throws SQLException;
}
