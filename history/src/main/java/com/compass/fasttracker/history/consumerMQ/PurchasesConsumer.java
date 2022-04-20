package com.compass.fasttracker.history.consumerMQ;

import com.compass.fasttracker.history.DTOs.Product;
import com.compass.fasttracker.history.DTOs.Purchase;
import com.compass.fasttracker.history.DTOs.messaging.ProductDTO;
import com.compass.fasttracker.history.DTOs.messaging.PurchasesDTO;
import com.compass.fasttracker.history.clientFeign.CatalogClient;
import com.compass.fasttracker.history.clientFeign.CheckoutClient;
import com.compass.fasttracker.history.clientFeign.CustomerClient;
import com.compass.fasttracker.history.models.Historic;
import com.compass.fasttracker.history.repository.HistoryRepository;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static com.compass.fasttracker.history.components.RabbitMQNames.*;

@Component
public class PurchasesConsumer {

    @Autowired
    private CustomerClient customerClient;

    @Autowired
    private CatalogClient catalogClient;

    @Autowired
    private CheckoutClient checkoutClient;

    @Autowired
    private HistoryRepository historyRepository;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(HISTORY_QUEUE_NAME),
            exchange = @Exchange(name = HISTORY_EXCHANGE_NAME),
            key = HISTORY_ROUTING_KEY))
    public void consumerHistory(final Message message, PurchasesDTO purchases) {
        Historic historic = new Historic();
        try{
            // Get user
            historic.setUser(this.customerClient.findById(purchases.getUser_id()));
            // Get payment
            Purchase purchase = new Purchase();
            purchase.setPaymentMethod(this.checkoutClient.findById(purchases.getPayment_id()));
            // Get products for each variation
            purchases.getCart().forEach(item -> {
                ProductDTO product = this.catalogClient.findProductByIdVariation(item.getVariant_id());
                purchase.getProducts().add(convertToProduct(product));
            });
            // Set date, total price and purchase
            historic.getPurchases().add(purchase);
            historic.setDate(purchases.getDate());
            historic.setTotal(purchases.getTotal());
            // Save historic
            this.historyRepository.save(historic);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private Product convertToProduct(ProductDTO productDTO){
        Product productFinal = new Product();
        productFinal.setDescription(productDTO.getDescription());
        productFinal.setName(productDTO.getName());
        productFinal.setColor(productDTO.getVariations().get(0).getColor());
        productFinal.setPrice(productDTO.getVariations().get(0).getPrice());
        productFinal.setSize(productDTO.getVariations().get(0).getSize());
        productFinal.setQuantity(productDTO.getVariations().get(0).getQuantity());
        return productFinal;
    }
}
