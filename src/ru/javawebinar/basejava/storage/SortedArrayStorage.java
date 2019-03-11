package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    @Override
    protected void insertUsingHelpIndex(Resume resume, int index) {
        int insertAt = -index - 1; // index from Arrays.binarySearch()
        for (int i = size - 1; i >= insertAt; i--) {
            storage[i + 1] = storage[i];
        }
        storage[insertAt] = resume;
    }

    @Override
    protected void deleteUsingHelpIndex(int index) {
        for (int i = index; i < size - 1; i++) {
            storage[i] = storage[i + 1];
        }
    }
}
