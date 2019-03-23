package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class SortedArrayStorage extends AbstractArrayStorage {
    public static final Comparator<Resume> RESUME_COMPARATOR_FOR_SAVE =  Comparator.comparing(Resume::getUuid);

    @Override
    protected Integer getSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid, "");
        return new Integer(Arrays.binarySearch(storage, 0, size, searchKey, RESUME_COMPARATOR_FOR_SAVE));
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
    protected List<Resume> getAllAnySorted() {
        return Arrays.asList(Arrays.copyOfRange(storage, 0, size));
    }
}
