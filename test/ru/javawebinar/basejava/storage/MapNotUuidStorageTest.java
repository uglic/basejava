package ru.javawebinar.basejava.storage;

public class MapNotUuidStorageTest extends AbstractStorageTest implements AbstractStorageTimeTest {
    public MapNotUuidStorageTest() {
        super(new MapNotUuidStorage());
    }
}