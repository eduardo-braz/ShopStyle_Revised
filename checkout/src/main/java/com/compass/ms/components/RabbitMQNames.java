package com.compass.ms.components;

public interface RabbitMQNames {

    static final String HISTORY_QUEUE_NAME = "history_ms";
    static final String HISTORY_EXCHANGE_NAME = "history_exchange";
    static final String HISTORY_ROUTING_KEY = "history";

    static final String CATALOG_QUEUE_NAME = "catalog_ms";
    static final String CATALOG_EXCHANGE_NAME = "catalog_exchange";
    static final String CATALOG_ROUTING_KEY = "catalog";

}
