package rs.ac.uns.ftn.informatika.rest.RabbitMQ;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.rest.domain.Location;
import rs.ac.uns.ftn.informatika.rest.domain.RecommendedLocation;

@Component
public class RabbitMQConsumer {
    private static final Logger log = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // WebSocket za slanje na frontend

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues="${myqueue2}")
    public void receive(String message){

        try{
            RecommendedLocation location = objectMapper.readValue(message,RecommendedLocation.class);
            log.info("Consumer>" + message);
            // Slanje poruke na frontend putem WebSocket-a
            messagingTemplate.convertAndSend("/topic/locations", location);
        }catch(Exception e){
            log.error("Error with message processing: " + e.getMessage());
        }
    }
}
