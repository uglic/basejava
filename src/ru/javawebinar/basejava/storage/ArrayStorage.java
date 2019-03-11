package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void insertUsingHelpIndex(Resume resume, int index) {
        storage[size] = resume;
    }

    @Override
    protected void deleteUsingHelpIndex(int index) {
        storage[index] = storage[size - 1];
    }

}