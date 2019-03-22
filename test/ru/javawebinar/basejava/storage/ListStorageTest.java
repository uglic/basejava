package ru.javawebinar.basejava.storage;

import org.junit.Test;

public class ListStorageTest extends AbstractStorageTest implements AbstractStorageTimeTest {
    public ListStorageTest() {
        super(new ListStorage());
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