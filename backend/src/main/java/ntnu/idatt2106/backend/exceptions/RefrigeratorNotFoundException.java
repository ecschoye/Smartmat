package ntnu.idatt2106.backend.exceptions;

/**
 * RefrigeratorNotFoundException is thrown when not refrigerator matches an id provided
 */
public class RefrigeratorNotFoundException extends Exception {

    public RefrigeratorNotFoundException(String message){
        super(message);
    }
}
