package ru.javawebinar.basejava.util;

import java.sql.SQLException;

public interface SqlExceptionFunction<T, R> {
    R apply(T t) throws SQLException;
}
