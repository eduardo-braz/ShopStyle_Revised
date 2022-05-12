package com.compass.ms.service;

import com.compass.ms.DTO.*;
import com.compass.ms.DTO.messaging.HistoryDTO;
import com.compass.ms.clientEureka.CatalogClient;
import com.compass.ms.clientEureka.CustomerClient;
import com.compass.ms.entity.Cart;
import com.compass.ms.entity.Payment;
import com.compass.ms.entity.Purchases;
import com.compass.ms.repository.CartRepository;
import com.compass.ms.repository.PaymentRepository;
import com.compass.ms.repository.PurchasesRepository;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.compass.ms.components.RabbitMQNames.*;

@Service
public class PurchasesServiceImpl implements PurchasesService{

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private CustomerClient customerClient;

    @Autowired
    private CatalogClient catalogClient;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    PurchasesRepository purchasesRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Jackson2JsonMessageConverter jsonConverter;

    @Override
    public PurchasesDTO save(PurchasesFormDTO body) {
        Purchases purchase = new Purchases();
        if (validateUser(body.getUser_id()))
            if (validatePayment(body.getPayment_id()))
                purchase = makePurchase(body.getCart());

        // Salva User e Payment IDs compra
        purchase.setUser_id(body.getUser_id());
        purchase.setPayment_id(body.getPayment_id());

        // Prepara objeto de mensagem para catalog
        List<CartDTO> cartList = new ArrayList<>();
        body.getCart().forEach(item ->{
            cartList.add(modelMapper.map(item, CartDTO.class));
        });

        // Prepara objeto de mensagem para history
        HistoryDTO historyDTO = new HistoryDTO();
        historyDTO.setUser_id(body.getUser_id());
        historyDTO.setPayment_id(body.getPayment_id());
        historyDTO.setTotal(purchase.getPrice());
        historyDTO.setCart(body.getCart());

        // Envia mensagens, salva compra e retorna objeto salvo
        publishToCatalog(cartList);
        publishToHistory(historyDTO);
        PurchasesDTO saved = modelMapper.map(this.purchasesRepository.save(purchase), PurchasesDTO.class);
        return saved;
    }

    private boolean validateUser(Long id){
        UserDTO user = this.customerClient.findId(id);
        if (!user.isActive())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not active.");
        return true;
    }

    private boolean validatePayment(Long id){
        Optional<Payment> payment_id = this.paymentRepository.findById(id);
        if (!payment_id.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found.");
        if(!payment_id.get().isStatus())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment not active.");
        return true;
    }

    /* Verifica se produto está ativo, soma valor total de compra, salva carrinho no banco,
     *  garante que apenas os item de mesma variação sejam inseridos na compra
     */
    private Purchases makePurchase(List<CartFormDTO> cart){
        if (!(cart.size()>0))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty.");
        Purchases purchase = new Purchases();
        try {
            cart.stream().forEach( itemCart -> {
                ProductDTO product = this.catalogClient.findProductByIdVariation(itemCart.getVariant_id());
                if (!product.isActive())
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not active.");
                product.getVariations().stream().forEach( productVariation -> {
                    if (itemCart.getVariant_id().equals(productVariation.getId())) {
                        if (productVariation.getQuantity() < itemCart.getQuantity())
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity is less than available.");
                        BigDecimal parcialPrice =
                                productVariation.getPrice().multiply(BigDecimal.valueOf(itemCart.getQuantity()));
                        purchase.setPrice(purchase.getPrice().add(parcialPrice));
                        purchase.getCart().add(this.cartRepository.save(modelMapper.map(itemCart, Cart.class)));
                    }
                });
            });
            return purchase;
        } catch (ResponseStatusException e) { throw new ResponseStatusException(e.getStatus(), e.getReason()); }
    }

    private void publishToHistory(HistoryDTO messsage){
        try {
            rabbitTemplate.setMessageConverter(jsonConverter);
            rabbitTemplate.convertAndSend(HISTORY_QUEUE_NAME, messsage);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private void publishToCatalog(List<CartDTO> messsage){
        try {
            rabbitTemplate.setMessageConverter(jsonConverter);
            rabbitTemplate.convertAndSend(CATALOG_QUEUE_NAME, messsage);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

}
