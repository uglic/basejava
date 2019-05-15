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
import java.util.EnumSet;
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
                (stmt) -> {
                    ResultSet rs = stmt.executeQuery();
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
        sqlHelper.<Boolean>transactionalExecute(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE Resume SET full_name = ? WHERE uuid = ?;")) {
                setStmtParamsForResume(stmt, resume);
                if (stmt.executeUpdate() == 0) {
                    throw new NotExistStorageException(resume.getUuid());
                }
            }
            addResumeContacts(conn, resume);
            deleteResumeContacts(conn, resume);
            updateResumeContacts(conn, resume,
                    "UPDATE Contact SET (name, url) = (?, ?) WHERE (resume_uuid = ?) AND (type = ?);");
            return null;
        }, resume.getUuid());
    }

    @Override
    public void save(final Resume resume) {
        sqlHelper.<Boolean>transactionalExecute(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO Resume(full_name, uuid) VALUES(?, ?);")) {
                setStmtParamsForResume(stmt, resume);
                stmt.execute();
            }
            updateResumeContacts(conn, resume,
                    "INSERT INTO Contact(name, url, resume_uuid, type) VALUES(?, ?, ?, ?);");
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
                            hasNext = fillResumeFromRs(rs, resume);
                            resumes.add(resume);
                        } while (hasNext);
                    }
                    return resumes;
                }, null);
    }

    @Override
    public void delete(final String uuid) {
        sqlHelper.<Boolean>transactionalExecute(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM Resume WHERE uuid = ?;")) {
                stmt.setString(1, uuid);
                if (stmt.executeUpdate() == 0) {
                    throw new NotExistStorageException(uuid);
                }
                return true;
            }
        }, uuid);
    }

    @Override
    public Resume get(final String uuid) {
        return sqlHelper.execute(getSqlGet() + " WHERE Resume.uuid = ?;",
                (stmt) -> {
                    stmt.setString(1, uuid);
                    ResultSet rs = stmt.executeQuery();
                    if (!rs.next()) throw new NotExistStorageException(uuid);
                    Resume resume = getResumeHeadFromRs(rs);
                    fillResumeFromRs(rs, resume);
                    return resume;
                }, uuid);
    }

    private Resume getResumeHeadFromRs(ResultSet rs) throws SQLException {
        return new Resume(rs.getString("uuid"), rs.getString("full_name"));
    }

    private boolean fillResumeFromRs(ResultSet rs, Resume resume) throws SQLException {
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

    private void setStmtParamsForResume(PreparedStatement stmt, Resume resume) throws SQLException {
        stmt.setString(1, resume.getFullName());
        stmt.setString(2, resume.getUuid());
    }

    private void updateResumeContacts(Connection conn, Resume resume, String sql) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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

    private EnumSet<ContactTypes> getUnsedResumeContacts(Resume resume) {
        EnumSet<ContactTypes> types = EnumSet.noneOf(ContactTypes.class);
        for (ContactTypes type : ContactTypes.values()) {
            if (resume.getContacts().get(type) == null) {
                types.add(type);
            }
        }
        return types;
    }

    private EnumSet<ContactTypes> getUsedResumeContacts(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT type FROM Contact WHERE resume_uuid = ?;")) {
            stmt.setString(1, resume.getUuid());
            EnumSet<ContactTypes> types = EnumSet.noneOf(ContactTypes.class);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                types.add(ContactTypes.valueOf(rs.getString(1)));
            }
            return types;
        }
    }

    private void deleteResumeContacts(Connection conn, Resume resume) throws SQLException {
        EnumSet<ContactTypes> unusedTypes = getUnsedResumeContacts(resume);
        if (unusedTypes.size() > 0) {
            StringBuilder builderSql = new StringBuilder();
            builderSql.append("DELETE FROM Contact WHERE (resume_uuid = ?) AND (type IN (?");
            for (int i = 1; i < unusedTypes.size(); i++) {
                builderSql.append(",?");
            }
            builderSql.append("));");
            try (PreparedStatement stmt = conn.prepareStatement(builderSql.toString())) {
                stmt.setString(1, resume.getUuid());
                int i = 0;
                for (ContactTypes type : unusedTypes) {
                    stmt.setString(i++ + 2, type.name());
                }
                stmt.execute();
            }
        }
    }

    private void addResumeContacts(Connection conn, Resume resume) throws SQLException {
        EnumSet<ContactTypes> usedTypes = getUsedResumeContacts(conn, resume);
        Resume temporaryResume = new Resume(resume.getUuid(), resume.getFullName());
        for (ContactTypes type : ContactTypes.values()) {
            if (resume.getContacts().containsKey(type) && !usedTypes.contains(type)) {
                temporaryResume.addContact(type, resume.getContacts().get(type));
            }
        }
        if (temporaryResume.getContacts().size() > 0) {
            updateResumeContacts(conn, temporaryResume,
                    "INSERT INTO Contact(name, url, resume_uuid, type) VALUES(?, ?, ?, ?);");
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

    private <R> R tryPrepared(Connection conn, String sql, SqlPreparedStatementFunction<R> action) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            return action.apply(stmt);
        }
    }
}
