package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.rest.domain.Comment;
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

    public List<Following> findAll() {
        return followingRepository.findAll();
    }


    // Vrati listu svih koje prosledjeni korisnik prati
    public List<Following> getAllFollowedByUsername(String username) {
        return followingRepository.findAllByUsername(username);
    }

    // Vrati listu svih koji prate prosledjenog korisnika
    public List<Following> getAllFollowersByUsername(String following) {
        return followingRepository.findAllByFollowing(following);
    }

    public List<Following> getAllByUserLastActivity(User user){
        return followingRepository.getAllByUserLastActivity(user.getUsername(), user.getLastActivity());
    }

    public Integer getAllByUserLastActivityCount(User user){
        return followingRepository.getAllByUserLastActivity(user.getUsername(), user.getLastActivity()).size();
    }

    // Dodaj novo praćenje
    @Transactional
    public Following addFollowing(Following following) {
        // Zaključaj sve zapise koji već prate istog korisnika
        entityManager.createQuery("SELECT f FROM Following f WHERE f.following = :following")
                .setParameter("following", following.getFollowing())
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getResultList();

        if (doesUserFollow(following.getUsername(), following.getFollowing())) {
            throw new IllegalArgumentException("User already follows this account!");
        }

        // Simuliraj kašnjenje
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Following saved = followingRepository.save(following);

        long followerCount = followingRepository.countByFollowing(following.getFollowing());
        System.out.println("Updated follower count for " + following.getFollowing() + ": " + followerCount);

        return saved;
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
