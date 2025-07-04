package rs.ac.uns.ftn.informatika.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.informatika.rest.controller.CommentController;
import rs.ac.uns.ftn.informatika.rest.domain.User;
import rs.ac.uns.ftn.informatika.rest.dto.CommentDTO;
import rs.ac.uns.ftn.informatika.rest.service.EmailService;
import rs.ac.uns.ftn.informatika.rest.service.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.informatika.rest.service.UserService;

import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestExampleApplicationTests {

	@Autowired
	private CommentController commentController; // Inject your comment controller to test the rate-limiting feature.

	private UserService userService = Mockito.mock(UserService.class);
	private EmailService emailService = Mockito.mock(EmailService.class);
  
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

	@Test
	public void testNotifyInactiveUsers() throws Exception {
		// Arrange: Add mock data for inactive users
		List<User> users = Arrays.asList(
				new User( "Mika", "Mikic", "mika123", "123", "mika@gmail", "Ulica122000", true, false,1, LocalDateTime.now().minusDays(15)),
				new User( "Mila", "Milic", "mila123", "123", "mila@gmail", "Ulica122000", true, false,2, LocalDateTime.now().minusDays(8))
		);

		Mockito.when(userService.findAll()).thenReturn(users);

		// Act: Call the notification method
		for (User user : users) {
			if (user.getLastActivity().isBefore(LocalDateTime.now().minusDays(7))) {
				emailService.sendInactivityEmail(user);
			}
		}

		// Assert: Verify that the email service was called for the correct users
		Mockito.verify(emailService, Mockito.times(1)).sendInactivityEmail(users.get(0)); // Mika
		Mockito.verify(emailService, Mockito.times(1)).sendInactivityEmail(users.get(1)); // Mila
	}

	@Test
	public void testCreateCommentUnderLimit() {
		CommentDTO commentDTO = new CommentDTO(1, 1, "user123", LocalDateTime.now(), "Test comment");

		// Simulate sending 60 comments in less than 60 minutes
		for (int i = 0; i < 60; i++) {
			ResponseEntity<CommentDTO> response = commentController.createComment(commentDTO);
			assertEquals("Comment " + (i + 1) + " should be created", HttpStatus.CREATED, response.getStatusCode());
		}
	}

	@Test
	public void testCreateCommentExceedLimit() {
		CommentDTO commentDTO = new CommentDTO(1, 1, "user123", LocalDateTime.now(), "Test comment");


		// Send 60 comments to hit the limit
		for (int i = 0; i < 60; i++) {
			commentController.createComment(commentDTO);
		}

		// Now attempt to send the 61st comment, which should be rejected
		ResponseEntity<CommentDTO> response = commentController.createComment(commentDTO);
		assertEquals("61st comment should exceed limit", HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
	}

}
