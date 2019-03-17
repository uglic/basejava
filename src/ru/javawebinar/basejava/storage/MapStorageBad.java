package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Map;
import java.util.TreeMap;

public class MapStorageBad extends AbstractStorage {
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
    protected Object getIndex(String uuid) {
        for (Integer index : storage.keySet()) {
            Resume resume = storage.get(index);
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
        storage.put(((Integer) key).intValue(), resume);
    }

    @Override
    protected void doSaveElement(Object key, Resume resume) {
        int maxKeyValue = -1;
        for (int keyValue : storage.keySet()) {
            if (keyValue > maxKeyValue) {
                maxKeyValue = keyValue;
            }
        }
        storage.put(maxKeyValue + 1, resume);
    }

    @Override
    protected void doDeleteElement(Object key) {
        storage.remove(((Integer) key).intValue());
    }

    protected boolean isKeyExists(Object key) {
        return (((Integer) key).intValue() >= 0);
    }
}
