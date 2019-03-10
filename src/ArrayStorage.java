import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    /**
     * Storage of resume
     */
    private final Resume[] storage = new Resume[10000];

    /**
     * Count of saved elements
     */
    private int size = 0;

    /**
     * Clear storage
     * Storage cleared by setting each array item to null
     * If not doing so Java heap will be littered
     * by Resume objects and String objects inside them.
     */
    void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }


    /**
     * Update early created resume with new (replace old)
     *
     * @param r Resume which will replace old with the same uuid
     * @throws IndexOutOfBoundsException If not found resume with the same uuid
     * @throws NullPointerException      If passed null value instead of resume
     */
    void update(Resume r) {
        if (r == null) {
            throw new NullPointerException();
        }
        String uuid = r.getUuid(); // to avoid multiple calls
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                storage[i] = r;
                return;
            }
        }
        System.out.println("Resume uuid=" + r.getUuid() + " not found for update");
        throw new IndexOutOfBoundsException();
    }

    /**
     * Add Resume item to the tail of storage
     * If resume with this uuid is already exists, do nothing.
     * Throw an IndexOutOfBoundsException exception
     * in a case of the storage is already full.
     * Null values not allowed, throw an NullPointerException in that case
     *
     * @param r Resume object to add to storage
     * @throws IndexOutOfBoundsException Storage is already full
     * @throws NullPointerException      Try to add null as Resume
     */
    void save(Resume r) {
        if (r == null) {
            throw new NullPointerException();
        }
        if (size >= storage.length) {
            throw new IndexOutOfBoundsException();
        }
        if (get(r.getUuid()) == null) {
            storage[size++] = r;
        } else {
            System.out.println("Resume uuid=" + r.getUuid() + " already exists");
        }
    }

    /**
     * Search for Resume by it's uuid
     * By conditions of this task storage is unsorted
     * so full search must be done.
     * Empty string or null instead of String object are allowed
     * Return first founded Resume if several of objects have
     * the same uuid.
     *
     * @param uuid String representation of Resume we are looking for
     * @return Reference to Resume object if founded or null
     */
    Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return storage[i];
            }
        }
        System.out.println("Resume uuid=" + uuid + " not found");
        return null;
    }

    /**
     * Delete first founded Resume from storage and return.
     * Throw an IndexOutOfBoundsException exception
     * if Resume with the uuid was not found.
     *
     * @param uuid String representation of Resume we are looking for
     * @throws IndexOutOfBoundsException if resume with the uuid was not found
     */
    void delete(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                System.arraycopy(storage, i + 1, storage, i, size - i - 1);
                storage[size - 1] = null; // clear double reference to the same Resume
                size--;
                return;
            }
        }
        System.out.println("Resume uuid=" + uuid + " not found for delete");
        throw new IndexOutOfBoundsException();
    }

    /**
     * Return all saved resumed
     *
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    /**
     * Count of saved elements in storage
     *
     * @return Count of saved elements in storage
     */
    int size() {
        return this.size;
    }
}
