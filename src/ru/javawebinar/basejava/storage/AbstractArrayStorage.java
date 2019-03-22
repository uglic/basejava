package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

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
    public List<Resume> getAllSorted() {
        Resume[] resumes = Arrays.copyOfRange(storage, 0, size);
        Arrays.sort(resumes, Resume.RESUME_COMPARATOR);
        return Arrays.asList(resumes);
    }

    @Override
    protected Resume doGet(Object key) {
        return storage[(int) key];
    }

    @Override
    protected void doUpdate(Resume resume, Object key) {
        storage[(int) key] = resume;
    }

    @Override
    protected void doDelete(Object key) {
        reorder((int) key);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected void doSave(Resume resume, Object key) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException(ERROR_MESSAGE_OVERFLOW, resume.toString());
        }
        storage[reorder((int) key)] = resume;
        size++;
    }

    @Override
    protected String getUniqueStringKeyFromResume(Resume resume) {
        return resume.getUuid();
    }

    /**
     * Reorder storage before add and delete operations.
     * Return index at which need to insert new resume
     * (for addition) or any other value (for deletion).
     * If index below zero (resume absent) - addition.
     * If index above or equal zero (exists) - deletion.
     *
     * @param index getIndex() value before reorder.
     * @return index to insert new resume (for addition)
     */
    protected abstract int reorder(int index);

    @Override
    protected boolean isExist(Object key) {
        return ((int) key >= 0);
    }
}