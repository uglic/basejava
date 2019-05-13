package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.util.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    public final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public int size() {
        return SqlHelper.executeQuery(connectionFactory,
                "SELECT COUNT(uuid) c1 FROM Resume;",
                (preparedStatement) -> {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    } else {
                        throw new StorageException("Error getting resume count");
                    }
                });
    }

    @Override
    public void clear() {
        SqlHelper.execute(connectionFactory,
                "DELETE FROM Contact; DELETE FROM Resume;",
                PreparedStatement::execute);
    }

    @Override
    public void update(Resume resume) {
        if (!isExist(connectionFactory, resume.getUuid())) {
            throw new NotExistStorageException(resume.getUuid());
        }
        SqlHelper.execute(connectionFactory,
                "UPDATE Resume SET full_name = ? WHERE uuid = ?;",
                (preparedStatement) -> {
                    preparedStatement.setString(1, resume.getFullName());
                    preparedStatement.setString(2, resume.getUuid());
                    preparedStatement.execute();
                });
    }

    @Override
    public List<Resume> getAllSorted() {
        return SqlHelper.executeQuery(connectionFactory,
                "SELECT * FROM Resume ORDER BY full_name, uuid;",
                (preparedStatement) -> {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    List<Resume> resumes = new ArrayList<>();
                    while (resultSet.next()) {
                        resumes.add(getFromResultSet(resultSet));
                    }
                    return resumes;
                });
    }

    @Override
    public void save(final Resume resume) {
        if (isExist(connectionFactory, resume.getUuid())) {
            throw new ExistStorageException(resume.getUuid());
        }
        SqlHelper.execute(connectionFactory,
                "INSERT INTO Resume(uuid, full_name) VALUES(?, ?);",
                (preparedStatement) -> {
                    preparedStatement.setString(1, resume.getUuid());
                    preparedStatement.setString(2, resume.getFullName());
                    preparedStatement.execute();
                });
    }

    @Override
    public void delete(final String uuid) {
        if (!isExist(connectionFactory, uuid)) {
            throw new NotExistStorageException(uuid);
        }
        SqlHelper.execute(connectionFactory,
                "DELETE FROM Resume WHERE uuid = ?;",
                (preparedStatement) -> {
                    preparedStatement.setString(1, uuid);
                    preparedStatement.execute();
                });
    }

    @Override
    public Resume get(final String uuid) {
        return SqlHelper.executeQuery(connectionFactory,
                "SELECT uuid, full_name FROM Resume resumeReal WHERE resumeReal.uuid = ?;",
                (preparedStatement) -> {
                    preparedStatement.setString(1, uuid);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        return getFromResultSet(resultSet);
                    } else {
                        throw new NotExistStorageException(uuid);
                    }
                });
    }

    private Resume getFromResultSet(ResultSet resultSet) {
        try {
            return new Resume(
                    resultSet.getString("uuid").trim(),
                    resultSet.getString("full_name"));
        } catch (SQLException e) {
            throw new StorageException(e);
        }

    }

    private boolean isExist(final ConnectionFactory connectionFactory, final String uuid) {
        return SqlHelper.executeQuery(connectionFactory,
                "SELECT uuid FROM Resume WHERE uuid = ?;",
                (preparedStatement) -> {
                    preparedStatement.setString(1, uuid);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    return resultSet.next();
                }
        );
    }
}
