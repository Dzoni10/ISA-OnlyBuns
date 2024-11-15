package rs.ac.uns.ftn.informatika.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.informatika.rest.domain.User;
import rs.ac.uns.ftn.informatika.rest.service.EmailService;
import rs.ac.uns.ftn.informatika.rest.service.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestExampleApplicationTests {
	private UserService userService = Mockito.mock(UserService.class);
	private EmailService emailService = Mockito.mock(EmailService.class);
	@Test
	public void contextLoads() {
	}

	@Test
	public void testNotifyInactiveUsers() throws Exception {
		// Arrange: Add mock data for inactive users
		List<User> users = Arrays.asList(
				new User(1, "Mika", "Mikic", "mika123", "123", "mika@gmail", "Ulica122000", true, false, LocalDateTime.now().minusDays(15)),
				new User(2, "Mila", "Milic", "mila123", "123", "mila@gmail", "Ulica122000", true, false, LocalDateTime.now().minusDays(8))
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

}
