package rs.ac.uns.ftn.informatika.rest.Interface.RepositoryInterfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import rs.ac.uns.ftn.informatika.rest.domain.Greeting;

public interface GreetingRepositoryInterface {

	Collection<Greeting> findAll();

	Greeting create(Greeting greeting);

	Greeting findOne(Long id);
	
	Greeting update(Greeting greeting);

	Greeting delete(Long id);

	ArrayList<Greeting> searchByText(Optional<String> text);
}
