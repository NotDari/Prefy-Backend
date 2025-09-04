package com.daribear.PrefyBackend.Errors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


/**
 * Data Object to represent an error so that it can be received by the application.
 * Used to send the error in a structured format, that can be recognised.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorSchema {
    private String message;
    private Integer customCode;
}
