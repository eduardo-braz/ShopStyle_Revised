package com.compass.ms.catalog.exceptions;

import com.compass.ms.catalog.exceptions.ResponseDTO.InvalidOperationDTO;
import com.compass.ms.catalog.exceptions.ResponseDTO.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;


import javax.validation.UnexpectedTypeException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CatalogExceptionHandler {

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<?> inactiveElement(InvalidOperationException e){
        return new ResponseEntity<>(new InvalidOperationDTO(e.getMessage(),
                e.getFieldName(), e.getValue()), e.getStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFoundElement(){
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> invalidArgumentInNameOrDescription (MethodArgumentNotValidException  e){
        List<ResponseError> details = new ArrayList<>();
        e.getFieldErrors().forEach(error -> {
            details.add(new ResponseError(error.getField(),
                    error.getDefaultMessage()));
        });
        return new ResponseEntity<>(details.toString(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<?> invalidArgumentInCategoryIDs (UnexpectedTypeException e){
        int index = e.getMessage().indexOf("Check configuration for '");
        String message = e.getLocalizedMessage().substring(index);
        String field = message.substring(message.indexOf("'")+1,message.length()-1);
        ResponseError responseError = new ResponseError(field, message);
        return new ResponseEntity<>(responseError.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> responseStatusException(ResponseStatusException e){
        return new ResponseEntity<>(e.getReason(), e.getStatus());
    }


}
