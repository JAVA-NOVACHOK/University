package ua.com.nikiforov.exceptions;

public class QueryNotExecuteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public QueryNotExecuteException(String message) {
        super(message);
    }
    
    public QueryNotExecuteException(String message, Exception e) {
        super(message, e);
    }

}
