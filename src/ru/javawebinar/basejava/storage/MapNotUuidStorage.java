package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class MapNotUuidStorage extends AbstractStorage {
    private final Map<Resume, Resume> storage = new TreeMap<>();

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected List<Resume> getAllAnySorted() {
        return new ArrayList<>(storage.values());
    }

    @Override
    protected Object getSearchKey(String uuid) {
        for (Resume resume : storage.keySet()) {
            if (resume.getUuid().equals(uuid)) {
                return resume;
            }
        }
        return null;
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected void doUpdate(Resume resume, Object searchKey) {
        storage.put((Resume) searchKey, resume);
    }

    @Override
    protected void doSave(Resume resume, Object searchKey) {
        storage.put(resume, resume);
    }

    @Override
    protected void doDelete(Object searchKey) {
        storage.remove(searchKey);
    }

    @Override
    protected boolean isExist(Object searchKey) {
        if (searchKey == null) return false;
        return (storage.containsKey(searchKey));
    }
}