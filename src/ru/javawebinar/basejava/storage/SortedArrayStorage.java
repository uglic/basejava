package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class SortedArrayStorage extends AbstractArrayStorage {
    public static final Comparator<Resume> RESUME_COMPARATOR_FOR_SAVE = (o1, o2) -> o1.getUuid().compareTo(o2.getUuid());
    private static final Comparator<Resume> RESUME_COMPARATOR_FULLNAME = (o1, o2) -> o1.getFullName().compareTo(o2.getFullName());
    private static final Comparator<Resume> RESUME_COMPARATOR_UUID = (o1, o2) -> o1.getUuid().compareTo(o2.getUuid());
    private static final Comparator<Resume> RESUME_COMPARATOR = RESUME_COMPARATOR_FULLNAME.thenComparing(RESUME_COMPARATOR_UUID);

    @Override
    protected Integer getSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid, "");
        return new Integer(Arrays.binarySearch(storage, 0, size, searchKey, RESUME_COMPARATOR_FOR_SAVE));
    }

    @Override
    protected int reorder(Object searchKey) {
        int from;
        int index = (int) searchKey;
        if (index < 0) {
            index = -index;
            from = index - 1;
        } else {
            from = index + 1;
        }
        System.arraycopy(storage, from, storage, index, size - from);
        return from;
    }

    @Override
    public List<Resume> getAllSorted() {
        Resume[] resumes = Arrays.copyOfRange(storage, 0, size);
        Arrays.sort(resumes, RESUME_COMPARATOR);
        return Arrays.asList(resumes);
    }
}
