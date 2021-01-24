package ua.com.nikiforov.exceptions;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String TIMESTAMP = "timestamp";
    private static final String STATUS = "status";
    private static final String ERRORS = "errors";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, new Date());
        body.put(STATUS, status.value());

        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.toList());
        body.put(ERRORS, errors);

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Object> itemNotFoundExceptionHandler(WebRequest webRequest, Exception exception) {
        return responseEntityBuilder(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DuplicateKeyException.class})
    public ResponseEntity<Object> duplicateKeyExceptionHandler(WebRequest webRequest, Exception exception) {
        return responseEntityBuilder(exception, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> responseEntityBuilder(Exception exception, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, new Date());
        body.put(STATUS, status.value());
        body.put(ERRORS, exception.getMessage());

        return new ResponseEntity<>(body, new HttpHeaders(), status);
    }
}



