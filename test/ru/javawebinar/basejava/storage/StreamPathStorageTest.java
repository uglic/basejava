package ru.javawebinar.basejava.storage;

import java.nio.file.Paths;

public class StreamPathStorageTest extends AbstractStorageTest {
    public StreamPathStorageTest() {
        super(new StreamPathStorage(Paths.get(STORAGE_DIR), new SerializedObjectStorageStream()));
    }
}