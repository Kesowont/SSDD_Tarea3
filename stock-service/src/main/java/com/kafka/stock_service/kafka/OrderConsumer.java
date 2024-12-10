package com.kafka.stock_service.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.kafka.base_domains.OrderEvent;
import com.kafka.stock_service.data.Orden;
import com.kafka.stock_service.data.OrdenRepository;

@Service
public class OrderConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);
    private final OrdenRepository ordenRepository;

    public OrderConsumer(OrdenRepository ordenRepository) {
        this.ordenRepository = ordenRepository;
    }

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(OrderEvent orderEvent) {
        LOGGER.info(String.format("Order event received in stock service => %s", orderEvent.toString()));

        // Guardar la orden en la base de datos
        Orden orden = new Orden(
            orderEvent.getOrder().getOrderId(),
            orderEvent.getOrder().getName(),
            orderEvent.getOrder().getQty(),
            orderEvent.getOrder().getPrice()
        );
        ordenRepository.save(orden);
        LOGGER.info("Order saved in database successfully.");
    }
}
