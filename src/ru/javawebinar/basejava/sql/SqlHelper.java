package ru.javawebinar.basejava.sql;

import org.postgresql.util.PSQLException;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper<R> {
    public R execute(ConnectionFactory connectionFactory, String sql, SqlExceptionFunction<R> function, String uuid) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            return function.apply(preparedStatement);
        } catch (SQLException e) {
            throw convertSqlException(e, uuid);
        }
    }

    public StorageException convertSqlException(SQLException e, String message) {
        if (e instanceof PSQLException) {
            switch (e.getSQLState()) {
                case "23505": //  postgres:23505:unique_violation
                    return new ExistStorageException(message == null ? "unknown" : message);
                default:
                    return new StorageException(e);
            }
        }
        return new StorageException(e);
    }
}

