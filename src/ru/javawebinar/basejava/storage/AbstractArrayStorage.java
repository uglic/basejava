package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10_000;
    protected static final String ERROR_MESSAGE_OVERFLOW = "Storage overflow";

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    @Override
    public void save(Resume r) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException(ERROR_MESSAGE_OVERFLOW, r.toString());
        } else {
            super.save(r);
            size++;
        }
    }

    @Override
    public void delete(String uuid) {
        super.delete(uuid);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected Resume getStorageElement(int index) {
        return storage[index];
    }

    @Override
    protected void setStorageElement(int index, Resume resume) {
        storage[index] = resume;
    }
}
