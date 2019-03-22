package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class ListStorage extends AbstractStorage {
    protected final List<Resume> storage = new LinkedList<>();

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
        resumes.sort(Resume.RESUME_COMPARATOR);
        return resumes;
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        // In homework analysis used get(i) - O(n2) for search because list is not an array
        // Never use get(i) for search in real linked lists
        for (ListIterator<Resume> iterator = storage.listIterator(); iterator.hasNext(); ) {
            int index = iterator.nextIndex();
            Resume resume = iterator.next();
            if (resume.getUuid().equals(uuid)) {
                return new Integer(index);
            }
        }
        return new Integer(-1);
    }

    @Override
    protected Resume doGet(Object key) {
        return storage.get((int) key);
    }

    @Override
    protected void doUpdate(Resume resume, Object key) {
        storage.set((int) key, resume);
    }

    @Override
    protected void doSave(Resume resume, Object key) {
        storage.add(resume);
    }

    @Override
    protected void doDelete(Object key) {
        storage.remove((int) key);
    }

    @Override
    protected boolean isExist(Object key) {
        return ((int) key >= 0);
    }

    @Override
    protected String getUniqueStringKeyFromResume(Resume resume) {
        return resume.getUuid();
    }
}
