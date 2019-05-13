package ru.javawebinar.basejava.storage;

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
        return new SqlHelper<Integer>().execute(connectionFactory,
                "SELECT COUNT(uuid) c1 FROM Resume;",
                (preparedStatement) -> {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    } else {
                        throw new StorageException("Error getting resume count");
                    }
                }, null);
    }

    @Override
    public void clear() {
        new SqlHelper<Boolean>().execute(connectionFactory,
                "DELETE FROM Contact; DELETE FROM Resume;",
                PreparedStatement::execute, null);
    }

    @Override
    public void update(Resume resume) {
        new SqlHelper<Boolean>().execute(connectionFactory,
                "UPDATE Resume SET full_name = ? WHERE uuid = ?;",
                (preparedStatement) -> {
                    preparedStatement.setString(1, resume.getFullName());
                    preparedStatement.setString(2, resume.getUuid());
                    if (preparedStatement.executeUpdate() == 1) {
                        return true;
                    } else {
                        throw new NotExistStorageException(resume.getUuid());
                    }
                }, resume.getUuid());
    }

    @Override
    public List<Resume> getAllSorted() {
        return new SqlHelper<List<Resume>>().execute(connectionFactory,
                "SELECT * FROM Resume ORDER BY full_name, uuid;",
                (preparedStatement) -> {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    List<Resume> resumes = new ArrayList<>();
                    while (resultSet.next()) {
                        resumes.add(getFromResultSet(resultSet));
                    }
                    return resumes;
                }, null);
    }

    @Override
    public void save(final Resume resume) {
        new SqlHelper<Boolean>().execute(connectionFactory,
                "INSERT INTO Resume(uuid, full_name) VALUES(?, ?);",
                (preparedStatement) -> {
                    preparedStatement.setString(1, resume.getUuid());
                    preparedStatement.setString(2, resume.getFullName());
                    return preparedStatement.execute();
                }, resume.getUuid());
    }

    @Override
    public void delete(final String uuid) {
        new SqlHelper<Boolean>().execute(connectionFactory,
                "DELETE FROM Resume WHERE uuid = ?;",
                (preparedStatement) -> {
                    preparedStatement.setString(1, uuid);
                    if (preparedStatement.executeUpdate() == 1) {
                        return true;
                    } else {
                        throw new NotExistStorageException(uuid);
                    }
                }, uuid);
    }

    @Override
    public Resume get(final String uuid) {
        return new SqlHelper<Resume>().execute(connectionFactory,
                "SELECT uuid, full_name FROM Resume resumeReal WHERE resumeReal.uuid = ?;",
                (preparedStatement) -> {
                    preparedStatement.setString(1, uuid);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        return getFromResultSet(resultSet);
                    } else {
                        throw new NotExistStorageException(uuid);
                    }
                }, uuid);
    }

    private Resume getFromResultSet(ResultSet resultSet) throws SQLException {
        return new Resume(
                resultSet.getString("uuid"),
                resultSet.getString("full_name"));
    }
}
