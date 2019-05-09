package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.strategy.DataStreamStorageStrategy;

import java.nio.file.Paths;

public class DataStreamPathStorageTest extends AbstractStorageTest {
    public DataStreamPathStorageTest() {
        super(new PathStorage(Paths.get(STORAGE_DIR), new DataStreamStorageStrategy()));
    }
}