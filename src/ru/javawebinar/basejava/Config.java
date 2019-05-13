package ru.javawebinar.basejava;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final File PROPERTIES_FILE = new File("config" + System.getProperty("file.separator") + "resumes.properties");
    private static final Properties PROPERTIES = new Properties();
    private static final Config INSTANCE = new Config();

    private final String storageDir;
    private final String databaseUrl;
    private final String databaseUser;
    private final String databasePassword;

    public static Config get() {
        return INSTANCE;
    }

    private Config() {
        try (InputStream is = new FileInputStream(PROPERTIES_FILE)) {
            PROPERTIES.load(is);
            storageDir = PROPERTIES.getProperty("storage.dir").replace("/", System.getProperty("file.separator"));
            databaseUrl = PROPERTIES.getProperty("db.url");
            databaseUser = PROPERTIES.getProperty("db.user");
            databasePassword = PROPERTIES.getProperty("db.password"); // bad practice to store passwords in String (cannot clear it), see java.io.Console.readPassword for example

        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Invalid config file " + PROPERTIES_FILE.getAbsolutePath());
        }
    }

    public String getStorageDir() {
        return storageDir;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }
}