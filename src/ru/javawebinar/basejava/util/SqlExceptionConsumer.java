package ru.javawebinar.basejava.util;

import java.sql.SQLException;

public interface SqlExceptionConsumer<T> {
    void accept(T t) throws SQLException;
}
