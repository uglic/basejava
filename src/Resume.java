import java.util.Objects;

/**
 * Initial resume class
 */
public class Resume {

    // Unique identifier
    public String uuid;

    @Override
    public String toString() {
        return uuid;
    }

    /**
     * Return unique identifier of resume
     * Function was introduced in Lesson 2
     *
     * @return Unique identifier
     */
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return uuid.equals(resume.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
