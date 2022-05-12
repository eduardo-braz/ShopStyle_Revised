package com.compass.ms.exception;

import com.compass.ms.entity.Instances;
import com.compass.ms.service.PaymentService;
import com.compass.ms.service.PurchasesService;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest
public class CheckoutExceptionHandlerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CheckoutExceptionHandler checkoutExceptionHandler;

    @MockBean
    PurchasesService purchasesService;

    @MockBean
    PaymentService paymentService;

    @MockBean
    BindException bindException;

    @MockBean
    MethodParameter parameter;

    @BeforeEach
    public void setup(){
     this.mockMvc = MockMvcBuilders.standaloneSetup(new CheckoutExceptionHandler()).build();
    }

    @Test
    public void shouldHaveReturnResponseEntityWhenResponseStatusExceptionCalled(){
        ResponseStatusException exception = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reason");
        when(paymentService.save(Instances.paymentForm())).thenAnswer(invocation -> {
                ResponseEntity<?> responseEntity = checkoutExceptionHandler.responseStatusException(exception);
                assertEquals(400, responseEntity.getStatusCode().value());
                assertEquals("Reason", responseEntity.getBody());
                return null;
                }
        );
        paymentService.save(Instances.paymentForm());
    }


    @Test
    public void shouldHaveReturnResponseEntityWhenValueInstantiationExceptionCalled(){
        ValueInstantiationException exception = mock(ValueInstantiationException.class);
        when(exception.getOriginalMessage()).thenReturn("Invalid field");
        ResponseEntity<?> responseEntity = checkoutExceptionHandler.messageNotReadable(exception);
        assertEquals(400, responseEntity.getStatusCode().value());
        assertTrue(responseEntity.getBody().toString().contains("\"error\": \"Invalid field\""));
    }

    @Test
    public void shouldHaveReturnResponseEntityWhenInvalidArgumentInNameOrDescriptionCalled(){
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindException);
        ResponseEntity<?> responseEntity = checkoutExceptionHandler.invalidValueArgument(exception);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void shouldHaveReturnResponseEntityWhenFeignExceptionCalled() {
        FeignException feignException = mock(FeignException.class);
        Request request = mock(Request.class);
        RequestTemplate requestTemplate = mock(RequestTemplate.class);
        when(feignException.request()).thenReturn(request);
        when(request.requestTemplate()).thenReturn(requestTemplate);
        when(requestTemplate.path()).thenReturn("/v1/checkout/001");
        when(feignException.status()).thenReturn(404);
        ResponseEntity<?> responseEntity = checkoutExceptionHandler.feignNotFoundException(feignException);
        assertEquals(404, responseEntity.getStatusCode().value());
        assertTrue(responseEntity.getBody().toString().contains("\"field\": \"user_id\""));
        assertTrue(responseEntity.getBody().toString().contains("\"error\": \"Not Found user_id 001\""));
    }





}
