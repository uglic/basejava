package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public void update(Resume resume) {
        Object key = getIndex(resume.getUuid());
        if (!isKeyExists(key)) {
            throw new NotExistStorageException(resume.toString());
        } else {
            setStorageElement(key, resume);
        }
    }

    @Override
    public void save(Resume resume) {
        Object key = getIndex(resume.getUuid());
        if (isKeyExists(key)) {
            throw new ExistStorageException(resume.toString());
        } else {
            doSaveElement(key, resume);
        }
    }

    @Override
    public void delete(String uuid) {
        Object key = getIndex(uuid);
        if (!isKeyExists(key)) {
            throw new NotExistStorageException(uuid);
        } else {
            doDeleteElement(key);
        }
    }

    @Override
    public Resume get(String uuid) {
        Object key = getIndex(uuid);
        if (!isKeyExists(key)) {
            throw new NotExistStorageException(uuid);
        }
        return getStorageElement(key);
    }

    protected abstract Object getIndex(String uuid);

    protected abstract Resume getStorageElement(Object key);

    protected abstract void setStorageElement(Object key, Resume resume);

    protected abstract void doSaveElement(Object key, Resume resume);

    protected abstract void doDeleteElement(Object key);

    protected abstract boolean isKeyExists(Object key);
}
