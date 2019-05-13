package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.sql.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper<R> {
    public R execute(ConnectionFactory connectionFactory, String sql, SqlExceptionFunction<PreparedStatement, R> function) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            return function.apply(preparedStatement);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}

