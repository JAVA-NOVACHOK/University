package ua.com.nikiforov.exceptions;

public class DataSourceNotInitializeException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public DataSourceNotInitializeException(String message) {
        super(message);
    }
    
    public DataSourceNotInitializeException(String message,Throwable exception) {
        super(message, exception);
    }

}
