package rs.ac.uns.ftn.informatika.rest.Interface.RepositoryInterfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.uns.ftn.informatika.rest.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepositoryInterface extends JpaRepository<Post, Integer> {

    public Page<Post> findAll(Pageable pageable);
    Post findPostById(int id);
    public List<Post> findAllByCreatorId(int creatorId);

    @Query("SELECT p FROM Post p WHERE p.id IN (" +
            "SELECT l.postId FROM Like l WHERE l.id IN (" +
            "SELECT l2.id FROM Like l2 WHERE l2.postId = l.postId AND " +
            "EXISTS (SELECT 1 FROM Post post WHERE post.id = l2.postId AND post.creationTime >= :sevenDaysAgo)) " +
            "GROUP BY l.postId ORDER BY COUNT(l.id) DESC)")
    List<Post> getPopularPostsIn7Days(@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo, Pageable pageable);
    @Query("SELECT p FROM Post p WHERE p.id IN (" +
            "SELECT l.postId FROM Like l GROUP BY l.postId ORDER BY COUNT(l.id) DESC)")
    List<Post> getPopularPostsEver(Pageable pageable);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.creationTime >= :oneMonthAgo")
    int getPostsInLastMonth(@Param("oneMonthAgo") LocalDateTime oneMonthAgo);

    @Query("SELECT COUNT(l.id) FROM Like l WHERE l.postId = :postId")
    int countLikesByPostId(int postId);


}
