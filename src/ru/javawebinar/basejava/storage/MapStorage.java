package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Map;
import java.util.TreeMap;

public class MapStorage extends AbstractStorage {
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
    public Resume[] getAll() {
        Resume[] resumes = new Resume[storage.size()];
        return storage.values().toArray(resumes);
    }

    @Override
    protected Object getIndex(String uuid) {
        if (storage.containsKey(uuid)) {
            return uuid;
        } else {
            return null;
        }
    }

    @Override
    protected Resume getStorageElement(Object key) {
        return storage.get((String) key);
    }

    @Override
    protected void setStorageElement(Object key, Resume resume) {
        storage.put((String) key, resume);
    }

    @Override
    protected void doSaveElement(Object key, Resume resume) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected void doDeleteElement(Object key) {
        storage.remove((String) key);
    }

    protected boolean isKeyExists(Object key) {
        return (key != null);
    }
}