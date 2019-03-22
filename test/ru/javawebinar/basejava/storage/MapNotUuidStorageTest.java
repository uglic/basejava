package ru.javawebinar.basejava.storage;

import org.junit.Test;

public class MapNotUuidStorageTest extends AbstractStorageTest implements AbstractStorageTimeTest {
    public MapNotUuidStorageTest() {
        super(new MapNotUuidStorage());
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