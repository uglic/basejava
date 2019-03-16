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
    protected SearchKey getIndex(String uuid) {
        if (storage.containsKey(uuid)) {
            return new SearchKey(uuid);
        } else {
            return new SearchKey(-1);
        }
    }

    @Override
    protected Resume getStorageElement(SearchKey key) {
        return storage.get(key.stringValue());
    }

    @Override
    protected void setStorageElement(SearchKey key, Resume resume) {
        storage.put(key.stringValue(), resume);
    }

    @Override
    protected void doSaveElement(SearchKey key, Resume resume) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected void doDeleteElement(SearchKey key) {
        storage.remove(key.stringValue());
    }
}