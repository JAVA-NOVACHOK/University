package ua.com.nikiforov.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String ERROR_MESSAGES = "errorMessages";
    private static final String ERROR = "ERROR! ";

    @ExceptionHandler(value = {BindException.class})
    public ModelAndView bindExceptionHandling(BindException e) {
        LOGGER.warn("Handling BindException!");
        ModelAndView modelAndView = new ModelAndView("errors/error");
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        List<String> errorMessages = new ArrayList<>();
        for (int i = 1; i <= errors.size(); i++) {
            errorMessages.add(String.format("%d. %s %s!", i, ERROR, errors.get(i - 1).getDefaultMessage()));
        }
        modelAndView.addObject(ERROR_MESSAGES, errorMessages);
        return modelAndView;
    }

    @ExceptionHandler(value = {TransactionSystemException.class})
        public ModelAndView constrainViolationExceptionHandling(TransactionSystemException e){
        ModelAndView modelAndView = new ModelAndView("errors/error");

        Throwable cause = e.getRootCause();
        StringBuilder errors = new StringBuilder();
        if(cause instanceof ConstraintViolationException){
            Set<ConstraintViolation<?>> violations = ((ConstraintViolationException)cause).getConstraintViolations();
            for(ConstraintViolation<?> violation : violations){
                errors.append(violation.getMessage()).append(System.lineSeparator());
            }
        }
        modelAndView.addObject(ERROR_MESSAGES,errors.toString());
        return modelAndView;
    }


}
