package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.sql.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper<R> {
    public R execute(ConnectionFactory connectionFactory, String sql, SqlExceptionFunction<R> function, String uuid) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            return function.apply(preparedStatement);
        } catch (SQLException e) {
            switch (e.getSQLState()) {
                case "23505": //  postgres:23505:unique_violation
                    throw new ExistStorageException(uuid == null ?"unknown": uuid);
                default:
                    throw new StorageException(e);
            }
        }
    }
}

