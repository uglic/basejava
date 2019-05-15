package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;
import ru.javawebinar.basejava.sql.SqlPreparedStatementFunction;

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
                                return true;
                            });
                    deleteOldContacts(conn, resume);
                    deleteOldSections(conn, resume);
                    insertContacts(conn, resume);
                    insertSections(conn, resume);
                    return true;
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
                                return true;
                            });
                    insertContacts(conn, resume);
                    insertSections(conn, resume);
                    return true;
                }, resume.getUuid());
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.transactionalExecute(
                conn -> {
                    final List<Resume> resumes = new ArrayList<>();
                    tryPrepared("SELECT * FROM Resume ORDER BY full_name, uuid;",
                            conn, stmt -> {
                                final ResultSet rs = stmt.executeQuery();
                                while (rs.next()) {
                                    Resume resume = new Resume(rs.getString("uuid"), rs.getString("full_name"));
                                    fillEmptyResume(conn, resume);
                                    resumes.add(resume);
                                }
                                return resumes;
                            });
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
        return sqlHelper.transactionalExecute(
                conn -> tryPrepared("SELECT * FROM Resume WHERE uuid = ?;",
                        conn, stmt -> {
                            stmt.setString(1, uuid);
                            ResultSet rs = stmt.executeQuery();
                            if (!rs.next()) throw new NotExistStorageException(uuid);
                            Resume resume = new Resume(uuid, rs.getString("full_name"));
                            fillEmptyResume(conn, resume);
                            return resume;
                        }), uuid);
    }

    private void fillEmptyResume(final Connection conn, final Resume resume) throws SQLException {
        fillContacts(conn, resume);
        fillSections(conn, resume);
    }

    private void fillContacts(final Connection conn, final Resume resume) throws SQLException {
        tryPrepared("SELECT * FROM Contact WHERE resume_uuid = ?;",
                conn, stmt -> {
                    stmt.setString(1, resume.getUuid());
                    final ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        if (rs.getString("type") != null) {
                            resume.addContact(
                                    ContactTypes.valueOf(rs.getString("type")),
                                    new Contact(
                                            rs.getString("name"),
                                            rs.getString("url")));
                        }
                    }
                    return true;
                });
    }

    private void fillSections(final Connection conn, final Resume resume) throws SQLException {
        tryPrepared("SELECT * FROM Section WHERE resume_uuid = ?;",
                conn, stmt -> {
                    stmt.setString(1, resume.getUuid());
                    final ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        if (rs.getString("type") != null) {
                            SectionTypes sectionType = SectionTypes.valueOf(rs.getString("type"));
                            AbstractSection section;
                            switch (sectionType) {
                                case OBJECTIVE:
                                case PERSONAL:
                                    section = new SimpleTextSection(rs.getString("content"));
                                    break;
                                case ACHIEVEMENT:
                                case QUALIFICATIONS:
                                    section = null;
                                    //section = new BulletedTextListSection();
                                    break;
                                case EXPERIENCE:
                                case EDUCATION:
                                    throw new StorageException("TODO::Unknown section: " + sectionType);
                                default:
                                    throw new StorageException("Unknown section: " + sectionType);
                            }
                            resume.addSection(sectionType, section);
                        }
                    }
                    return true;
                });
    }

    private void deleteOldContacts(final Connection conn, final Resume resume) throws SQLException {
        tryPrepared("DELETE FROM Contact WHERE (resume_uuid = ?)",
                conn, stmt -> {
                    stmt.setString(1, resume.getUuid());
                    stmt.execute();
                    return true;
                });
    }

    private void deleteOldSections(final Connection conn, final Resume resume) throws SQLException {
        tryPrepared("DELETE FROM Section WHERE (resume_uuid = ?)",
                conn, stmt -> {
                    stmt.setString(1, resume.getUuid());
                    stmt.execute();
                    return true;
                });
    }

    private void insertContacts(final Connection conn, final Resume resume) throws SQLException {
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
                    return true;
                });
    }

    private void insertSections(final Connection conn, final Resume resume) throws SQLException {
        tryPrepared("INSERT INTO Section(resume_uuid, type, content) VALUES(?, ?, ?);",
                conn, stmt -> {
                    for (Map.Entry<SectionTypes, AbstractSection> section : resume.getSections().entrySet()) {
                        SectionTypes sectionType = section.getKey();
                        stmt.setString(1, resume.getUuid());
                        stmt.setString(2, section.getKey().name());
                        switch (sectionType) {
                            case OBJECTIVE:
                            case PERSONAL:
                                SimpleTextSection sectionTyped = (SimpleTextSection) section.getValue();
                                stmt.setString(3, sectionTyped.getContent());
                                break;
                            case ACHIEVEMENT:
                            case QUALIFICATIONS:
                                //section = new BulletedTextListSection();
                                break;
                            case EXPERIENCE:
                            case EDUCATION:
                                throw new StorageException("TODO::Unknown section: " + sectionType);
                            default:
                                throw new StorageException("Unknown section: " + sectionType);
                        }
                        stmt.addBatch();
                    }
                    stmt.executeBatch();
                    return true;
                });
    }

    private <R> R tryPrepared(final String sql, final Connection conn, final SqlPreparedStatementFunction<R> action) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT)) {
            return action.apply(stmt);
        }
    }
}
