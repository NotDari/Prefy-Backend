package com.daribear.PrefyBackend.Errors;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Getter
@Setter
public class CustomError extends RuntimeException {
    private Integer httpStatus;
    private String message;
    private Integer customCode;

    public CustomError(Integer httpsServletResponse, String message, Integer customCode) {
        this.httpStatus = httpsServletResponse;
        this.message = message;
        this.customCode = customCode;
    }

}
