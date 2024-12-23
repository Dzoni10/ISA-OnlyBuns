package rs.ac.uns.ftn.informatika.rest.authentification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.rest.service.UserService;

import java.time.LocalDate;

@Component
public class ScheduledTasks {

    @Autowired
    private UserService userService;

    //@Scheduled(cron = "0 20 23 * * ?")
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteInactiveAccounts() {
        LocalDate today = LocalDate.now();
        if (today.getDayOfMonth() == today.lengthOfMonth()) {
            System.out.println("Danas je poslednji dan meseca. Brišem neaktivne korisnike.");
            userService.deleteInactiveUsers();
        } else {
            System.out.println("Danas nije poslednji dan meseca. Nalog se neće brisati.");
        }
    }
}
