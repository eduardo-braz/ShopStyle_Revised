package com.compass.ms.controller;

import com.compass.ms.DTO.PaymentDTO;
import com.compass.ms.DTO.PaymentFormDTO;
import com.compass.ms.entity.Instances;
import com.compass.ms.exception.CheckoutExceptionHandler;
import com.compass.ms.service.PaymentService;
import com.compass.ms.service.PurchasesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.Optional;

import static com.compass.ms.entity.Instances.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@DisplayName("Payment Controller Test")
public class PaymentControllerTest {

    @TestConfiguration
    static class PaymentControllerTestConfiguration{    }

    @Autowired
    PaymentController paymentController;

    @MockBean
    PaymentService paymentService;

    @MockBean
    PurchasesService purchasesService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CheckoutExceptionHandler(), paymentController).build();
    }

    @Test
    @DisplayName("Deve retornar status 201 ao salvar payment")
    public void shouldHaveReturnStatusCreatedWhenSavePayment() throws Exception {
        when(paymentService.save(any(PaymentFormDTO.class))).thenReturn(paymentDTO());
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentForm())))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect( result -> {
                            PaymentDTO paymentDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                                    PaymentDTO.class);
                            assertEquals(paymentDTO, paymentDTO());
                        } );
    }

    @Test
    @DisplayName("Deve retornar status 200 ao buscar todos payments")
    public void shouldHaveReturnStatusOKWhenGetAllPayments() throws Exception {
        when(paymentService.getAll()).thenReturn(Collections.emptyList());
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/v1/payments"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Deve retornar status 200 ao buscar um payment")
    public void shouldHaveReturnStatusOKWhenGetPaymentById() throws Exception {
        when(paymentService.getById(1L)).thenReturn(Optional.of(paymentDTO()));
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/v1/payments/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect( result -> {
                    PaymentDTO paymentDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                            PaymentDTO.class);
                    assertEquals(paymentDTO, paymentDTO());
                } );
    }

    @Test
    @DisplayName("Deve retornar Not Found ao buscar um payment não existente")
    public void shouldHaveReturnStatusNotFoundWhenGetInvalidPaymentById() throws Exception {
        when(paymentService.getById(1L)).thenReturn(Optional.empty());
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/v1/payments/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar Not Found ao tentar atualizar um payment não existente")
    public void shouldHaveReturnStatusNotFoundWhenUpdateInvalidPaymentById() throws Exception {
        when(paymentService.update(paymentForm(),1L)).thenReturn(Optional.empty());
        mockMvc.perform( MockMvcRequestBuilders
                        .put("/v1/payments/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentForm())))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar status 200 ao atualizar um payment")
    public void shouldHaveReturnStatusOKWhenUpdatePaymentById() throws Exception {
        when(paymentService.update(paymentForm(),1L)).thenReturn(Optional.of(paymentDTO()));
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/payments/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentForm())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    PaymentDTO paymentDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                            PaymentDTO.class);
                    assertEquals(paymentDTO, paymentDTO());
                });
    }


    @Test
    @DisplayName("Deve retornar status 200 ao deletar um payment")
    public void shouldHaveReturnStatusOKWhenDeletePaymentById() throws Exception {
        when(paymentService.delete(1L)).thenReturn(HttpStatus.OK);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/payments/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Deve retornar Not Found ao tentar deletar um payment")
    public void shouldHaveReturnNotFoundWhenDeletePayment() throws Exception {
        when(paymentService.delete(1L)).thenReturn(HttpStatus.NOT_FOUND);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/payments/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }









}
