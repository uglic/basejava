/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];

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

        // this check may be needed for correctness of start index for clear
        if (size > storage.length) {
            size = storage.length;
        }

        for (int i = size - 1; i >= 0; i--) {
            storage[i] = null;
        }
        size = 0;
    }

    /**
     * Add Resume item to the tail of storage
     * Throw an IndexOutOfBoundsException exception in case
     * of storage is already full
     *
     * @param r Resume object to add to storage
     */
    void save(Resume r) {

        // Throw exception if final size more than allowed
        if (size > storage.length - 1) {
            throw new IndexOutOfBoundsException();
        }
        storage[size++] = r;
    }

    /**
     * Search for Resume by it's uuid
     * By conditions of this task storage is unsorted
     * so full search must be done
     * Empty string or null instead of String object are allowed
     * Return first founded Resume if several of objects have
     * the same uuid.
     *
     * @param uuid String representation of Resume we are looking for
     * @return Reference to Resume object if founded or null
     */
    Resume get(String uuid) {
        for (Resume r : storage) {
            if (r != null && r.toString().equals(uuid)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Delete first founded Resume from storage and return
     * Throw an Exception if Resume with uuid was not found
     *
     * @param uuid String representation of Resume we are looking for
     */
    void delete(String uuid) {

        // Look for item
        for (int i = 0; i < size; i++) {
            if (storage[i] != null && storage[i].toString().equals(uuid)) {
                storage[i] = null;
            }
        }

    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return new Resume[0];
    }

    int size() {
        return this.size;
    }
}
