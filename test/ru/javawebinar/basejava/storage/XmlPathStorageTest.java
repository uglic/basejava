package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.strategy.XmlStorageStrategy;

import java.nio.file.Paths;

public class XmlPathStorageTest extends AbstractStorageTest {
    public XmlPathStorageTest() {
        super(new PathStorage(Paths.get(STORAGE_DIR), new XmlStorageStrategy()));
    }
}