package ru.javawebinar.basejava.storage;

import org.junit.Test;
import ru.javawebinar.basejava.exception.StorageException;

public class ListStorageTest extends AbstractStorageTest {
    private static final String FAIL_MESSAGE_OVERFLOW = "This type of storage does not support overflow exception";

    public ListStorageTest() {
        super(new ListStorage());
    }

    @Test(expected = StorageException.class)
    public void saveOverflow() {
        throw new StorageException(FAIL_MESSAGE_OVERFLOW, "");
    }

}