package ntnu.idatt2106.backend.exceptions;
/**
 * Custom exception which holds a string.
 */
public class UserNotFoundException extends Exception{
    public UserNotFoundException(String message) {
        super(message);
    }
}
