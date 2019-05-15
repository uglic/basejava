package ru.javawebinar.basejava.sql;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface SqlConnectionConsumer {
    void accept(Connection connection) throws SQLException;
}
