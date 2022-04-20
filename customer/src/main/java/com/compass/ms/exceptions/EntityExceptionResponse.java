package com.compass.ms.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.persistence.EntityExistsException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityExceptionResponse extends EntityExistsException {

    private HttpStatus status;
    private String message;

    public EntityExceptionResponse(HttpStatus status) {
        this.status = status;
    }
}
