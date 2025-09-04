package com.daribear.PrefyBackend.Errors;

import com.daribear.PrefyBackend.Errors.CustomError;
import com.daribear.PrefyBackend.Errors.ErrorSchema;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.NestedIOException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;

/**
 * This is my custom spring exception handler which returns the error to clients, so the client can figure out the issue
 */
@ControllerAdvice()
public class ExceptionInterceptor extends ResponseEntityExceptionHandler {

    public ExceptionInterceptor() {
        System.out.println("4129 controllerAdvice Accessed");
    }

    /**
     * Handles any customError thrown and converts it into an ErrorScheme and returns it with the status.
     * @param e the customError
     * @return ResponseEntity with the error details
     */
    @ExceptionHandler(CustomError.class)
    protected ResponseEntity<CustomError> handleAllExceptions(CustomError e) {
        ErrorSchema exceptionResponse =
                new ErrorSchema(
                        e.getMessage(), ((CustomError)e).getCustomCode());
        return new ResponseEntity(exceptionResponse, HttpStatus.valueOf(((CustomError)e).getHttpStatus()));
    }

    /**
     * Handles an acessdenied exception
     * @param e the exception thrown
     * @return the Response Entity with the error details.
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    protected ResponseEntity<Object> test(Exception e){
        return  new ResponseEntity<>(e, HttpStatus.HTTP_VERSION_NOT_SUPPORTED);
    }

}
