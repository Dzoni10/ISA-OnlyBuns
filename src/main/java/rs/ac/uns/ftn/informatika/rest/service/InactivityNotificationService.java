package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.rest.domain.User;

import javax.mail.MessagingException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InactivityNotificationService {
    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public InactivityNotificationService(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 20 * * *")
    public void notifyInactiveUsers() throws MessagingException, InterruptedException {
        List<User> usersToNotify = filterInactiveUsers(userService.findAll());
        for (User user : usersToNotify) {
            try {
                emailService.sendInactivityEmail(user);
            } catch (MessagingException e) {
                // Log and continue to notify other users
                System.err.println("Failed to send email to user: " + user.getId());
                e.printStackTrace();
            }
        }
    }
    /*
    private List<User> filterUsers(List<User> users){
        List<User> filteredUsers = new ArrayList<User>();
        long currentTime = System.currentTimeMillis();

        for(User user : users){
            long lastActMilli =user.getLastActivity().atZone(ZoneId.of("Europe/Belgrade")).toInstant().toEpochMilli();
            long daysDifference = (currentTime - lastActMilli) / 24 / 60 / 60 / 1000;
            if(daysDifference > 7){
                filteredUsers.add(user);
            }
        }
        return filteredUsers;
    }
    */
    private List<User> filterInactiveUsers(List<User> users) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Belgrade"));

        // Stream to filter inactive users
        return users.stream()
                .filter(user -> {
                    ZonedDateTime lastActivity = user.getLastActivity().atZone(ZoneId.of("Europe/Belgrade"));
                    long daysDifference = ChronoUnit.DAYS.between(lastActivity, now);
                    return daysDifference > 7;
                })
                .collect(Collectors.toList());
    }
}
