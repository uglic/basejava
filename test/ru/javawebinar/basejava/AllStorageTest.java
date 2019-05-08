package ru.javawebinar.basejava;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ru.javawebinar.basejava.storage.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ArrayStorageTest.class,
        SortedArrayStorageTest.class,
        ListStorageTest.class,
        MapUuidStorageTest.class,
        MapResumeStorageTest.class,
        FileStorageTest.class,
        PathStorageTest.class,
        XmlFileStorageTest.class,
        XmlPathStorageTest.class
})
public class AllStorageTest {
}