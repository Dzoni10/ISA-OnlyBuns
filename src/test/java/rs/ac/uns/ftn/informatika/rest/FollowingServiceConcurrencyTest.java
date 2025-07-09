package rs.ac.uns.ftn.informatika.rest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rs.ac.uns.ftn.informatika.rest.Interface.RepositoryInterfaces.FollowingRepositoryInterface;
import rs.ac.uns.ftn.informatika.rest.domain.Following;
import rs.ac.uns.ftn.informatika.rest.service.FollowingService;

import java.time.LocalDateTime;

@SpringBootTest
public class FollowingServiceConcurrencyTest {

    @Autowired
    private FollowingService followingService;

    @Autowired
    private FollowingRepositoryInterface followingRepository;

    @Test
    public void testConcurrentFollowings() throws InterruptedException {
        Runnable task = () -> {
            Following f = new Following("follower_" + Thread.currentThread().getId(), "targetUser", LocalDateTime.now());
            try {
                followingService.addFollowing(f);
            } catch (Exception e) {
                System.out.println("Greška u niti: " + e.getMessage());
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        Thread t3 = new Thread(task);

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        long count = followingRepository.countByFollowing("targetUser");
        System.out.println("Ukupan broj pratilaca za targetUser: " + count);
    }
}

