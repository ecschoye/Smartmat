package ntnu.idatt2106.backend.exceptions;
/**
 * Custom exception which holds a string.
 */
public class OldPasswordDoesNotMatchException extends Exception {
    public OldPasswordDoesNotMatchException(String message) {
        super(message);
    }
}
