package com.compass.ms.entity;

import com.compass.ms.DTO.PaymentFormDTO;
import com.compass.ms.DTO.PurchasesFormDTO;
import com.compass.ms.controller.PaymentController;
import com.compass.ms.controller.PurchasesController;
import com.compass.ms.entity.PaymentType;
import com.compass.ms.exception.CheckoutExceptionHandler;
import com.compass.ms.service.PaymentService;
import com.compass.ms.service.PurchasesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.util.Collections;

import static com.compass.ms.entity.Instances.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
/*
@SpringBootTest
@AutoConfigureMockMvc
 */
@DisplayName("Testes de validação de requisição (Body)")
public class ValidationFormBodyTest {

    @TestConfiguration
    static class ValidationFormBodyTestConfiguration{    }

    @MockBean
    PurchasesService purchasesService;

    @MockBean
    PaymentService paymentService;

    @Autowired
    PaymentController paymentController;

    @Autowired
    PurchasesController purchasesController;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private PaymentFormDTO invalidPayment = paymentForm();
    private PurchasesFormDTO invalidPurchase = purchasesForm();

    @BeforeEach
    public void setup() {
       mockMvc = MockMvcBuilders.standaloneSetup(new CheckoutExceptionHandler(),
               paymentController, purchasesController).build();
    }

    private void paymentPerformArgumentTester(String fieldErrorExpected, String messageExpected) throws Exception {
        when(paymentService.save(invalidPayment)).thenReturn(paymentDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPayment)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(
                        result -> {
                            assertTrue( result.getResolvedException() instanceof MethodArgumentNotValidException);
                            FieldError fieldError =
                                    ((MethodArgumentNotValidException) result.getResolvedException()).getFieldError();
                            assertEquals(fieldErrorExpected, fieldError.getField());
                            assertEquals(messageExpected, fieldError.getDefaultMessage());
                        }
                );
    }

    private void purchasePerformArgumentTester(String fieldErrorExpected, String messageExpected) throws Exception {
        when(purchasesService.save(invalidPurchase)).thenReturn(purchasesDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPurchase)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(
                        result -> {
                            assertTrue( result.getResolvedException() instanceof MethodArgumentNotValidException);
                            FieldError fieldError =
                                    ((MethodArgumentNotValidException) result.getResolvedException()).getFieldError();
                            assertEquals(fieldErrorExpected, fieldError.getField());
                            assertEquals(messageExpected, fieldError.getDefaultMessage());
                        }
                );
    }

    @Test
    @DisplayName("Lança Invalid Value Exception por desconto menor que zero")
    public void handleInvalidValueExceptionInPaymentBecauseDiscountLessThanZero() throws Exception {
        invalidPayment.setDiscount(BigDecimal.valueOf(-1));
        String fieldErrorExpected = "discount";
        String messageExpected = "must be greater than or equal to 0";
        paymentPerformArgumentTester(fieldErrorExpected, messageExpected);
    }

    @Test
    @DisplayName("Lança Invalid Value Exception por type nulo")
    public void handleInvalidValueExceptionInPaymentBecauseTypeIsNull() throws Exception {
        invalidPayment.setType(null);
        String fieldErrorExpected = "type";
        String messageExpected = "must not be null";
        paymentPerformArgumentTester(fieldErrorExpected, messageExpected);
    }

    @Test
    @DisplayName("Lança Illegal Argument Exception por type inválido")
    public void handleIllegalArgumentExceptionInPaymentBecauseInvalidType() {
        String value = "abcd";
        IllegalArgumentException argumentException = assertThrows(IllegalArgumentException.class, () -> {
            invalidPayment.setType(PaymentType.setValue(value));
        });

        assertEquals("Invalid value \'"+ value +"\'", argumentException.getMessage());

    }

    @Test
    @DisplayName("Lança Invalid Value Exception por desconto nulo")
    public void handleInvalidValueExceptionInPaymentBecauseDiscountIsNull() throws Exception {
        invalidPayment.setDiscount(null);
        String fieldErrorExpected = "discount";
        String messageExpected = "must not be null";
        paymentPerformArgumentTester(fieldErrorExpected, messageExpected);

    }

    @Test
    @DisplayName("Lança Invalid Value Exception por user_id nulo")
    public void handleInvalidValueExceptionInPurchaseUserIdIsNull() throws Exception {
        invalidPurchase.setUser_id(null);
        String fieldErrorExpected = "user_id";
        String messageExpected = "must not be null";
        purchasePerformArgumentTester(fieldErrorExpected, messageExpected);

    }

    @Test
    @DisplayName("Lança Invalid Value Exception por payment_id nulo")
    public void handleInvalidValueExceptionInPurchasePaymentIdIsNull() throws Exception {
        invalidPurchase.setPayment_id(null);
        String fieldErrorExpected = "payment_id";
        String messageExpected = "must not be null";
        purchasePerformArgumentTester(fieldErrorExpected, messageExpected);

    }

    @Test
    @DisplayName("Lança Invalid Value Exception por cart nulo")
    public void handleInvalidValueExceptionInPurchaseCartIsNull() throws Exception {
        invalidPurchase.setCart(null);
        String fieldErrorExpected = "cart";
        String messageExpected = "must not be empty";
        purchasePerformArgumentTester(fieldErrorExpected, messageExpected);

    }

    @Test
    @DisplayName("Lança Invalid Value Exception por cart vazio")
    public void handleInvalidValueExceptionInPurchaseCartIsEmpty() throws Exception {
        invalidPurchase.setCart(Collections.EMPTY_LIST);
        String fieldErrorExpected = "cart";
        String messageExpected = "must not be empty";
        purchasePerformArgumentTester(fieldErrorExpected, messageExpected);

    }

}
