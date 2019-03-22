package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStorageTest {
    protected final Storage storage;

    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";
    protected static final String UUID_NEW = "uuidNew";
    protected static final String FULLNAME_1 = "Abab Ababov";
    protected static final String FULLNAME_2 = "Baba Babov";
    protected static final String FULLNAME_3 = "Caca Cavov";
    protected static final String FULLNAME_NEW = "Nemo Captain";

    protected static final String UUID_TO_CHECK_EXISTING = UUID_1;
    protected static final String FAIL_MESSAGE_OVERFLOW = "Storage overflow before expected";
    protected static final String FAIL_MESSAGE_OVERFLOW_ONLY_FOR_ARRAYS = "This type of storage does not support overflow exception";

    protected final Resume RESUME_EXIST_1 = new Resume(UUID_1, FULLNAME_1);
    protected final Resume RESUME_EXIST_2 = new Resume(UUID_2, FULLNAME_2);
    protected final Resume RESUME_EXIST_3 = new Resume(UUID_3, FULLNAME_3);
    protected final Resume RESUME_EXIST_NEW = new Resume(UUID_NEW, FULLNAME_NEW);

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear(); // this line can be deleted because storage here is empty.
        storage.save(RESUME_EXIST_1);
        storage.save(RESUME_EXIST_2);
        storage.save(RESUME_EXIST_3);
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
        Resume newResume = new Resume(RESUME_EXIST_1);
        storage.update(newResume);
        Assert.assertSame(storage.get(newResume.getUuid()), newResume);
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
        storage.clear();
        storage.save(RESUME_EXIST_2);
        storage.save(RESUME_EXIST_1);
        storage.save(RESUME_EXIST_3);
        List<Resume> resumesMust = new ArrayList<>();
        resumesMust.add(RESUME_EXIST_1);
        resumesMust.add(RESUME_EXIST_2);
        resumesMust.add(RESUME_EXIST_3);
        List<Resume> resumesReal = storage.getAllSorted();
        Assert.assertEquals(resumesMust, resumesReal);
    }

    @Test
    public void save() {
        int sizeOld = storage.size();
        storage.save(RESUME_EXIST_NEW);
        Assert.assertSame(RESUME_EXIST_NEW, storage.get(UUID_NEW));
        Assert.assertEquals(sizeOld + 1, storage.size());
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

    @Test(expected = StorageException.class)
    public void saveOverflow() {
        if (storage instanceof ArrayList) {
            storage.clear();
            try {
                for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                    Resume resume = new Resume();
                    storage.save(resume);
                }
            } catch (StorageException e) {
                Assert.fail(FAIL_MESSAGE_OVERFLOW);
            }
            String uuid = "u" + String.format("%d", AbstractArrayStorage.STORAGE_LIMIT);
            Resume resume = new Resume();
            storage.save(resume);
        } else {
            throw new StorageException(FAIL_MESSAGE_OVERFLOW_ONLY_FOR_ARRAYS, "");
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
        Assert.assertEquals(RESUME_EXIST_1, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(UUID_NEW);
    }
}