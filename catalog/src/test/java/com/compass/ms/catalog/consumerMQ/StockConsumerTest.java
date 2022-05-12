package com.compass.ms.catalog.consumerMQ;

import com.compass.ms.catalog.DTOs.CartDTO;
import com.compass.ms.catalog.models.Variation;
import com.compass.ms.catalog.repositories.VariationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.util.List;
import java.util.Optional;

import static com.compass.ms.catalog.models.Instances.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("Stock Consumer Test")
public class StockConsumerTest {

    @TestConfiguration
    static class VariationServiceTestConfiguration{
        @Bean
        public StockConsumer stockConsumer(){
            return new StockConsumer();
        }
    }

    @MockBean
    VariationRepository variationRepository;

    @Autowired
    StockConsumer stockConsumer;

    byte[] body = new HexBinaryAdapter().unmarshal("c000060000");
    Message message = new Message(body);


    @Test
    @DisplayName("Deve lançar ResponseStatusException ao tentar dar baixa em estoque por erro no banco de dados")
    public void shouldHaveThrowResponseStatusExceptionWhenTryUpdateStock()  {
        when(variationRepository.save(any())).thenThrow(ServerErrorException.class);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            stockConsumer.consumerHistory(message, cartDTOList());
        });
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, exception.getStatus());

    }

    @Test
    @DisplayName("Deve atualizar estoque corretamente")
    public void shouldHaveUpdateStockCorrectly() {
        Variation variationOne = variationOne();
        Variation variationTwo = variationTwo();
        List<CartDTO> cart = cartDTOList();

        when(variationRepository.findById(cart.get(0).getVariant_id()))
                .thenReturn(Optional.of(variationOne));
        when(variationRepository.findById(cart.get(1).getVariant_id()))
                .thenReturn(Optional.of(variationTwo));
        assertDoesNotThrow(()-> {
            stockConsumer.consumerHistory(message, cart);
        });
        assertEquals(4, variationOne.getQuantity());
        assertEquals(9, variationTwo.getQuantity());
        verify(variationRepository, times(cart.size())).save(any());
    }



}
