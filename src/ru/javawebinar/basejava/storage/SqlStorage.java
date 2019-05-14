package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public int size() {
        return sqlHelper.<Integer>execute("SELECT COUNT(uuid) c1 FROM Resume;",
                (ps) -> {
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        return rs.getInt(1);
                    } else {
                        throw new StorageException("Error getting resume count");
                    }
                }, null
        );
    }

    @Override
    public void clear() {
        sqlHelper.<Boolean>execute("DELETE FROM Contact; DELETE FROM Resume;",
                PreparedStatement::execute, null);
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.<Boolean>execute("UPDATE Resume SET full_name = ? WHERE uuid = ?;",
                (ps) -> {
                    ps.setString(1, resume.getFullName());
                    ps.setString(2, resume.getUuid());
                    if (ps.executeUpdate() == 0) {
                        throw new NotExistStorageException(resume.getUuid());
                    } else {
                        return true;
                    }
                }, resume.getUuid());
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.execute("SELECT * FROM Resume ORDER BY full_name, uuid;",
                (ps) -> {
                    ResultSet rs = ps.executeQuery();
                    List<Resume> resumes = new ArrayList<>();
                    while (rs.next()) {
                        resumes.add(getResumeFromResultSet(rs));
                    }
                    return resumes;
                }, null);
    }

    @Override
    public void save(final Resume resume) {
        sqlHelper.<Boolean>execute("INSERT INTO Resume(uuid, full_name) VALUES(?, ?);",
                (ps) -> {
                    ps.setString(1, resume.getUuid());
                    ps.setString(2, resume.getFullName());
                    return ps.execute();
                }, resume.getUuid());
    }

    @Override
    public void delete(final String uuid) {
        sqlHelper.<Boolean>execute("DELETE FROM Resume WHERE uuid = ?;",
                (ps) -> {
                    ps.setString(1, uuid);
                    if (ps.executeUpdate() == 0) {
                        throw new NotExistStorageException(uuid);
                    } else {
                        return true;
                    }
                }, uuid);
    }

    @Override
    public Resume get(final String uuid) {
        return sqlHelper.execute(
                "SELECT uuid, full_name FROM Resume resumeReal WHERE resumeReal.uuid = ?;",
                (ps) -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        return getResumeFromResultSet(rs);
                    } else {
                        throw new NotExistStorageException(uuid);
                    }
                }, uuid);
    }

    private Resume getResumeFromResultSet(ResultSet resultSet) throws SQLException {
        return new Resume(
                resultSet.getString("uuid"),
                resultSet.getString("full_name"));
    }
}
