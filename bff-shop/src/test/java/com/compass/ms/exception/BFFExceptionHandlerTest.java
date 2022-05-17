package com.compass.ms.exception;

import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("BFF Exception Handler Test")
public class BFFExceptionHandlerTest {

    @TestConfiguration
    public static class HistoryExceptionHandlerTestConfiguration {
        @Bean
        public BFFExceptionHandler init(){
            return new BFFExceptionHandler();
        }
    }

    @Autowired
    BFFExceptionHandler bffExceptionHandler;

    @Test
    @DisplayName("Deve retornar Response Entity ao lançar Feign Exception")
    public void shouldHaveReturnResponseEntityWhenFeignExceptionCalled() {
        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(404);
        ResponseEntity<?> responseEntity = bffExceptionHandler.feignNotFoundException(feignException);
        assertEquals(404, responseEntity.getStatusCode().value());
    }




}
