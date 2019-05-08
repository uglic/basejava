package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.strategy.JsonStorageStrategy;

import java.nio.file.Paths;

public class JsonPathStorageTest extends AbstractStorageTest {
    public JsonPathStorageTest() {
        super(new PathStorage(Paths.get(STORAGE_DIR), new JsonStorageStrategy()));
    }
}