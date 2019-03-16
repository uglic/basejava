package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public class ListStorageTest extends AbstractStorageTest implements AbstractStorageTimeTest {
    private static final String FAIL_MESSAGE_OVERFLOW = "This type of storage does not support overflow exception";

    public ListStorageTest() {
        super(new ListStorage());
    }

    @Test
    public void getAll() {
        Resume[] resumesMust = new Resume[]{
                resumeExist3,
                resumeExist2,
                resumeExist1,
        };
        Resume[] resumesReal = storage.getAll();
        Assert.assertArrayEquals(resumesMust, resumesReal);
    }

    @Test(expected = StorageException.class)
    public void saveOverflow() {
        throw new StorageException(FAIL_MESSAGE_OVERFLOW, "");
    }

    @Test
    public void testOfSpeedSave() {
        testOfSpeedSave(storage);
    }

    @Test
    public void testOfSpeedSaveDelete() {
        testOfSpeedSaveDelete(storage);
    }

    @Test
    public void testOfSpeedSaveGetDelete() {
        testOfSpeedSaveGetDelete(storage);
    }

    @Test
    public void testOfSpeedSaveGetUpdateDelete() {
        testOfSpeedSaveGetUpdateDelete(storage);
    }
}