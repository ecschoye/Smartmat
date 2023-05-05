package ntnu.idatt2106.backend.exceptions;

/**
 * Exception for when it does not exist an element
 */
public class NoSuchElementException extends Exception {
    /**
     * Constructor for NoSuchElementException
     * @param message message to specify the error
     */
    public NoSuchElementException(String message) {
        super(message);
    }
}
