package com.onlybuns.advertiser;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {

    // Ova metoda će se pozvati kad god stigne poruka na specificirani red
    @RabbitListener(queues = "${rabbitmq.queue}")
    public void consumeMessage(String message) {
        System.out.println("Primljena poruka: " + message);

    }
}
