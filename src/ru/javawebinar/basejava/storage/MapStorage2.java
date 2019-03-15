package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Map;
import java.util.TreeMap;

/**
 * This version of MapStorage not use getIndex().
 *
 */
public class MapStorage2 extends AbstractStorage {
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
    public void update(Resume resume) {
        if (!storage.containsKey(resume.getUuid())) {
            throw new NotExistStorageException(resume.toString());
        } else {
            storage.put(resume.getUuid(), resume);
        }
    }

    @Override
    public void save(Resume resume) {
        if (storage.containsKey(resume.getUuid())) {
            throw new ExistStorageException(resume.toString());
        } else {
            storage.put(resume.getUuid(), resume);
        }
    }

    @Override
    public void delete(String uuid) {
        if (!storage.containsKey(uuid)) {
            throw new NotExistStorageException(uuid);
        } else {
            storage.remove(uuid);
        }
    }

    @Override
    public Resume get(String uuid) {
        if (!storage.containsKey(uuid)) {
            throw new NotExistStorageException(uuid);
        }
        return storage.get(uuid);
    }

    /* not needed for maps */
    @Override
    protected int getIndex(String uuid) {
        return  -1;
    }


    /* not needed for maps */
    @Override
    protected Resume getStorageElement(int index) {
        return null;
    }

    /* not needed for maps */
    @Override
    protected void setStorageElement(int index, Resume resume) {
    }

    /* not needed for maps */
    @Override
    protected void doSavedElement(Resume resume, int index) {
    }

    /* not needed for maps */
    @Override
    protected void doDeletedElement(int index) {
    }
}
