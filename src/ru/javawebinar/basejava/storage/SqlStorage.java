package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;
import ru.javawebinar.basejava.sql.SqlPreparedStatementFunction;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
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
                    deleteContacts(conn, resume);
                    deleteSections(conn, resume);
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
                conn -> readSectionsForResumes(
                        conn,
                        tryPrepared("SELECT * FROM Resume LEFT JOIN Contact ON Resume.uuid = Contact.resume_uuid ORDER BY full_name, uuid;",
                                conn, stmt -> {
                                    final ResultSet rs = stmt.executeQuery();
                                    final List<Resume> resumes = new ArrayList<>();
                                    if (rs.next()) {
                                        Resume resume;
                                        while ((resume = getResumeFromRs(rs)) != null) {
                                            resumes.add(resume);
                                        }
                                    }
                                    return resumes;
                                }))
                , null);
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
                conn -> readSections(
                        conn,
                        tryPrepared("SELECT * FROM Resume LEFT JOIN Contact ON Resume.uuid = Contact.resume_uuid WHERE uuid = ?;",
                                conn, stmt -> {
                                    stmt.setString(1, uuid);
                                    ResultSet rs = stmt.executeQuery();
                                    if (!rs.next()) throw new NotExistStorageException(uuid);
                                    return getResumeFromRs(rs);
                                }))
                , uuid);
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

    private List<Resume> readSectionsForResumes(final Connection conn, final List<Resume> resumes) throws SQLException {
        return tryPrepared("SELECT Section.* FROM Section LEFT JOIN Resume ON Section.resume_uuid = Resume.uuid ORDER BY full_name, resume_uuid;",
                conn, stmt -> {
                    final ResultSet rs = stmt.executeQuery();
                    if (rs.next()) { // [resumes] are sorted by [full_name, uuid]
                        for (Resume resume : resumes) {
                            readResumeSectionsFromRs(rs, resume);
                        }
                    }
                    return resumes;
                });
    }

    private Resume readSections(final Connection conn, final Resume resume) throws SQLException {
        return tryPrepared("SELECT * FROM Section WHERE resume_uuid = ?;",
                conn, stmt -> {
                    stmt.setString(1, resume.getUuid());
                    final ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        readResumeSectionsFromRs(rs, resume);
                    }
                    return resume;
                });
    }

    private boolean readResumeSectionsFromRs(final ResultSet rs, Resume resume) throws SQLException {
        if (rs.isAfterLast() || resume == null) {
            return false;
        }
        final String uuid = resume.getUuid();
        do {
            if (rs.getString("type") != null) {
                SectionTypes sectionType = SectionTypes.valueOf(rs.getString("type"));
                String content = rs.getString("content");
                AbstractSection section;
                switch (sectionType) {
                    case OBJECTIVE:
                    case PERSONAL:
                        section = new SimpleTextSection(content);
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        section = new BulletedTextListSection(
                                new ArrayList<>(Arrays.asList(content.split("\n"))));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        throw new StorageException("TODO: Unknown section: " + sectionType);
                    default:
                        throw new StorageException("Unknown section: " + sectionType);
                }
                resume.addSection(sectionType, section);
            }
        } while (rs.next() && uuid.equals(rs.getString("resume_uuid")));
        return true;
    }

    private void deleteContacts(final Connection conn, final Resume resume) throws SQLException {
        tryPrepared("DELETE FROM Contact WHERE (resume_uuid = ?)",
                conn, stmt -> {
                    stmt.setString(1, resume.getUuid());
                    stmt.execute();
                    return true;
                });
    }

    private void deleteSections(final Connection conn, final Resume resume) throws SQLException {
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
                    for (Map.Entry<SectionTypes, AbstractSection> sectionEntry : resume.getSections().entrySet()) {
                        SectionTypes sectionType = sectionEntry.getKey();
                        AbstractSection section = sectionEntry.getValue();
                        stmt.setString(1, resume.getUuid());
                        stmt.setString(2, sectionType.name());
                        String content;
                        switch (sectionType) {
                            case OBJECTIVE:
                            case PERSONAL:
                                content = ((SimpleTextSection) section).getContent();
                                break;
                            case ACHIEVEMENT:
                            case QUALIFICATIONS:
                                content = String.join("\n", ((BulletedTextListSection) section).getItems());
                                break;
                            case EXPERIENCE:
                            case EDUCATION:
                                throw new StorageException("TODO: Unknown section type");
                            default:
                                throw new StorageException("Unknown section type: " + sectionType.name());
                        }
                        stmt.setString(3, content);
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
