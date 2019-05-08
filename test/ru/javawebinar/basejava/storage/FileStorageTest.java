package ru.javawebinar.basejava.storage;

import java.io.File;

public class FileStorageTest extends AbstractArrayStorageTest {
    public FileStorageTest() {
        super(new FileStorage(new File(STORAGE_DIR), new SerializableStorageStrategy()));
    }
}