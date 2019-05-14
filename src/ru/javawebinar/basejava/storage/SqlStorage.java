package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Contact;
import ru.javawebinar.basejava.model.ContactTypes;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        sqlHelper.<Boolean>execute("UPDATE Contact SET (name, url) = (?, ?)" +
                        " WHERE (resume_uuid = ?) AND (type = ?);",
                (ps) -> {
                    ps.setString(3, resume.getUuid());
                    for (Map.Entry<ContactTypes, Contact> contact : resume.getContacts().entrySet()) {
                        ps.setString(1, contact.getValue().getName());
                        ps.setString(2, contact.getValue().getUrl());
                        ps.setString(4, contact.getKey().name());
                        return ps.execute();
                    }
                    return true;
                }, resume.getUuid());
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.execute("SELECT uuid, full_name FROM Resume ORDER BY full_name, uuid;",
                (ps) -> {
                    ResultSet rs = ps.executeQuery();
                    List<Resume> resumes = new ArrayList<>();
                    while (rs.next()) {
                        resumes.add(get(rs.getString("uuid")));
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
        sqlHelper.<Boolean>execute("INSERT INTO Contact(resume_uuid, type, name, url) VALUES(?, ?, ?, ?);",
                (ps) -> {
                    ps.setString(1, resume.getUuid());
                    for (Map.Entry<ContactTypes, Contact> contact : resume.getContacts().entrySet()) {
                        ps.setString(2, contact.getKey().name());
                        ps.setString(3, contact.getValue().getName());
                        ps.setString(4, contact.getValue().getUrl());
                        ps.execute();
                    }
                    return true;
                }, resume.getUuid());
    }

    @Override
    public void delete(final String uuid) {
        sqlHelper.<Boolean>execute("DELETE FROM Contact WHERE resume_uuid = ?;",
                (ps) -> {
                    ps.setString(1, uuid);
                    return true;
                }, uuid);
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
        return sqlHelper.execute("SELECT" +
                        " Resume.uuid uuid," +
                        " Resume.full_name, " +
                        " Contact.type," +
                        " Contact.name," +
                        " Contact.url," +
                        " Contact.id contact_id" +
                        " FROM " +
                        " Resume LEFT JOIN Contact" +
                        " ON Resume.uuid = Contact.resume_uuid" +
                        " WHERE Resume.uuid = ?;",
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

    private Resume getResumeFromResultSet(ResultSet rs) throws SQLException {
        Resume resume = new Resume(
                rs.getString("uuid"),
                rs.getString("full_name"));
        do {
            if (rs.getString("type") != null) {
                resume.addContact(
                        ContactTypes.valueOf(rs.getString("type")),
                        new Contact(
                                rs.getString("name"),
                                rs.getString("url")));
            }
        } while (rs.next());
        return resume;
    }
}
