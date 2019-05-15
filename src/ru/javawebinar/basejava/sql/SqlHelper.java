package ru.javawebinar.basejava.sql;

import org.postgresql.util.PSQLException;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public <R> R execute(String sql, SqlPreparedStatementFunction<R> function, String uuid) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT)) {
            return function.apply(ps);
        } catch (SQLException e) {
            throw convertSqlException(e, uuid);
        }
    }

    public void transactionalExecute(SqlConnectionConsumer function, String uuid) {
        try (Connection conn = connectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);
                function.accept(conn);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw convertSqlException(e, uuid);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public static StorageException convertSqlException(SQLException e, String message) {
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

