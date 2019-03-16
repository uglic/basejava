package ru.javawebinar.basejava.storage;

import org.junit.Test;
import ru.javawebinar.basejava.exception.StorageException;

public class MapStorageTest extends AbstractStorageTest implements AbstractStorageTimeTest {
    private static final String FAIL_MESSAGE_OVERFLOW = "This type of storage does not support overflow exception";

    public MapStorageTest() {
        super(new MapStorage());
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