package ru.javawebinar.basejava.storage;

import java.io.File;

public class StreamFileStorageTest extends AbstractStorageTest {
    public StreamFileStorageTest() {
        super(new StreamFileStorage(new File(STORAGE_DIR), new SerializedObjectStorageStream()));
    }
}