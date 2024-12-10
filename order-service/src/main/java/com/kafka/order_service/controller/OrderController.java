package com.kafka.order_service.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kafka.base_domains.Order;
import com.kafka.base_domains.OrderEvent;
import com.kafka.order_service.kafka.OrderProducer;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderProducer orderProducer;

    public OrderController(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @PostMapping("/orders")
    public String placeOrder(@RequestBody Order order) {
        // Generar un ID único para la orden
        order.setOrderId(UUID.randomUUID().toString());

        // Crear el evento de la orden
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setStatus("PENDING");
        orderEvent.setMessage("Order is in pending state");
        orderEvent.setOrder(order);

        // Enviar el evento al productor Kafka
        orderProducer.sendMessage(orderEvent);

        return "Order placed successfully!";
    }
}