package rs.ac.uns.ftn.informatika.rest.Interface.ServiceInterfaces;

import rs.ac.uns.ftn.informatika.rest.domain.Greeting;
import rs.ac.uns.ftn.informatika.rest.dto.GreetingDTO;
import rs.ac.uns.ftn.informatika.rest.dto.GreetingTextDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public interface GreetingServiceInterface {
    Collection<Greeting> findAll();

    Greeting findOne(Long id);

    Greeting create(GreetingDTO greeting) throws Exception;

    Greeting update(GreetingDTO greeting, Long id) throws Exception;

    Greeting delete(Long id);

    Greeting updateGreetingText(GreetingTextDTO greetingDTO, long id) throws Exception;

    ArrayList<Greeting> searchGreetings(Optional<String> text);
}
