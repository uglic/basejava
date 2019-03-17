package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected Object getIndex(String uuid) {
        Resume searchKey = new Resume(uuid);
        return new Integer(Arrays.binarySearch(storage, 0, size, searchKey));
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
