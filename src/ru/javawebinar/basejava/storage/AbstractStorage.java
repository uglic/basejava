package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public void update(Resume resume) {
        Object key = getExistedSearchKey(getUniqueStringKeyFromResume(resume));
        doUpdate(resume, key);
    }

    @Override
    public void save(Resume resume) {
        Object key = getNotExistedSearchKey(getUniqueStringKeyFromResume(resume));
        doSave(resume, key);
    }

    @Override
    public void delete(String uuid) {
        Object key = getExistedSearchKey(uuid);
        doDelete(key);
    }

    @Override
    public Resume get(String uuid) {
        Object key = getExistedSearchKey(uuid);
        return doGet(key);
    }

    protected abstract Object getSearchKey(String uuid);

    protected abstract Resume doGet(Object key); // very bad practice is to call get method starting with do

    protected abstract void doUpdate(Resume resume, Object key);

    protected abstract void doSave(Resume resume, Object key);

    protected abstract void doDelete(Object key);

    protected abstract boolean isExist(Object key);

    protected abstract String getUniqueStringKeyFromResume(Resume resume);

    private Object getExistedSearchKey(String uuid) {
        Object key = getSearchKey(uuid);
        if (!isExist(key)) {
            throw new NotExistStorageException(uuid);
        }
        return key;
    }

    private Object getNotExistedSearchKey(String uuid) {
        Object key = getSearchKey(uuid);
        if (isExist(key)) {
            throw new ExistStorageException(uuid);
        }
        return key;
    }
}
