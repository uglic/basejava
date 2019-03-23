package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    static final int STORAGE_LIMIT = 10_000;
    static final String ERROR_MESSAGE_OVERFLOW = "Storage overflow";

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
    protected Resume doGet(Object searchKey) {
        return storage[(int) searchKey];
    }

    @Override
    protected void doUpdate(Resume resume, Object searchKey) {
        storage[(int) searchKey] = resume;
    }

    @Override
    protected void doDelete(Object searchKey) {
        reorder((int) searchKey);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected void doSave(Resume resume, Object searchKey) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException(ERROR_MESSAGE_OVERFLOW, resume.toString());
        }
        storage[reorder((int) searchKey)] = resume;
        size++;
    }

    @Override
    protected String getUniqueStringKeyFromResume(Resume resume) {
        return resume.getUuid();
    }

    protected abstract int reorder(Object searchKey);

    @Override
    protected boolean isExist(Object searchKey) {
        return ((int) searchKey >= 0);
    }
}