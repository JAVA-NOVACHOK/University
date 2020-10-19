package ua.com.nikiforov.exceptions;

public class ChangesNotMadeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ChangesNotMadeException(String message) {
        super(message);
    }
    
    public ChangesNotMadeException(String message, Exception e) {
        super(message, e);
    }

}
