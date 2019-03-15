package ru.javawebinar.basejava.storage;

import org.junit.Test;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public class MapStorageTest extends AbstractStorageTest {
    private static final String FAIL_MESSAGE_OVERFLOW = "This type of storage does not support overflow exception";
    private static final int ELEMENT_COUNT = 10_000;

    public MapStorageTest() {
        super(new MapStorage());
    }

    @Test(expected = StorageException.class)
    public void saveOverflow() {
        throw new StorageException(FAIL_MESSAGE_OVERFLOW, "");
    }

    @Test
    public void testOfSpeedSave() {
        long startTime;
        long endTime;
        long time;

        storage.clear();
        startTime = System.nanoTime();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            Resume resume = new Resume();
            storage.save(resume);
        }
        endTime = System.nanoTime();
        time = endTime - startTime;
        System.out.println("M1:testOfSpeedSave:          " + time + " nanosec");
    }

    @Test
    public void testOfSpeedSaveDelete() {
        long startTime;
        long endTime;
        long time;

        // Save and delete test
        storage.clear();
        startTime = System.nanoTime();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            Resume resume = new Resume();
            String uuid = resume.getUuid();
            storage.save(resume);
            storage.delete(uuid);
        }
        endTime = System.nanoTime();
        time = endTime - startTime;
        System.out.println("M1:testOfSpeedSaveDelete:    " + time + " nanosec");
    }

    @Test
    public void testOfSpeedSaveGetDelete() {
        long startTime;
        long endTime;
        long time;

        // Save and delete test
        storage.clear();
        startTime = System.nanoTime();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            Resume resume = new Resume();
            String uuid = resume.getUuid();
            storage.save(resume);
            storage.get(uuid);
            storage.delete(uuid);
        }
        endTime = System.nanoTime();
        time = endTime - startTime;
        System.out.println("M1:testOfSpeedSaveGetDelete: " + time + " nanosec");
    }
}