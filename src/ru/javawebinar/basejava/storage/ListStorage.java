package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

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
    public Resume[] getAll() {
        Resume[] resumes = new Resume[storage.size()];
        return storage.toArray(resumes);
    }

    @Override
    protected SearchKey getIndex(String uuid) {
        for (ListIterator<Resume> iterator = storage.listIterator(); iterator.hasNext(); ) {
            Resume resume = iterator.next();
            if (resume.getUuid().equals(uuid)) {
                return new SearchKey(iterator);
            }
        }
        return new SearchKey(-1);
    }

    @Override
    protected Resume getStorageElement(SearchKey key) {
        return key.iterator().previous();
    }

    @Override
    protected void setStorageElement(SearchKey key, Resume resume) {
        key.iterator().set(resume);
    }

    @Override
    protected void doSaveElement(SearchKey key, Resume resume) {
        storage.add(0, resume);
    }

    @Override
    protected void doDeleteElement(SearchKey key) {
        key.iterator().remove();
    }
}
