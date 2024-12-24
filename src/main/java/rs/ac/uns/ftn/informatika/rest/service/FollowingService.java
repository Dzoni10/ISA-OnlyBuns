package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.rest.domain.Following;
import rs.ac.uns.ftn.informatika.rest.Interface.RepositoryInterfaces.FollowingRepositoryInterface;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.informatika.rest.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.List;

@Service
public class FollowingService {

    @Autowired
    private FollowingRepositoryInterface followingRepository;
    private EntityManager entityManager;  // Za zaključavanje

    // Vrati listu svih koje prosledjeni korisnik prati
    public List<Following> getAllFollowedByUsername(String username) {
        return followingRepository.findAllByUsername(username);
    }

    // Vrati listu svih koji prate prosledjenog korisnika
    public List<Following> getAllFollowersByUsername(String following) {
        return followingRepository.findAllByFollowing(following);
    }

    // Dodaj novo praćenje
    @Transactional
    public Following addFollowing(Following following) {

        if (doesUserFollow(following.getUsername(), following.getFollowing())) {
            throw new IllegalArgumentException("User already follows this account!");
        }
        Following savedFollowing = followingRepository.save(following);

        // Simuliraj kašnjenje (za testiranje konkurentnosti)
        try {
            Thread.sleep(3000); // Simulacija za testiranje
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

         // Ažuriraj broj pratilaca direktno u bazi (opciono, može se logovati)
        long followerCount = followingRepository.countByFollowing(following.getFollowing());
        System.out.println("Updated follower count for " + following.getFollowing() + ": " + followerCount);

        return savedFollowing;
    }

    // Proveri da li korisnik prati nekoga
    public boolean doesUserFollow(String username, String following) {
        return followingRepository.existsByUsernameAndFollowing(username, following);
    }

    // Ukloni praćenje
    public void removeFollowing(int id) {
        followingRepository.deleteById(id);
    }
}
