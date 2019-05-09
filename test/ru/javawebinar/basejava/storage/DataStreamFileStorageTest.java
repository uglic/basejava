package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.strategy.DataStreamStorageStrategy;

import java.io.File;

public class DataStreamFileStorageTest extends AbstractStorageTest {
    public DataStreamFileStorageTest() {
        super(new FileStorage(new File(STORAGE_DIR), new DataStreamStorageStrategy()));
    }
}