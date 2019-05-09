package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.strategy.ObjectStreamStorageStrategy;

import java.nio.file.Paths;

public class PathStorageTest extends AbstractStorageTest {
    public PathStorageTest() {
        super(new PathStorage(Paths.get(STORAGE_DIR), new ObjectStreamStorageStrategy()));
    }
}