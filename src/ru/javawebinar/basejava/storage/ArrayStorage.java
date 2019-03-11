package ru.javawebinar.basejava.storage;

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
    protected int reorder(int index, int shift) {
        if (shift == -1) {
            storage[index] = storage[size + shift];
        }
        return size;
    }
}