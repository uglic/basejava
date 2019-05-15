package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Contact;
import ru.javawebinar.basejava.model.ContactTypes;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;
import ru.javawebinar.basejava.sql.SqlPreparedStatementConsumer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public int size() {
        return sqlHelper.<Integer>execute(
                "SELECT COUNT(uuid) c1 FROM Resume;",
                stmt -> {
                    final ResultSet rs = stmt.executeQuery();
                    if (!rs.next()) throw new StorageException("Error getting resume count");
                    return rs.getInt(1);
                }, null);
    }

    @Override
    public void clear() {
        sqlHelper.<Boolean>execute(
                "DELETE FROM Resume;",
                PreparedStatement::execute, null);
    }

    @Override
    public void update(final Resume resume) {
        sqlHelper.transactionalExecute(
                conn -> {
                    tryPrepared("UPDATE Resume SET full_name = ? WHERE uuid = ?;",
                            conn, stmt -> {
                                stmt.setString(1, resume.getFullName());
                                stmt.setString(2, resume.getUuid());
                                if (stmt.executeUpdate() == 0) throw new NotExistStorageException(resume.getUuid());
                            });
                    deleteOldThenInsertNewResumeContacts(conn, resume);
                }, resume.getUuid());
    }

    @Override
    public void save(final Resume resume) {
        sqlHelper.transactionalExecute(
                conn -> {
                    tryPrepared("INSERT INTO Resume(uuid, full_name) VALUES(?, ?);",
                            conn, stmt -> {
                                stmt.setString(1, resume.getUuid());
                                stmt.setString(2, resume.getFullName());
                                stmt.execute();
                            });
                    deleteOldThenInsertNewResumeContacts(conn, resume);
                }, resume.getUuid());
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.execute("SELECT * FROM Resume LEFT JOIN Contact ON Resume.uuid = Contact.resume_uuid ORDER BY full_name, uuid;",
                stmt -> {
                    final ResultSet rs = stmt.executeQuery();
                    final List<Resume> resumes = new ArrayList<>();
                    if (rs.next()) {
                        Resume resume;
                        while ((resume = getResumeFromRs(rs)) != null) {
                            resumes.add(resume);
                        }
                    }
                    return resumes;
                }, null);
    }

    @Override
    public void delete(final String uuid) {
        sqlHelper.execute("DELETE FROM Resume WHERE uuid = ?;",
                stmt -> {
                    stmt.setString(1, uuid);
                    if (stmt.executeUpdate() == 0) throw new NotExistStorageException(uuid);
                    return true;
                }, uuid);
    }

    @Override
    public Resume get(final String uuid) {
        return sqlHelper.execute("SELECT * FROM Resume LEFT JOIN Contact ON Resume.uuid = Contact.resume_uuid WHERE uuid = ?;",
                stmt -> {
                    stmt.setString(1, uuid);
                    ResultSet rs = stmt.executeQuery();
                    if (!rs.next()) throw new NotExistStorageException(uuid);
                    return getResumeFromRs(rs);
                }, uuid);
    }

    private Resume getResumeFromRs(final ResultSet rs) throws SQLException {
        if (rs.isAfterLast()) {
            return null;
        }
        final String uuid = rs.getString("uuid");
        final Resume resume = new Resume(uuid, rs.getString("full_name"));
        do {
            if (rs.getString("type") != null) {
                resume.addContact(
                        ContactTypes.valueOf(rs.getString("type")),
                        new Contact(
                                rs.getString("name"),
                                rs.getString("url")));
            }
        } while (rs.next() && uuid.equals(rs.getString("uuid")));
        return resume;
    }

    private void deleteOldThenInsertNewResumeContacts(final Connection conn, final Resume resume) throws SQLException {
        tryPrepared("DELETE FROM Contact WHERE (resume_uuid = ?)",
                conn, stmt -> {
                    stmt.setString(1, resume.getUuid());
                    stmt.execute();
                });
        tryPrepared("INSERT INTO Contact(name, url, resume_uuid, type) VALUES(?, ?, ?, ?);",
                conn, stmt -> {
                    for (Map.Entry<ContactTypes, Contact> contact : resume.getContacts().entrySet()) {
                        stmt.setString(1, contact.getValue().getName());
                        stmt.setString(2, contact.getValue().getUrl());
                        stmt.setString(3, resume.getUuid());
                        stmt.setString(4, contact.getKey().name());
                        stmt.addBatch();
                    }
                    stmt.executeBatch();
                });
    }

    private void tryPrepared(final String sql, final Connection conn, final SqlPreparedStatementConsumer action) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT)) {
            action.accept(stmt);
        }
    }
}
