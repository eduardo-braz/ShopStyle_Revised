package com.compass.ms.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class BFFExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> feignNotFoundException(FeignException e) {
        return new ResponseEntity<>(e.contentUTF8(), HttpStatus.valueOf(e.status()));
    }



}
