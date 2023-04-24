package ntnu.idatt2106.backend.exceptions;
/**
 * Custom exception which holds a string.
 */
public class LastSuperuserException extends Exception {
    public LastSuperuserException(String message) {
        super(message);
    }
}
