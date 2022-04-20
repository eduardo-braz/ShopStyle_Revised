package com.compass.fasttracker.history.exceptions;

import com.compass.fasttracker.history.exceptions.response.ResponseError;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CheckoutExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> invalidValueArgument(MethodArgumentNotValidException e){
        List<ResponseError> details = new ArrayList<>();
        e.getFieldErrors().forEach(error -> {
            details.add(new ResponseError(error.getField(),
                    error.getDefaultMessage()));
        });
        return new ResponseEntity<>(details.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValueInstantiationException.class)
    public ResponseEntity<?> messageNotReadable(ValueInstantiationException e) {
        String fieldProblem = e.getOriginalMessage().substring(e.getOriginalMessage().lastIndexOf("Invalid"));
        ResponseError error = new ResponseError("type", fieldProblem);
        return new ResponseEntity<>(error.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> responseStatusException(ResponseStatusException e){
        return new ResponseEntity<>(e.getReason(), e.getStatus());
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> feignNotFoundException(FeignException e){
        String path = e.request().requestTemplate().path().toString();
        path = path.substring(path.lastIndexOf("/")+1);
        ResponseError responseError = new ResponseError("user_id", "Not Found user_id " + path);
        return new ResponseEntity<>(responseError.toString(), HttpStatus.valueOf(e.status()));
    }


}
