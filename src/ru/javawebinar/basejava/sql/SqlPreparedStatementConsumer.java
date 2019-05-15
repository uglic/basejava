package ru.javawebinar.basejava.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface SqlPreparedStatementConsumer {
    void accept(PreparedStatement preparedStatement) throws SQLException;
}
