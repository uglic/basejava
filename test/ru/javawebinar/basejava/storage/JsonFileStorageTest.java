package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.strategy.JsonStorageStrategy;

import java.io.File;

public class JsonFileStorageTest extends AbstractStorageTest {
    public JsonFileStorageTest() {
        super(new FileStorage(new File(STORAGE_DIR), new JsonStorageStrategy()));
    }
}