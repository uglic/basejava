package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class ListStorage extends AbstractStorage {
    protected final List<Resume> storage = new LinkedList<Resume>();

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
    public void save(Resume r) {
        int index = getIndex(r.getUuid());
        if (index >= 0) {
            throw new ExistStorageException(r.toString());
        } else {
            insertElement(r, index);
        }
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        } else {
            fillDeletedElement(index);
        }
    }

    @Override
    protected void fillDeletedElement(int index) {
        storage.remove(index);
    }

    @Override
    protected void insertElement(Resume resume, int index) {
        storage.add(resume);
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
