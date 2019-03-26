package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class SortedArrayStorage extends AbstractArrayStorage {
    @Override
    protected Integer getSearchKey(String uuid) {
        // Only compare by uuid is allowed here because we don't know any other field value
        return Arrays.binarySearch(storage, 0, size, new Resume(uuid, ""),
                Comparator.comparing(Resume::getUuid));
    }

    @Override
    protected int reorder(Object searchKey) {
        int from;
        int index = (int) searchKey;
        if (index < 0) {
            index = -index;
            from = index - 1;
        } else {
            from = index + 1;
        }
        System.arraycopy(storage, from, storage, index, size - from);
        return from;
    }

    @Override
    protected List<Resume> doCopyAll() {
        return Arrays.asList(Arrays.copyOfRange(storage, 0, size));
    }
}
