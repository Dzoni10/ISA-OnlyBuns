package rs.ac.uns.ftn.informatika.rest.Interface.RepositoryInterfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.informatika.rest.domain.Like;

import java.util.List;

public interface LikeRepositoryInterface extends JpaRepository<Like, Integer> {

    public Page<Like> findAll(Pageable pageable);

    Like findLikeById(int id);

    public List<Like> findAllByPostId(int postId);

    public List<Like> findAllByUsername(String username);

    boolean existsByPostIdAndUsername(int postId, String username);

    Like findLikeByPostIdAndUsername(int postId, String username);
}
