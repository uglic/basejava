package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class MapNotUuidStorage extends AbstractStorage {
    protected final Map<String, Resume> storage = new TreeMap<>();
    private static final Comparator<Resume> RESUME_COMPARATOR_FULLNAME = (o1, o2) -> o1.getFullName().compareTo(o2.getFullName());
    private static final Comparator<Resume> RESUME_COMPARATOR_UUID = (o1, o2) -> o1.getUuid().compareTo(o2.getUuid());
    private static final Comparator<Resume> RESUME_COMPARATOR = RESUME_COMPARATOR_FULLNAME.thenComparing(RESUME_COMPARATOR_UUID);

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
        resumes.sort(RESUME_COMPARATOR);
        return resumes;
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return storage.get((String) searchKey);
    }

    @Override
    protected void doUpdate(Resume resume, Object searchKey) {
        storage.put((String) searchKey, resume);
    }

    @Override
    protected void doSave(Resume resume, Object searchKey) {
        storage.put(getUniqueStringKeyFromResume(resume), resume);
    }

    @Override
    protected void doDelete(Object searchKey) {
        storage.remove((String) searchKey);
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return (storage.containsKey(searchKey));
    }

    @Override
    protected String getUniqueStringKeyFromResume(Resume resume) {
        return resume.getFullName();
    }
}