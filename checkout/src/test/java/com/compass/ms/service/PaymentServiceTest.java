package com.compass.ms.service;

import com.compass.ms.DTO.PaymentDTO;
import com.compass.ms.DTO.PaymentFormDTO;
import com.compass.ms.entity.Payment;
import com.compass.ms.entity.PaymentType;
import com.compass.ms.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ServerErrorException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.compass.ms.entity.Instances.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Payment Testes")
public class PaymentServiceTest {

    @TestConfiguration
    static class PaymentServiceTestConfiguration{
        @Bean
        public PaymentService paymentService(){return new PaymentServiceImpl();}

        @Bean
        public ModelMapper modelMapper(){
            return new ModelMapper();
        }
    }

    @Autowired
    PaymentService paymentService;

    @Autowired
    ModelMapper modelMapper;

    @MockBean
    PaymentRepository paymentRepository;

    private Payment payment;
    private PaymentDTO paymentDTO;
    private PaymentFormDTO paymentFormDTO;

    @BeforeEach
    public void init(){
        payment = payment();
        paymentDTO = paymentDTO();
        paymentFormDTO = paymentForm();
    }

    @Test
    @DisplayName("Deve salvar um payment")
    public void shouldHaveSaveSucessfullPayment() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        PaymentDTO saved = assertDoesNotThrow(() -> paymentService.save(paymentFormDTO));
        assertNotNull(saved);
        assertEquals(saved, paymentDTO);
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar um payment")
    public void shouldHaveThrowExceptionWhenSavePayment(){
        when(paymentRepository.save(any())).thenThrow(ServerErrorException.class);
        try{
            paymentService.save(paymentFormDTO);
        } catch (ServerErrorException exception) {
            assertEquals("Database error.", exception.getReason());
            assertInstanceOf(ServerErrorException.class, exception);
        }
    }

    @Test
    @DisplayName("Deve buscar todos payments salvos no banco")
    public void shouldHaveGetAllPayments() {
        List<Payment> payments = new ArrayList<>();
        when(paymentRepository.findAll()).thenReturn(payments);
        assertDoesNotThrow(() -> paymentService.getAll());
    }

    @Test
    @DisplayName("Deve buscar um payment de {id}")
    public void shouldHaveGetPaymentById() {
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        Optional<PaymentDTO> found = assertDoesNotThrow(() -> paymentService.getById(1L));
        assertTrue(found.isPresent());
        assertInstanceOf(PaymentDTO.class, found.get());
    }

    @Test
    @DisplayName("Deve retornar Optional vazio ao buscar um payment de id inválido")
    public void shouldHaveReturnEmptyOptionalWhenGetPayment() {
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.empty());
        Optional<PaymentDTO> found = assertDoesNotThrow(() -> paymentService.getById(1L));
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Deve deletar um payment de {id}")
    public void shouldHaveDeletPaymentById() {
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        HttpStatus httpStatus = assertDoesNotThrow(() -> paymentService.delete(1L));
        assertEquals(HttpStatus.OK, httpStatus);
    }

    @Test
    @DisplayName("Deve retornar NOT FOUND ao tentar deletar um payment de id inválido")
    public void shouldHaveReturnEmptyOptionalWhenDeletePayment() {
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.empty());
        HttpStatus httpStatus = assertDoesNotThrow(() -> paymentService.delete(1L));
        assertEquals(HttpStatus.NOT_FOUND, httpStatus);
    }

    @Test
    @DisplayName("Deve atualizar um payment")
    public void shouldHaveUpdatePayment() {
        Payment paymentUpdate = new Payment(payment.getId(),PaymentType.CREDIT_CARD, BigDecimal.valueOf(3),true);
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any())).thenReturn(paymentUpdate);
        Optional<PaymentDTO> updated = assertDoesNotThrow(() -> paymentService.update(paymentFormDTO, payment.getId()));
        assertTrue(updated.isPresent());
        assertInstanceOf(PaymentDTO.class, updated.get());
        assertNotEquals(paymentUpdate, payment);
    }

    @Test
    @DisplayName("Deve retornar um Optional ao tentar atualizar payment com ID inválido")
    public void shouldHaveReturnEmptyOptionalWhenUpdatePayment() {
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.empty());
        Optional<PaymentDTO> updated = assertDoesNotThrow(() -> paymentService.getById(anyLong()));
        assertFalse(updated.isPresent());
    }
}
