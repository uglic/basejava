package ru.javawebinar.basejava.storage;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected SearchKey getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return new SearchKey(i);
            }
        }
        return new SearchKey(-1);
    }

    @Override
    protected int reorder(int index) {
        if (index >= 0) {
            storage[index] = storage[size - 1];
        }
        return size;
    }
}