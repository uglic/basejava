package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.lang.reflect.Constructor;

public abstract class AbstractArrayStorageTest {
    protected Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";

    @Before
    public void setUp() throws Exception {
        setUpTestObjects(this);
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Ignore
    public void setUpTestObjects(Object caller) throws Exception {
        Class<?> classObj = Class.forName(getTestingClassName(caller));
        Constructor<?> constructor = classObj.getConstructor(new Class[0]);
        storage = (Storage) constructor.newInstance(new Object[0]);
    }

    @Ignore
    private String getTestingClassName(Object caller) {
        String testClassName = caller.getClass().getName();
        return testClassName.substring(0, testClassName.lastIndexOf("Test"));
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
        String testUUID = UUID_1;
        Resume resume = new Resume(testUUID);
        Resume oldResume = storage.get(testUUID);
        storage.update(resume);
        Resume newResume = storage.get(testUUID);
        Assert.assertEquals(testUUID, newResume.getUuid());
        Assert.assertEquals(true, newResume != oldResume);
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        Resume resume = new Resume(UUID_1 + "update");
        storage.update(resume);
    }

    @Test(expected = NullPointerException.class)
    public void updateNullResume() {
        storage.update(null);
    }

    @Test
    public void getAll() {
        Resume[] resumesMust = new Resume[]{
                new Resume(UUID_1),
                new Resume(UUID_2),
                new Resume(UUID_3),
        };
        Resume[] resumesReal = storage.getAll();
        Assert.assertArrayEquals(resumesMust, resumesReal);
    }

    @Test
    public void getAllEmpty() {
        storage.clear();
        Resume[] resumesMust = new Resume[0];
        Resume[] resumesReal = storage.getAll();
        Assert.assertArrayEquals(resumesMust, resumesReal);
    }

    @Test
    public void save() {
        String newUUID = UUID_1 + "save";
        Resume resume = new Resume(newUUID);
        storage.save(resume);
        Assert.assertEquals(newUUID, storage.get(newUUID).getUuid());
        Assert.assertEquals(true, storage.get(newUUID) == resume);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        Resume resume = storage.get(UUID_1);
        storage.save(resume);
    }

    @Test(expected = NullPointerException.class)
    public void saveNullResume() {
        storage.save(null);
    }

    @Test(expected = StorageException.class)
    public void saveOverflow() {
        final int LOCAL_LIMIT = 1_000_001;
        storage.clear();
        for (int i = 0; i < LOCAL_LIMIT; i++) {
            String uuid = "u" + String.format("%d", i);
            Resume resume = new Resume(uuid);
            try {
                storage.save(resume);
            } catch (StorageException e) {
                System.out.println("Storage limit for [" + getTestingClassName(this)
                        + "] found: " + i + " items");
                throw e;
            }
        }
        System.out.println("Storage limit for [" + getTestingClassName(this)
                + "] not found. May be set bigger LOCAL_LIMIT in saveOverflow()");
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        final String checkUUID = UUID_3;
        Assert.assertEquals (checkUUID, storage.get(checkUUID).getUuid());
        storage.delete(checkUUID);
        storage.get(checkUUID);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        String newUUID = UUID_1 + "delete";
        storage.delete(newUUID);
    }

    @Test
    public void get() {
        final String checkUUID = UUID_2;
        Assert.assertEquals(checkUUID, storage.get(checkUUID).getUuid());
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("dummy");
    }
}