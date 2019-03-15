package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorageTest {
    protected Storage storage;

    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";
    protected static final String UUID_NEW = "uuidNew";
    protected static final String UUID_TO_CHECK_EXISTING = UUID_1;
    protected static final String FAIL_MESSAGE_OVERFLOW = "Storage overflow before expected";

    protected final Resume resumeExist1 = new Resume(UUID_1);
    protected final Resume resumeExist2 = new Resume(UUID_2);
    protected final Resume resumeExist3 = new Resume(UUID_3);
    protected final Resume resumeNew = new Resume(UUID_NEW);

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear(); // this line can be deleted because storage here is empty.
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
        Resume newResume = new Resume(UUID_1);
        storage.update(newResume);
        Assert.assertSame(storage.get(newResume.getUuid()), newResume);
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(resumeNew);
    }

    @Test(expected = NullPointerException.class)
    public void updateNullResume() {
        storage.update(null);
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
        storage.save(resumeNew);
        Assert.assertSame(resumeNew, storage.get(UUID_NEW));
        Assert.assertEquals(sizeOld + 1, storage.size());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(resumeExist1);
        // cannot check size here because of exception
    }

    @Test(expected = NullPointerException.class)
    public void saveNullResume() {
        storage.save(null);
        // cannot check size here because of exception
    }

    @Test(expected = StorageException.class)
    public void saveOverflow() {
        storage.clear();
        try {
            for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                String uuid = String.format("%d", i);
                Resume resume = new Resume(uuid);
                storage.save(resume);
            }
        } catch (StorageException e) {
            Assert.fail(FAIL_MESSAGE_OVERFLOW);
        }
        String uuid = "u" + String.format("%d", AbstractArrayStorage.STORAGE_LIMIT);
        Resume resume = new Resume(uuid);
        storage.save(resume);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        int sizeOld = storage.size();
        storage.delete(UUID_TO_CHECK_EXISTING);
        Assert.assertEquals(sizeOld - 1, storage.size());
        storage.get(UUID_TO_CHECK_EXISTING);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(UUID_NEW);
        // cannot check size here because of exception
    }

    @Test
    public void get() {
        Assert.assertEquals(resumeExist1, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(UUID_NEW);
    }
}
