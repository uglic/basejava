package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class SortedArrayStorage extends AbstractArrayStorage {
    public static final Comparator<Resume> RESUME_COMPARATOR_FOR_SAVE = (o1, o2) -> o1.getUuid().compareTo(o2.getUuid());

    @Override
    protected Integer getSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid, "");
        return new Integer(Arrays.binarySearch(storage, 0, size, searchKey, RESUME_COMPARATOR_FOR_SAVE));
    }

    @Override
    protected int reorder(int index) {
        int from;
        if (index < 0) {
            index = -index;
            from = index - 1;
        } else {
            from = index + 1;
        }
        System.arraycopy(storage, from, storage, index, size - from);
        return from;
    }
}
