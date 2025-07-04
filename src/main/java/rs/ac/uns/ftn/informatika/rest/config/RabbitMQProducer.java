package rs.ac.uns.ftn.informatika.rest.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;
    private final FanoutExchange exchange;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate, FanoutExchange exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    public void sendMessage(String message) {
        System.out.println("Slanje poruke: " + message);
        rabbitTemplate.convertAndSend(exchange.getName(), "", message); // routingKey je prazan jer FanoutExchange ignoriše key
    }
}
