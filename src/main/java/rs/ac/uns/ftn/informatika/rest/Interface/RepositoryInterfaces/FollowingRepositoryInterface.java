package rs.ac.uns.ftn.informatika.rest.Interface.RepositoryInterfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.uns.ftn.informatika.rest.domain.Following;

import java.time.LocalDateTime;
import java.util.List;

public interface FollowingRepositoryInterface extends JpaRepository<Following, Integer> {
    // Vraca paginiranu listu svih "following" zapisa
    Page<Following> findAll(Pageable pageable);

    // Pronadji "following" zapis na osnovu ID-a
    Following findFollowingById(int id);

    // Vraca listu svih koje prosledjeni username prati
    @Query("SELECT f FROM Following f WHERE f.username = :username")
    List<Following> findAllByUsername(@Param("username") String username);

    // Vraca listu svih koji prate prosledjeni username
    @Query("SELECT f FROM Following f WHERE f.following = :following")
    List<Following>  findAllByFollowing(@Param("following") String following);

    // Provera da li korisnik prati drugog korisnika
    boolean existsByUsernameAndFollowing(String username, String following);

    // Vraca "following" zapis na osnovu username-a i korisnika kojeg prati
    Following findFollowingByUsernameAndFollowing(String username, String following);

    @Query("SELECT f FROM Following f WHERE f.username = :username AND f.creationTime > :lastActivity")
    List<Following> getAllByUserLastActivity(@Param("username") String username, @Param("lastActivity") LocalDateTime lastActivity);
    // Prebroj pratioce za ciljanu osobu
    long countByFollowing(String following);

}
