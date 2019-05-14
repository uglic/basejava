package ru.javawebinar.basejava;

import ru.javawebinar.basejava.storage.SqlStorage;
import ru.javawebinar.basejava.storage.Storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final File PROPERTIES_FILE = new File("config" + System.getProperty("file.separator") + "resumes.properties");
    private static final Config INSTANCE = new Config();

    private final String storageDir;
    private final Storage sqlStorage;

    public static Config get() {
        return INSTANCE;
    }

    private Config() {
        try (InputStream is = new FileInputStream(PROPERTIES_FILE)) {
            Properties props = new Properties();
            props.load(is);
            storageDir = props.getProperty("storage.dir").replace("/", System.getProperty("file.separator"));
            sqlStorage = new SqlStorage(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password") // bad practice to store passwords in String (cannot clear it), see java.io.Console.readPassword for example
            );
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPERTIES_FILE.getAbsolutePath());
        }
    }

    public String getStorageDir() {
        return storageDir;
    }

    public Storage getSqlStorage() {
        return sqlStorage;
    }
}
