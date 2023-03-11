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

@ControllerAdvice()
public class ExceptionInterceptor extends ResponseEntityExceptionHandler {

    public ExceptionInterceptor() {
        System.out.println("4129 controllerAdvice Accessed");
    }

    @ExceptionHandler(CustomError.class)
    protected ResponseEntity<CustomError> handleAllExceptions(CustomError e) {
        System.out.println("4129 HANDLER CALLED!");
        ErrorSchema exceptionResponse =
                new ErrorSchema(
                        e.getMessage(), ((CustomError)e).getCustomCode());
        return new ResponseEntity(exceptionResponse, HttpStatus.valueOf(((CustomError)e).getHttpStatus()));
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    protected ResponseEntity<Object> test(Exception e){
        System.out.println("Sdad testing");
        return  new ResponseEntity<>(e, HttpStatus.HTTP_VERSION_NOT_SUPPORTED);
    }

}
