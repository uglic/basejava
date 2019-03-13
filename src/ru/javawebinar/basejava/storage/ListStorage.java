package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.LinkedList;
import java.util.ListIterator;

public class ListStorage extends AbstractStorage {
    protected final LinkedList<Resume> storage = new LinkedList<Resume>();

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
        Resume[] resumes = new Resume[size()];
        return storage.toArray(resumes);
    }

    @Override
    protected void fillDeletedElement(int index) {
        storage.remove(index);
    }

    @Override
    protected void insertElement(Resume resume, int index) {
        storage.add(index, resume);
    }

    @Override
    protected int getIndex(String uuid) {
        for (ListIterator<Resume> iterator = storage.listIterator(); iterator.hasNext(); ) {
            int index = iterator.nextIndex();
            Resume resume = iterator.next();
            if (resume.getUuid().equals(uuid)) {
                return index;
            }
        }
        return -1;
    }

    @Override
    protected Resume getStorageElement(int index) {
        return storage.get(index);
    }

    @Override
    protected void setStorageElement(int index, Resume resume) {
        storage.set(index, resume);
    }
}
