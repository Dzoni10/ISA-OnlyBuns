package rs.ac.uns.ftn.informatika.rest.Interface.RepositoryInterfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.informatika.rest.domain.Following;

import java.util.List;

public interface FollowingRepositoryInterface extends JpaRepository<Following, Integer> {
    // Vraca paginiranu listu svih "following" zapisa
    Page<Following> findAll(Pageable pageable);

    // Pronadji "following" zapis na osnovu ID-a
    Following findFollowingById(int id);

    // Vraca listu svih koje prosledjeni username prati
    List<Following> findAllByUsername(String username);

    // Vraca listu svih koji prate prosledjeni username
    List<Following> findAllByFollowing(String following);

    // Provera da li korisnik prati drugog korisnika
    boolean existsByUsernameAndFollowing(String username, String following);

    // Vraca "following" zapis na osnovu username-a i korisnika kojeg prati
    Following findFollowingByUsernameAndFollowing(String username, String following);

}
