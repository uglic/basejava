package ru.javawebinar.basejava.storage;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.TestData;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static ru.javawebinar.basejava.TestData.*;

public abstract class AbstractStorageTest {
    protected static final String STORAGE_DIR = Config.get().getStorageDir();

    protected final Storage storage;

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        // order must be random to test getAllSorted()
        storage.clear();
        storage.save(RESUME_EXIST_2);
        storage.save(RESUME_EXIST_1);
        storage.save(RESUME_EXIST_3);
    }

    @Test
    public void size() {
        assertEquals(3, storage.size());
    }

    @Test
    public void clear() {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void update() {
        Resume newResume = TestData.get(UUID_1, "updated fullName", CONTACTS_FOR_UUID_1, SECTIONS_FOR_UUID_1);
        storage.update(newResume);
        assertEquals(newResume, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(RESUME_EXIST_NEW);
    }

    @Test(expected = NullPointerException.class)
    public void updateNullResume() {
        storage.update(null);
    }

    @Test
    public void getAllSorted() {
        List<Resume> resumesExpected = Arrays.asList(RESUME_EXIST_1, RESUME_EXIST_2, RESUME_EXIST_3);
        List<Resume> resumesReal = storage.getAllSorted();
        assertEquals(resumesExpected, resumesReal);
        assertEquals(3, storage.size());
    }

    @Test
    public void save() {
        int sizeOld = storage.size();
        storage.save(RESUME_EXIST_NEW);
        assertEquals(RESUME_EXIST_NEW, storage.get(UUID_NEW));
        assertEquals(sizeOld + 1, storage.size());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(RESUME_EXIST_1);
        // cannot check size here because of exception
    }

    @Test(expected = NullPointerException.class)
    public void saveNullResume() {
        storage.save(null);
        // cannot check size here because of exception
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        int sizeOld = storage.size();
        storage.delete(UUID_TO_CHECK_EXISTING);
        assertEquals(sizeOld - 1, storage.size());
        storage.get(UUID_TO_CHECK_EXISTING);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(UUID_NEW);
    }

    @Test
    public void get() {
        assertEquals(RESUME_EXIST_1, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(UUID_NEW);
    }
}