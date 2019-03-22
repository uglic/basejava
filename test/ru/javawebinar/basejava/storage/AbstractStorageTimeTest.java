package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

interface AbstractStorageTimeTest {
    boolean MAKE_TIME_TESTS = true; // Run or stop time tests for all
    int ELEMENT_COUNT = 10_000;     // Count of simple operations to perform
    int LEN_OF_MESSAGE = 30;

    default void testOfSpeedSave(Storage storage) {
        testOfSpeed(storage, true, false, false, false, LEN_OF_MESSAGE);
    }

    default void testOfSpeedSaveDelete(Storage storage) {
        testOfSpeed(storage, true, false, false, true, LEN_OF_MESSAGE);
    }

    default void testOfSpeedSaveGetDelete(Storage storage) {
        testOfSpeed(storage, true, true, false, true, LEN_OF_MESSAGE);
    }

    default void testOfSpeedSaveGetUpdateDelete(Storage storage) {
        testOfSpeed(storage, true, true, true, true, LEN_OF_MESSAGE);
    }

    default void testOfSpeed(Storage storage,
                             boolean runSave, boolean runGet, boolean runUpdate, boolean runDelete,
                             int lenOfMessage) {
        if (!MAKE_TIME_TESTS) return;

        long startTime;
        long endTime;
        long time;
        storage.clear();
        startTime = System.nanoTime();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            Resume resume = new Resume();
            String uuid = resume.getUuid();
            if (runSave) storage.save(resume);
            if (runGet) storage.get(uuid);
            if (runUpdate) storage.update(resume);
            if (runDelete) storage.delete(uuid);
        }
        endTime = System.nanoTime();
        time = endTime - startTime;

        StringBuilder sb = new StringBuilder();
        sb.append("TestOf ");
        if (runSave) sb.append("Save");
        if (runGet) sb.append("Get");
        if (runUpdate) sb.append("Update");
        if (runDelete) sb.append("Delete");
        if (sb.length() < LEN_OF_MESSAGE) {
            int appendSize = LEN_OF_MESSAGE - sb.length();
            for (int i = 0; i < appendSize; i++) {
                sb.append(" ");
            }
        }
        sb.append(String.format("%12d", time));
        sb.append(" nanosec: ");
        sb.append(this.getClass().getSimpleName());

        System.out.println(sb.toString());
    }
}