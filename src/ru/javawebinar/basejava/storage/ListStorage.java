package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class ListStorage extends AbstractStorage {
    protected final List<Resume> storage = new LinkedList<>();
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
        ArrayList<Resume> resumes = new ArrayList<>(storage);
        resumes.sort(RESUME_COMPARATOR);
        return resumes;
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        int i = 0;
        for (Resume resume : storage) {
            if (resume.getUuid().equals(uuid)) {
                return i;
            }
            ++i;
        }
        return -1;
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return storage.get((int) searchKey);
    }

    @Override
    protected void doUpdate(Resume resume, Object searchKey) {
        storage.set((int) searchKey, resume);
    }

    @Override
    protected void doSave(Resume resume, Object searchKey) {
        storage.add(resume);
    }

    @Override
    protected void doDelete(Object searchKey) {
        storage.remove((int) searchKey);
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return ((int) searchKey >= 0);
    }

    @Override
    protected String getUniqueStringKeyFromResume(Resume resume) {
        return resume.getUuid();
    }
}
