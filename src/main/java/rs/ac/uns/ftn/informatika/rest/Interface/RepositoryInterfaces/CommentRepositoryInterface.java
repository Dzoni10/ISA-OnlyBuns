package rs.ac.uns.ftn.informatika.rest.Interface.RepositoryInterfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.informatika.rest.domain.Comment;

import java.util.List;

public interface CommentRepositoryInterface extends JpaRepository<Comment, Integer> {

    public Page<Comment> findAll(Pageable pageable);

    Comment findCommentById(int id);

    public List<Comment> findAllByPostId(int postId);

    public List<Comment> findAllByUsername(String username);
}
