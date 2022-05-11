package com.compass.ms.exceptions;

import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustumerExceptionHandler {

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<?> userEmailFound (EntityExceptionResponse e){
        return new ResponseEntity<>(
                new ErrorMessage(e.getMessage(), e.getCause()),
                e.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<?> invalidArgument (MethodArgumentNotValidException e){
        List<ResponseError> details = new ArrayList<>();
        e.getFieldErrors().forEach(error -> {
            details.add(new ResponseError(error.getField(),
                    error.getDefaultMessage()));
        });
        return new ResponseEntity<>(details.toString(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> responseStatusException(ResponseStatusException e){
        return new ResponseEntity<>(e.getReason(), e.getStatus());
    }

}
