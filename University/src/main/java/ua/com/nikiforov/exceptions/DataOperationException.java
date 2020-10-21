package ua.com.nikiforov.exceptions;

public class DataOperationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataOperationException(String message) {
        super(message);
    }
    
    public DataOperationException(String message, Exception e) {
        super(message, e);
    }

}
