package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.ResumeTestData;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {
    protected static final File STORAGE_DIR = new File("data/storage");

    @SuppressWarnings("WeakerAccess")
    protected final Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_NEW = "uuidNew";
    private static final String UUID_TO_CHECK_EXISTING = UUID_1;

    private final Resume RESUME_EXIST_1 = ResumeTestData.get(UUID_1, "Abab Ababov");
    private final Resume RESUME_EXIST_2 = ResumeTestData.get("uuid2", "Baba Babov");
    private final Resume RESUME_EXIST_3 = ResumeTestData.get("uuid3", "Caca Cavov");
    private final Resume RESUME_EXIST_NEW = ResumeTestData.get(UUID_NEW, "Nemo Captain");

    @SuppressWarnings("WeakerAccess")
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
        Resume newResume = ResumeTestData.get(UUID_1, "updated fullName");
        storage.update(newResume);
        Assert.assertEquals(storage.get(newResume.getUuid()), newResume);
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
        Assert.assertEquals(RESUME_EXIST_NEW, storage.get(UUID_NEW));
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