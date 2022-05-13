package com.compass.fasttracker.history.consumerMQ;

import com.compass.fasttracker.history.DTOs.messaging.ProductDTO;
import com.compass.fasttracker.history.Instance.Instances;
import com.compass.fasttracker.history.clientFeign.CatalogClient;
import com.compass.fasttracker.history.clientFeign.CheckoutClient;
import com.compass.fasttracker.history.clientFeign.CustomerClient;
import com.compass.fasttracker.history.models.Historic;
import com.compass.fasttracker.history.repository.HistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerErrorException;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("Purchases Consumer Test")
public class PurchasesConsumerTest {

    @TestConfiguration
    static class PurchasesConsumerTestConfiguration{
        @Bean
        public PurchasesConsumer init(){
            return new PurchasesConsumer();
        }
    }

    @MockBean
    CustomerClient customerClient;

    @MockBean
    CatalogClient catalogClient;

    @MockBean
    CheckoutClient checkoutClient;

    @MockBean
    HistoryRepository historyRepository;

    @Autowired
    PurchasesConsumer purchasesConsumer;

    byte[] body = new HexBinaryAdapter().unmarshal("c000060000");
    Message message = new Message(body);


    @Test
    @DisplayName("Deve consumir compra do RabbitMQ e salvar historico")
    public void shouldHaveConsumerPurchaseAndSaveHistoric(){
        when(customerClient.findById(anyLong())).thenReturn(Instances.user());
        when(checkoutClient.findById(anyLong())).thenReturn(Instances.payment());
        when(catalogClient.findProductByIdVariation(anyString())).thenReturn(Instances.productDTO());

        purchasesConsumer.consumerHistory(message, Instances.purchasesDTO());

        verify(customerClient, times(1)).findById(anyLong());
        verify(checkoutClient, times(1)).findById(anyLong());
        verify(catalogClient, times(Instances.productDTO().getVariations().size())).findProductByIdVariation(anyString());
        verify(historyRepository, times(1)).save(any(Historic.class));
    }

    @Test
    @DisplayName("Deve lançar ResponseStatusException quando tentar consumir compra e houver erro")
    public void shouldHaveThrowResponseStatusExceptionWhenTryConsumerPurchaseAndSaveHistoric(){
        ProductDTO product = new ModelMapper().map(Instances.productList().get(0), ProductDTO.class);
        when(customerClient.findById(anyLong())).thenReturn(Instances.user());
        when(checkoutClient.findById(anyLong())).thenReturn(Instances.payment());
        when(catalogClient.findProductByIdVariation(anyString())).thenThrow(ServerErrorException.class);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            purchasesConsumer.consumerHistory(message, Instances.purchasesDTO());
        });
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, exception.getStatus());
        verify(historyRepository, times(0)).save(any(Historic.class));
    }


}
