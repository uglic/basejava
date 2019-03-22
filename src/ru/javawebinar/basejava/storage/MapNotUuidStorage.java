package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MapNotUuidStorage extends AbstractStorage {
    protected final Map<String, Resume> storage = new TreeMap<>();

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumes = new ArrayList<>(storage.values());
        resumes.sort(Resume.RESUME_COMPARATOR);
        return resumes;
    }

    @Override
    public void update(Resume resume) {
        Object key = getExistedSearchKey(resume.getFullName());
        doUpdate(resume, key);
    }

    @Override
    public void save(Resume resume) {
        Object key = getNotExistedSearchKey(resume.getFullName());
        doSave(resume, key);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected Resume doGet(Object key) {
        return storage.get((String) key);
    }

    @Override
    protected void doUpdate(Resume resume, Object key) {
        storage.put((String) key, resume);
    }

    @Override
    protected void doSave(Resume resume, Object key) {
        storage.put(resume.getFullName(), resume);
    }

    @Override
    protected void doDelete(Object key) {
        storage.remove((String) key);
    }

    protected boolean isExist(Object key) {
        return (storage.containsKey(key));
    }

    // also cat be set as protected in AbstractStorage
    private Object getExistedSearchKey(String uuid) {
        Object key = getSearchKey(uuid);
        if (!isExist(key)) {
            throw new NotExistStorageException(uuid);
        }
        return key;
    }

    // also cat be set as protected in AbstractStorage
    private Object getNotExistedSearchKey(String uuid) {
        Object key = getSearchKey(uuid);
        if (isExist(key)) {
            throw new ExistStorageException(uuid);
        }
        return key;
    }
}