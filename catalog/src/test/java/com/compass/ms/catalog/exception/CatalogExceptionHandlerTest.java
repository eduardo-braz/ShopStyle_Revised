package com.compass.ms.catalog.exception;

import com.compass.ms.catalog.exceptions.CatalogExceptionHandler;
import com.compass.ms.catalog.exceptions.InvalidOperationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.UnexpectedTypeException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("Exception Handler Test")
public class CatalogExceptionHandlerTest {

    @InjectMocks
    CatalogExceptionHandler catalogExceptionHandler;

    @MockBean
    BindException bindException;

    @MockBean
    MethodParameter parameter;

    @Test
    public void shouldHaveReturnResponseEntityWhenInvalidOperationExceptionCalled(){
        InvalidOperationException exception = new InvalidOperationException("Message test", HttpStatus.BAD_REQUEST);
        ResponseEntity<?> responseEntity = catalogExceptionHandler.inactiveElement(exception);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().toString().contains("Message test"));
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldHaveReturnResponseEntityWhenNotFoundElementCalled(){
        ResponseEntity<?> responseEntity = catalogExceptionHandler.notFoundElement();
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void shouldHaveReturnResponseEntityWhenInvalidArgumentInNameOrDescriptionCalled(){
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindException);
        ResponseEntity<?> responseEntity = catalogExceptionHandler.invalidArgumentInNameOrDescription(exception);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void shouldHaveReturnResponseEntityWhenInvalidArgumentInCategoryIDsCalled(){
        UnexpectedTypeException exception = new UnexpectedTypeException("Check configuration for 'Message test'");
        ResponseEntity<?> responseEntity = catalogExceptionHandler.invalidArgumentInCategoryIDs(exception);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().toString().contains("Message test"));
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldHaveReturnResponseEntityWhenMethodResponseStatusExceptionCalled(){
        ResponseStatusException exception = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reason");
        ResponseEntity<?> responseEntity = catalogExceptionHandler.responseStatusException(exception);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Reason", responseEntity.getBody());
    }


}
