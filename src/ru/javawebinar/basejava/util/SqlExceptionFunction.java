package ru.javawebinar.basejava.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlExceptionFunction<R> {
    R apply(PreparedStatement t) throws SQLException;
}
