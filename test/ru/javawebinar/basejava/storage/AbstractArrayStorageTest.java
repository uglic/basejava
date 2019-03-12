package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.NotExistStorageException;
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

    public void setUpTestObjects(Object caller) throws Exception {
        String testClassName = caller.getClass().getName();
        String className = testClassName.substring(0, testClassName.lastIndexOf("Test"));
        Class<?> classObj = Class.forName(className);
        Constructor<?> constructor = classObj.getConstructor(new Class[0]);
        storage = (Storage) constructor.newInstance(new Object[0]);
        System.out.println("Created instance of " + storage.getClass().getName());
    }

    @Test
    public void size() throws Exception {
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void update() throws Exception {
        String testUUID = UUID_1;
        Resume resume = new Resume(testUUID);
        Resume oldResume = storage.get(testUUID);
        storage.update(resume);
        Resume newResume = storage.get(testUUID);
        Assert.assertEquals(testUUID, newResume.getUuid());
        Assert.assertEquals(true, newResume != oldResume);
    }

    @Test
    public void getAll() throws Exception {

    }

    @Test
    public void save() throws Exception {

    }

    @Test
    public void delete() throws Exception {

    }

    @Test
    public void get() throws Exception {

    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get("dummy");
    }
}