package com.compass.ms.catalog.consumerMQ;

import com.compass.ms.catalog.DTOs.CartDTO;
import com.compass.ms.catalog.models.Variation;
import com.compass.ms.catalog.repositories.VariationRepository;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static com.compass.ms.catalog.components.RabbitMQNames.*;

@Component
public class StockConsumer {

    @Autowired
    private VariationRepository variationRepository;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(CATALOG_QUEUE_NAME),
            exchange = @Exchange(name = CATALOG_EXCHANGE_NAME),
            key = CATALOG_ROUTING_KEY))
    public void consumerHistory(final Message message, List<CartDTO> cartList) {
        try {
            cartList.forEach(item -> {
                Optional<Variation> itemCatalog = this.variationRepository.findById(item.getVariant_id());
                itemCatalog.get().setQuantity(itemCatalog.get().getQuantity() - item.getQuantity());
                this.variationRepository.save(itemCatalog.get());
            });
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

}
