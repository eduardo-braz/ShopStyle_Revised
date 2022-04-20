package com.compass.ms.catalog.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class InvalidOperationException extends Throwable {

    private String message;
    private String fieldName;
    private String value;
    private HttpStatus status;


    public InvalidOperationException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public InvalidOperationException(HttpStatus status) {
        this.status = status;
    }

}
