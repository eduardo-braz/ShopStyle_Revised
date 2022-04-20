package com.compass.ms.catalog.components;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

import static com.compass.ms.catalog.components.RabbitMQNames.*;


@Component
public class RabbitMQConfig {

    @Autowired
    AmqpAdmin amqpAdmin;

    private Queue queue(String queue){
        return new Queue(queue, true, false, false);
    }

    private DirectExchange directExchange(String name){
        return new DirectExchange(name);
    }

    private Binding binding(Queue queue, DirectExchange directExchange, String routingKey){
        return BindingBuilder.bind(queue).to(directExchange).with(routingKey);
    }

    @PostConstruct
    private void rabbitStarter(){
        Queue catalogQueue = queue(CATALOG_QUEUE_NAME);
        DirectExchange catalogDirectExchange = directExchange(CATALOG_EXCHANGE_NAME);
        Binding catalogBinding = binding(catalogQueue, catalogDirectExchange, CATALOG_ROUTING_KEY);

        amqpAdmin.declareQueue(catalogQueue);
        amqpAdmin.declareExchange(catalogDirectExchange);
        amqpAdmin.declareBinding(catalogBinding);


    }

}
