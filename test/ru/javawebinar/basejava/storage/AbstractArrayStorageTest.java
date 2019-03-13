package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractArrayStorageTest {
    private Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_NEW = "uuidNew";
    private static final String UUID_TO_CHECK_EXISTING = UUID_1;
    private static final String FAIL_MESSAGE_OVERFLOW = "Storage overflow before expected";

    private final Resume resumeExist1 = new Resume(UUID_1);
    private final Resume resumeExist2 = new Resume(UUID_2);
    private final Resume resumeExist3 = new Resume(UUID_3);
    private final Resume resumeNew = new Resume(UUID_NEW);

    protected AbstractArrayStorageTest(Storage storage) {
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
        Resume oldResume = storage.get(UUID_TO_CHECK_EXISTING);
        storage.update(new Resume(UUID_TO_CHECK_EXISTING));
        Resume newResume = storage.get(UUID_TO_CHECK_EXISTING);
        Assert.assertNotSame(oldResume, newResume);
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
            Assert.fail(FAIL_MESSAGE_OVERFLOW);
        }
        String uuid = "u" + String.format("%d", AbstractArrayStorage.STORAGE_LIMIT);
        Resume resume = new Resume(uuid);
        try {
            storage.save(resume);
        } catch (StorageException e) {
            Assert.assertEquals(AbstractArrayStorage.STORAGE_LIMIT, storage.size());
        }
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
        Assert.assertNotNull(storage.get(UUID_TO_CHECK_EXISTING));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(UUID_NEW);
    }
}