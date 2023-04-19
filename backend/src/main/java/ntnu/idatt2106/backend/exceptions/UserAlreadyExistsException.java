package ntnu.idatt2106.backend.exceptions;
/**
 * Custom exception which holds a string.
 */
public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
