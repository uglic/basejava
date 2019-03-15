package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Map;
import java.util.TreeMap;

public class MapStorage extends AbstractStorage {
    /**
     * Use of index as unique key follows from use of getIndex()
     * in the most methods of AbstractStorage.
     * Interface Storage as is don't have any indexes besides uuid,
     * but if we will use Map<String(==uuid), Resume> then we must to implement
     * all AbstractStorage from the sсratch.
     */
    protected final Map<Integer, Resume> storage = new TreeMap<>();

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
    protected int getIndex(String uuid) {
        for (Integer index : storage.keySet()) {
            Resume resume = storage.get(index);
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
        storage.put(index, resume);
    }

    @Override
    protected void doSavedElement(Resume resume, int index) {
        int maxKeyValue = -1;
        for (int key : storage.keySet()) {
            if (key > maxKeyValue) {
                maxKeyValue = key;
            }
        }
        storage.put(maxKeyValue + 1, resume);
    }

    @Override
    protected void doDeletedElement(int index) {
        storage.remove(index);
    }
}
