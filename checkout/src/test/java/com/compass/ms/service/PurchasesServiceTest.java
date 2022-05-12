package com.compass.ms.service;

import com.compass.ms.DTO.PurchasesDTO;
import com.compass.ms.DTO.PurchasesFormDTO;
import com.compass.ms.DTO.UserDTO;
import com.compass.ms.DTO.messaging.HistoryDTO;
import com.compass.ms.clientEureka.CatalogClient;
import com.compass.ms.clientEureka.CustomerClient;
import com.compass.ms.entity.*;
import com.compass.ms.repository.CartRepository;
import com.compass.ms.repository.PaymentRepository;
import com.compass.ms.repository.PurchasesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static com.compass.ms.components.RabbitMQNames.CATALOG_QUEUE_NAME;
import static com.compass.ms.components.RabbitMQNames.HISTORY_QUEUE_NAME;
import static com.compass.ms.entity.Instances.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Purchases Testes")
public class PurchasesServiceTest {

    @TestConfiguration
    static class PurchasesServiceTestConfiguration{

        @Bean
        public PurchasesService purchasesService(){return new PurchasesServiceImpl();}

        @Bean
        public RestTemplate getRestTamplate(){ return new RestTemplate(); }

        @Bean
        public ModelMapper modelMapper(){
            return new ModelMapper();
        }
    }

    @Autowired
    PurchasesService purchasesService;

    @Autowired
    ModelMapper modelMapper;

    @MockBean
    Jackson2JsonMessageConverter jsonConverter;

    @MockBean
    PurchasesRepository purchasesRepository;

    @MockBean
    PaymentRepository paymentRepository;

    @MockBean
    CartRepository cartRepository;

    @MockBean
    private CustomerClient customerClient;

    @MockBean
    private CatalogClient catalogClient;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    private Purchases purchases;
    private PurchasesFormDTO purchasesForm;

    @BeforeEach
    public void beforeEach(){
        when(customerClient.findId(anyLong())).thenReturn(new UserDTO(1L, true));
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment()));
        when(catalogClient.findProductByIdVariation(anyString())).thenReturn(firstProductDTO());
        when(cartRepository.save(any(Cart.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        when(purchasesRepository.save(any(Purchases.class))).thenReturn(purchases());

        this.purchases = purchases();
        this.purchasesForm = purchasesForm();
    }

    @Test
    @DisplayName("Deve salvar uma compra")
    public void shouldHaveSaveSucessfullPurchases(){
        PurchasesDTO saved = purchasesService.save(purchasesForm);

        verify(customerClient, times(1)).findId(purchasesForm().getUser_id());
        verify(paymentRepository, times(1)).findById(purchasesForm().getPayment_id());
        verify(catalogClient, atLeastOnce()).findProductByIdVariation(anyString());
        verify(cartRepository, times(1)).save(any(Cart.class));
        verify(rabbitTemplate,times(2)).setMessageConverter(jsonConverter);
        verify(rabbitTemplate,times(1))
                .convertAndSend(eq(HISTORY_QUEUE_NAME), any(HistoryDTO.class));
        verify(rabbitTemplate,times(1))
                .convertAndSend(eq(CATALOG_QUEUE_NAME), anyList());
        verify(purchasesRepository, times(1)).save(any(Purchases.class));

        assertEquals(purchasesDTO(), saved);
        assertEquals(purchases.getPrice(), saved.getPrice());
    }

    @Test
    @DisplayName("Não deve salvar uma compra devido usuário não está ativo")
    public void shouldNotSavePurchaseBecauseUserNotActive() {
        when(customerClient.findId(anyLong())).thenReturn(new UserDTO(1L, false));
        try {
            purchasesService.save(purchasesForm);
        } catch (ResponseStatusException e) {
            assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
            assertEquals(e.getReason(), "User not active.");
        }

        verify(customerClient, times(1)).findId(purchasesForm().getUser_id());
        verifyNoInteractions(cartRepository);
        verifyNoInteractions(rabbitTemplate);
        verifyNoInteractions(purchasesRepository);
    }

    @Test
    @DisplayName("Não deve salvar uma compra devido payment não está ativo")
    public void shouldNotSavePurchaseBecausePaymentNotActive(){
        Payment payment =
                new Payment(1L, PaymentType.DEBIT, BigDecimal.valueOf(10), false);

        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        try {
            purchasesService.save(purchasesForm);
        } catch (ResponseStatusException e){
            assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
            assertEquals(e.getReason(), "Payment not active.");
        }

        verify(paymentRepository, times(1)).findById(purchasesForm().getPayment_id());
        verifyNoInteractions(cartRepository);
        verifyNoInteractions(rabbitTemplate);
        verifyNoInteractions(purchasesRepository);
    }

    @Test
    @DisplayName("Não deve salvar uma compra devido payment não existe no BD")
    public void shouldNotSavePurchaseBecausePaymentDoesNotExist(){
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.empty());
        try {
            purchasesService.save(purchasesForm);
        } catch (ResponseStatusException e){
            assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
            assertEquals(e.getReason(), "Payment not found.");
        }
        verify(paymentRepository, times(1)).findById(purchasesForm().getPayment_id());
        verifyNoInteractions(cartRepository);
        verifyNoInteractions(rabbitTemplate);
        verifyNoInteractions(purchasesRepository);
    }

    @Test
    @DisplayName("Não deve salvar devido erro conexão RabbitMQ")
    public void shouldNotSavePurchaseBecauseConnectionErrorRabbitMQ(){
        doThrow(ResponseStatusException.class).when(rabbitTemplate)
                .convertAndSend(eq(HISTORY_QUEUE_NAME), any(HistoryDTO.class));
        doThrow(ResponseStatusException.class).when(rabbitTemplate)
                .convertAndSend(eq(CATALOG_QUEUE_NAME), anyList());
        try {
            purchasesService.save(purchasesForm);
        } catch (ResponseStatusException e){
            assertEquals(e.getStatus(), HttpStatus.SERVICE_UNAVAILABLE);
        }

        verify(rabbitTemplate,times(1)).setMessageConverter(jsonConverter);
        verify(rabbitTemplate, times(0)).convertAndSend(eq(HISTORY_QUEUE_NAME), any(HistoryDTO.class));
        verifyNoInteractions(purchasesRepository);
    }

    @Test
    @DisplayName("Não deve salvar uma compra com carrinho vazio")
    public void shouldNotSavePurchaseWithEmptyCart(){
        purchasesForm.setCart(Collections.EMPTY_LIST);
        try {
            purchasesService.save(purchasesForm);
        } catch (ResponseStatusException e){
            assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
            assertEquals(e.getReason(), "Cart is empty.");
        }

        verifyNoInteractions(purchasesRepository);
        verifyNoInteractions(cartRepository);
    }

    @Test
    @DisplayName("Deve calcular corretamente valor total de compra")
    public void shouldCalculateTotalPricePurchase(){
        when(purchasesRepository.save(any(Purchases.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        when(catalogClient.findProductByIdVariation(cartList().get(0).getVariant_id()))
                .thenReturn(firstProductDTO());
        when(catalogClient.findProductByIdVariation(cartList().get(1).getVariant_id()))
                .thenReturn(secondProductDTO());

        BigDecimal total = BigDecimal.valueOf(289.60);

        PurchasesDTO saved = purchasesService.save(purchasesForm);
        assertEquals(total, saved.getPrice());
    }

    @Test
    @DisplayName("Não deve salvar uma compra se quantidade solicitada " +
            "é menor que disponível no estoque")
    public void shouldNotSavePurchaseIfQuantityIsLessThanAvaliable(){
        purchasesForm.getCart().get(0).setQuantity(50);
        try {
            purchasesService.save(purchasesForm);
        } catch (ResponseStatusException e){
            assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
            assertEquals(e.getReason(), "Quantity is less than available.");
        }
        verifyNoInteractions(purchasesRepository);
        verifyNoInteractions(rabbitTemplate);
    }


}