package com.daribear.PrefyBackend.Errors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorSchema {
    private String message;
    private Integer customCode;
}
