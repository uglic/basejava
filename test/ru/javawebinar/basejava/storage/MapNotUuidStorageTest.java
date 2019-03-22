package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.basejava.model.Resume;

public class MapNotUuidStorageTest extends AbstractStorageTest implements AbstractStorageTimeTest {
    public MapNotUuidStorageTest() {
        super(new MapNotUuidStorage());
    }

    @Test
    public void update() {
        Resume newResume = new Resume(RESUME_EXIST_1);
        storage.update(newResume);
        Assert.assertSame(storage.get(newResume.getFullName()), newResume);
    }

    @Test
    public void save() {
        int sizeOld = storage.size();
        storage.save(RESUME_EXIST_NEW);
        Assert.assertSame(RESUME_EXIST_NEW, storage.get(FULLNAME_NEW));
        Assert.assertEquals(sizeOld + 1, storage.size());
    }

    @Test
    public void get() {
        Assert.assertEquals(RESUME_EXIST_1, storage.get(FULLNAME_1));
    }
}