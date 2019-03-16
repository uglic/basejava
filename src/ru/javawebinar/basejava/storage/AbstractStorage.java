package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ListIterator;

public abstract class AbstractStorage implements Storage {

    @Override
    public void update(Resume resume) {
        SearchKey key = getIndex(resume.getUuid());
        if (!key.isExist()) {
            throw new NotExistStorageException(resume.toString());
        } else {
            setStorageElement(key, resume);
        }
    }

    @Override
    public void save(Resume resume) {
        SearchKey key = getIndex(resume.getUuid());
        if (key.isExist()) {
            throw new ExistStorageException(resume.toString());
        } else {
            doSaveElement(key, resume);
        }
    }

    @Override
    public void delete(String uuid) {
        SearchKey key = getIndex(uuid);
        if (!key.isExist()) {
            throw new NotExistStorageException(uuid);
        } else {
            doDeleteElement(key);
        }
    }

    @Override
    public Resume get(String uuid) {
        SearchKey key = getIndex(uuid);
        if (!key.isExist()) {
            throw new NotExistStorageException(uuid);
        }
        return getStorageElement(key);
    }

    /**
     * Helper class to pass uniform search key
     */
    protected class SearchKey {
        final private int intKey;
        private String stringKey;
        private ListIterator<Resume> iterator;

        protected SearchKey(int intKey) {
            this.intKey = intKey;
        }

        protected SearchKey(String stringKey) {
            this.intKey = -1;
            this.stringKey = stringKey;
            this.iterator = null;
        }

        protected SearchKey(ListIterator<Resume> iterator) {
            this.intKey = -1;
            this.stringKey = null;
            this.iterator = iterator;
        }

        int intValue() {
            return intKey;
        }

        protected String stringValue() {
            return stringKey;
        }

        protected ListIterator<Resume> iterator() {
            return iterator;
        }

        protected boolean isExist() {
            return ((intKey >= 0)
                    || (stringKey != null)
                    || (iterator != null));
        }
    }

    protected abstract SearchKey getIndex(String uuid);

    protected abstract Resume getStorageElement(SearchKey key);

    protected abstract void setStorageElement(SearchKey key, Resume resume);

    protected abstract void doSaveElement(SearchKey key, Resume resume);

    protected abstract void doDeleteElement(SearchKey key);
}
