package rs.ac.uns.ftn.informatika.rest.RabbitMQ;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public void sendMessage(Object message) {
        try{
            String jsonMessage = objectMapper.writeValueAsString(message);
            rabbitTemplate.convertAndSend("my-exchange", "routing-key", jsonMessage);
        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

}
