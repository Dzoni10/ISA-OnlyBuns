package rs.ac.uns.ftn.informatika.rest.Interface.RepositoryInterfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.informatika.rest.domain.Post;

import java.util.List;

public interface PostRepositoryInterface extends JpaRepository<Post, Integer> {

    public Page<Post> findAll(Pageable pageable);
    Post findPostById(int id);
    public List<Post> findAllByCreatorId(int creatorId);

}
