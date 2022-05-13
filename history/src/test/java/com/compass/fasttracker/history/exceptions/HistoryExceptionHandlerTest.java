package com.compass.fasttracker.history.exceptions;

import com.compass.fasttracker.history.service.HistoryService;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("History Exception Handler Test")
public class HistoryExceptionHandlerTest {

    @TestConfiguration
    public static class HistoryExceptionHandlerTestConfiguration{
        @Bean
        public HistoryExceptionHandler historyExceptionHandler(){
            return new HistoryExceptionHandler();
        }
    }

    @Autowired
    HistoryExceptionHandler historyExceptionHandler;

    @MockBean
    HistoryService historyService;

    @MockBean
    BindException bindException;

    @MockBean
    MethodParameter parameter;

    @Test
    @DisplayName("Deve retornar Response Entity ao lançar InvalidArgument")
    public void shouldHaveReturnResponseEntityWhenMethodInvalidArgumentCalled(){
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindException);
        ResponseEntity<?> responseEntity = historyExceptionHandler.invalidValueArgument(exception);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }


    @Test
    @DisplayName("Deve retornar Response Entity ao lançar Value Instantiation")
    public void shouldHaveReturnResponseEntityWhenValueInstantiationExceptionCalled(){
        ValueInstantiationException exception = mock(ValueInstantiationException.class);
        when(exception.getOriginalMessage()).thenReturn("Invalid field");
        ResponseEntity<?> responseEntity = historyExceptionHandler.messageNotReadable(exception);
        assertEquals(400, responseEntity.getStatusCode().value());
        assertTrue(responseEntity.getBody().toString().contains("\"error\": \"Invalid field\""));
    }

    @Test
    @DisplayName("Deve retornar Response Entity ao lançar Invalid Argument")
    public void shouldHaveReturnResponseEntityWhenInvalidArgumentInNameOrDescriptionCalled(){
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindException);
        ResponseEntity<?> responseEntity = historyExceptionHandler.invalidValueArgument(exception);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Deve retornar Response Entity ao lançar Feign Exception")
    public void shouldHaveReturnResponseEntityWhenFeignExceptionCalled() {
        FeignException feignException = mock(FeignException.class);
        Request request = mock(Request.class);
        RequestTemplate requestTemplate = mock(RequestTemplate.class);
        when(feignException.request()).thenReturn(request);
        when(request.requestTemplate()).thenReturn(requestTemplate);
        when(requestTemplate.path()).thenReturn("/v1/checkout/001");
        when(feignException.status()).thenReturn(404);
        ResponseEntity<?> responseEntity = historyExceptionHandler.feignNotFoundException(feignException);
        assertEquals(404, responseEntity.getStatusCode().value());
        assertTrue(responseEntity.getBody().toString().contains("\"field\": \"user_id\""));
        assertTrue(responseEntity.getBody().toString().contains("\"error\": \"Not Found user_id 001\""));
    }





}
