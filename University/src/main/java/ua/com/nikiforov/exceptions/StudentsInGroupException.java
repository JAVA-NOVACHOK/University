package ua.com.nikiforov.exceptions;

public class StudentsInGroupException extends RuntimeException{

    public StudentsInGroupException(String message) {
        super(message);
    }

    public StudentsInGroupException(String message, Throwable cause) {
        super(message, cause);
    }
}
