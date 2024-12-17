package rs.ac.uns.ftn.informatika.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.informatika.rest.service.UserService;

import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestExampleApplicationTests {

	@Autowired
	private UserService userService;
	@Test
	public void testPessimisticLocking() throws Throwable {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Future<?> future1 = executor.submit(() -> {
			System.out.println("Startovan Thread 1");
			userService.isUsernameExist("pera123");
			try { Thread.sleep(5000); } catch (InterruptedException e) { }
		});

		Future<?> future2 = executor.submit(() -> {
			System.out.println("Startovan Thread 2");
			try { Thread.sleep(200); } catch (InterruptedException e) { }
			userService.updateUserEmail("pera123", "newemail@example.com");
		});

		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.SECONDS);

		// Proverite eventualne izuzetke
		try {
			future1.get();
			future2.get();
		} catch (ExecutionException e) {
			throw e.getCause();
		}

	}

}
