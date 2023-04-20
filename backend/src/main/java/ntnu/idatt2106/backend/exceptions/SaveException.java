package ntnu.idatt2106.backend.exceptions;

/**
 * SaveException is used when there is an exception while creating or updating an object
 */
public class SaveException extends Exception {
    public SaveException(String message) {
        super(message);
    }
}
