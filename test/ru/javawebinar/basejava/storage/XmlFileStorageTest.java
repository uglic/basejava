package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.strategy.XmlStorageStrategy;

import java.io.File;

public class XmlFileStorageTest extends AbstractStorageTest {
    public XmlFileStorageTest() {
        super(new FileStorage(new File(STORAGE_DIR), new XmlStorageStrategy()));
    }
}