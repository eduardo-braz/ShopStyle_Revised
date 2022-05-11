package com.compass.ms.exceptions;

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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("Exception Handler Test")
public class ExceptionHandlerTest {

    @InjectMocks
    CustumerExceptionHandler custumerExceptionHandler;

    @MockBean
    BindException bindException;

    @MockBean
    MethodParameter parameter;

    @Test
    public void shouldHaveReturnResponseEntityWhenMethodUserEmailFoundCalled(){
        EntityExceptionResponse exception = new EntityExceptionResponse(HttpStatus.BAD_REQUEST);
        exception.setMessage("Message test");
        ResponseEntity<?> responseEntity = custumerExceptionHandler.userEmailFound(exception);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().toString().contains("Message test"));
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldHaveReturnResponseEntityWhenMethodInvalidArgumentCalled(){
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindException);
        ResponseEntity<?> responseEntity = custumerExceptionHandler.invalidArgument(exception);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void shouldHaveReturnResponseEntityWhenMethodResponseStatusExceptionCalled(){
        ResponseStatusException exception = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reason");
        ResponseEntity<?> responseEntity = custumerExceptionHandler.responseStatusException(exception);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Reason", responseEntity.getBody());
    }

}
