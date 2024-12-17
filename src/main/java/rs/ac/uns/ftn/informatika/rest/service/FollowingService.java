package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.rest.domain.Following;
import rs.ac.uns.ftn.informatika.rest.Interface.RepositoryInterfaces.FollowingRepositoryInterface;

import java.util.List;

@Service
public class FollowingService {

    @Autowired
    private FollowingRepositoryInterface followingRepository;

    // Vrati listu svih koje prosledjeni korisnik prati
    public List<Following> getAllFollowedByUsername(String username) {
        return followingRepository.findAllByUsername(username);
    }

    // Vrati listu svih koji prate prosledjenog korisnika
    public List<Following> getAllFollowersByUsername(String following) {
        return followingRepository.findAllByFollowing(following);
    }

    // Dodaj novo praćenje
    public Following addFollowing(Following following) {
        return followingRepository.save(following);
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
