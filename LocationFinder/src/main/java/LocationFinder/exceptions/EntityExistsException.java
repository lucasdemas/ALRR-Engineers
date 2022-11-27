package LocationFinder.exceptions;

public class EntityExistsException extends Exception {
    /**
     * A method to create the exception.
     * @param str
     *      The exception message
     */
    public EntityExistsException(final String str) {
        super(str);
    }
}
