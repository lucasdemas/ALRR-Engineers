package LocationFinder.exceptions;



public class NotFoundException extends Exception {
    /**
     * A method to create the exception.
     * @param str
     *      The exception message
     */
    public NotFoundException(final String str) {
        super(str);
    }
}
