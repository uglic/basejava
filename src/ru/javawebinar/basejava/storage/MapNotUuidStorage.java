package ru.javawebinar.basejava.storage;

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
        storage.put(getUniqueStringKeyFromResume(resume), resume);
    }

    @Override
    protected void doDelete(Object key) {
        storage.remove((String) key);
    }

    @Override
    protected boolean isExist(Object key) {
        return (storage.containsKey(key));
    }

    @Override
    protected String getUniqueStringKeyFromResume(Resume resume) {
        return resume.getFullName();
    }
}