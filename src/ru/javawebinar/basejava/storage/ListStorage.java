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
    protected Object getIndex(String uuid) {
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
    protected Resume getStorageElement(Object key) {
        return storage.get(((Integer) key).intValue());
    }

    @Override
    protected void setStorageElement(Object key, Resume resume) {
        storage.set(((Integer) key).intValue(), resume);
    }

    @Override
    protected void doSaveElement(Object key, Resume resume) {
        storage.add(0, resume);
    }

    @Override
    protected void doDeleteElement(Object key) {
        storage.remove(((Integer) key).intValue());
    }

    protected boolean isKeyExists(Object key) {
        return (((Integer) key).intValue() >= 0);
    }
}
