package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

abstract class AbstractArrayStorageTest {
    Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_NEW = "uuidNew";

    private final Resume resumeExist1 = new Resume(UUID_1);
    private final Resume resumeExist2 = new Resume(UUID_2);
    private final Resume resumeExist3 = new Resume(UUID_3);
    private final Resume resumeNew = new Resume(UUID_NEW);

    @Before
    public void setUp() {
        storage.clear();
        storage.save(resumeExist1);
        storage.save(resumeExist2);
        storage.save(resumeExist3);
    }

    @Test
    public void size() {
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public void clear() {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void update() {
        int sizeOld = storage.size();
        Resume oldResume = storage.get(UUID_1);
        storage.update(new Resume(UUID_1));  // must have a new reference
        Resume newResume = storage.get(UUID_1);
        Assert.assertEquals(true, newResume != oldResume);
        Assert.assertEquals(storage.size(), sizeOld);
    }

    @Test
    public void updateNotExist() {
        int sizeOld = storage.size();
        try {
            storage.update(resumeExist1);
        } catch (NotExistStorageException e) {
            Assert.assertEquals(storage.size(), sizeOld);
        }

    }

    @Test
    public void updateNullResume() {
        int sizeOld = storage.size();
        try {
            storage.update(null);
        } catch (NullPointerException e) {
            Assert.assertEquals(storage.size(), sizeOld);
        }
    }

    @Test
    public void getAll() {
        Resume[] resumesMust = new Resume[]{
                resumeExist1,
                resumeExist2,
                resumeExist3,
        };
        Resume[] resumesReal = storage.getAll();
        Assert.assertArrayEquals(resumesMust, resumesReal);
    }

    @Test
    public void save() {
        int sizeOld = storage.size();
        try {
            storage.save(resumeNew);
        } catch (ExistStorageException e) {
            Assert.fail();
        }
        try {
            storage.get(UUID_NEW);
        } catch (NotExistStorageException e) {
            Assert.fail();
        }
        Assert.assertEquals(storage.size(), sizeOld + 1);
    }

    @Test
    public void saveExist() {
        int sizeOld = storage.size();
        try {
            storage.save(resumeExist1);
        } catch (ExistStorageException e) {
            Assert.assertEquals(storage.size(), sizeOld);
        }
    }

    @Test
    public void saveNullResume() {
        int sizeOld = storage.size();
        try {
            storage.save(null);
        } catch (NullPointerException e) {
            Assert.assertEquals(storage.size(), sizeOld);
        }
    }

    @Test
    public void saveOverflow() {
        storage.clear();
        try {
            for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                String uuid = "u" + String.format("%d", i);
                Resume resume = new Resume(uuid);
                storage.save(resume);
            }
        } catch (StorageException e) {
            Assert.fail();
        }
        String uuid = "u" + String.format("%d", AbstractArrayStorage.STORAGE_LIMIT);
        Resume resume = new Resume(uuid);
        try {
            storage.save(resume);
        } catch (StorageException e) {
            Assert.assertEquals(storage.size(), AbstractArrayStorage.STORAGE_LIMIT);
        }
    }

    @Test
    public void delete() {
        int sizeOld = storage.size();
        storage.delete(UUID_1);
        Assert.assertEquals(storage.size(), sizeOld - 1);
    }

    @Test
    public void deleteNotExist() {
        int sizeOld = storage.size();
        try {
            storage.delete(UUID_NEW);
        } catch (NotExistStorageException e) {
            Assert.assertEquals(storage.size(), sizeOld);
        }
    }

    @Test
    public void get() {
        try {
            storage.get(UUID_1);
        } catch (NotExistStorageException e) {
            Assert.fail();
        }
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(UUID_NEW);
    }
}