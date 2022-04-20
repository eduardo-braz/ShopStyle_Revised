package com.compass.fasttracker.history.components;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

import static com.compass.fasttracker.history.components.RabbitMQNames.*;


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
        Queue historyQueue = queue(HISTORY_QUEUE_NAME);
        DirectExchange historyDirectExchange = directExchange(HISTORY_EXCHANGE_NAME);
        Binding historyBinding = binding(historyQueue, historyDirectExchange, HISTORY_ROUTING_KEY);

        amqpAdmin.declareQueue(historyQueue);
        amqpAdmin.declareExchange(historyDirectExchange);
        amqpAdmin.declareBinding(historyBinding);

    }

}
