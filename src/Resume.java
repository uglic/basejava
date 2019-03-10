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

}
