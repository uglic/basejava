package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Contact;
import ru.javawebinar.basejava.model.ContactTypes;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;
import ru.javawebinar.basejava.sql.SqlPreparedStatementFunction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    private enum DataChangeType {
        UPDATE, INSERT
    }

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public int size() {
        return sqlHelper.<Integer>execute("SELECT COUNT(uuid) c1 FROM Resume;",
                (stmt) -> {
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        return rs.getInt(1);
                    } else {
                        throw new StorageException("Error getting resume count");
                    }
                }, null);
    }

    @Override
    public void clear() {
        sqlHelper.<Boolean>execute("DELETE FROM Contact; DELETE FROM Resume;",
                PreparedStatement::execute, null
        );
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.<Boolean>transactionalExecute(conn -> {
            changeResumeTransactional(conn, resume, DataChangeType.UPDATE);
            return null;
        }, resume.getUuid());
    }

    @Override
    public void save(final Resume resume) {
        sqlHelper.<Boolean>transactionalExecute(conn -> {
            changeResumeTransactional(conn, resume, DataChangeType.INSERT);
            return null;
        }, resume.getUuid());
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.execute(getSqlGet() + " ORDER BY full_name, uuid",
                (stmt) -> {
                    ResultSet rs = stmt.executeQuery();
                    List<Resume> resumes = new ArrayList<>();
                    Resume resume;
                    if (rs.next()) {
                        boolean hasNext;
                        do {
                            resume = getResumeHeadFromRs(rs);
                            hasNext = fillResumeFromRs(resume, rs);
                            resumes.add(resume);
                        } while (hasNext);
                    }
                    return resumes;
                }, null);
    }

    @Override
    public void delete(final String uuid) {
        sqlHelper.<Boolean>transactionalExecute(conn -> {
            try (PreparedStatement stmt
                         = conn.prepareStatement("DELETE FROM Contact WHERE resume_uuid = ?;")) {
                stmt.setString(1, uuid);
                stmt.execute();
            }
            try (PreparedStatement stmt
                         = conn.prepareStatement("DELETE FROM Resume WHERE uuid = ?;")) {
                stmt.setString(1, uuid);
                if (stmt.executeUpdate() == 0) {
                    throw new NotExistStorageException(uuid);
                } else {
                    return true;
                }
            }
        }, uuid);
    }

    @Override
    public Resume get(final String uuid) {
        return sqlHelper.execute(getSqlGet() + " WHERE Resume.uuid = ?;",
                (stmt) -> {
                    stmt.setString(1, uuid);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        Resume resume = getResumeHeadFromRs(rs);
                        fillResumeFromRs(resume, rs);
                        return resume;
                    } else {
                        throw new NotExistStorageException(uuid);
                    }
                }, uuid);
    }

    private Resume getResumeHeadFromRs(ResultSet rs) throws SQLException {
        return new Resume(rs.getString("uuid"), rs.getString("full_name"));
    }

    private boolean fillResumeFromRs(Resume resume, ResultSet rs) throws SQLException {
        boolean isNextExist;
        String uuid = rs.getString("uuid");
        do {
            if (rs.getString("type") != null) {
                resume.addContact(
                        ContactTypes.valueOf(rs.getString("type")),
                        new Contact(
                                rs.getString("name"),
                                rs.getString("url")));
            }
        } while ((isNextExist = rs.next()) && uuid.equals(rs.getString("uuid")));
        return isNextExist;
    }

    private void changeResumeTransactional(Connection conn, Resume resume, DataChangeType type) throws SQLException {
        String sql1 = null;
        String sql2 = null;
        SqlPreparedStatementFunction action = (s) -> null;
        switch (type) {
            case UPDATE:
                sql1 = "UPDATE Resume SET full_name = ? WHERE uuid = ?;";
                sql2 = "UPDATE Contact SET (name, url) = (?, ?) WHERE (resume_uuid = ?) AND (type = ?);";
                action = (stmt) -> {
                    if (stmt.executeUpdate() == 0) {
                        throw new NotExistStorageException(resume.getUuid());
                    }
                    return null;
                };
                break;
            case INSERT:
                sql1 = "INSERT INTO Resume(full_name, uuid) VALUES(?, ?);";
                sql2 = "INSERT INTO Contact(name, url, resume_uuid, type) VALUES(?, ?, ?, ?);";
                action = PreparedStatement::execute;
                break;
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql1)) {
            stmt.setString(1, resume.getFullName());
            stmt.setString(2, resume.getUuid());
            action.apply(stmt);
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql2)) {
            stmt.setString(3, resume.getUuid());
            for (Map.Entry<ContactTypes, Contact> contact : resume.getContacts().entrySet()) {
                stmt.setString(1, contact.getValue().getName());
                stmt.setString(2, contact.getValue().getUrl());
                stmt.setString(4, contact.getKey().name());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private String getSqlGet() {
        return "SELECT" +
                " Resume.uuid uuid," +
                " Resume.full_name, " +
                " Contact.type," +
                " Contact.name," +
                " Contact.url," +
                " Contact.id contact_id" +
                " FROM " +
                " Resume LEFT JOIN Contact" +
                " ON Resume.uuid = Contact.resume_uuid";
    }
}
