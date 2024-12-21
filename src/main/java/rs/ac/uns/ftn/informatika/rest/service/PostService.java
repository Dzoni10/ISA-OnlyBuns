package rs.ac.uns.ftn.informatika.rest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.ac.uns.ftn.informatika.rest.Interface.RepositoryInterfaces.PostRepositoryInterface;
import rs.ac.uns.ftn.informatika.rest.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final Logger LOG = LoggerFactory.getLogger(PostService.class);
    @Autowired
    private PostRepositoryInterface postRepositoryInterface;

    public List<Post> findAll() {
        return postRepositoryInterface.findAll();
    }
    public Post findOne(Integer id) {
        return postRepositoryInterface.findById(id).orElseGet(null);
    }

    public Page<Post> findAll(Pageable page) {
        return postRepositoryInterface.findAll(page);
    }

    public List<Post> findByCreatorId(Integer creatorId)
    {
        return postRepositoryInterface.findAllByCreatorId(creatorId);
    }

    public Post findPostById(int id) {
        return postRepositoryInterface.findPostById(id);
    }

    public Post save(Post post){
        return postRepositoryInterface.save(post);
    }


    public void remove(int id) {
        postRepositoryInterface.deleteById(id);
    }

    public Post updatePost(int id, Post postDetails) {
        return postRepositoryInterface.findById(id).map(post -> {
            post.setDescription(postDetails.getDescription());
            post.setUsername(postDetails.getUsername());
            post.setCreationTime(LocalDateTime.now());
            return postRepositoryInterface.save(post);
        }).orElse(null);
    }

    @Cacheable("popularPostsLast7DaysCache")
    public List<Post> getPopularPostsIn7days() {
        System.out.println("7 dana");
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        Pageable pageable = PageRequest.of(0, 5);
        return postRepositoryInterface.getPopularPostsIn7Days(sevenDaysAgo,pageable);
    }

    @Cacheable("popularPostsAllTimeCache")
    public List<Post> getPopularPostsEver() {
        System.out.println("ikada");
        Pageable pageable = PageRequest.of(0, 10);
        return postRepositoryInterface.getPopularPostsEver(pageable);
    }

    public int getLikeCountForPost(int postId) {
        return postRepositoryInterface.countLikesByPostId(postId);
    }

    @CacheEvict(value = {"popularPostsAllTimeCache", "popularPostsLast7DaysCache"}, allEntries = true)
    public void evictCaches(){
        LOG.info("Posts removed from cache!");
    }

    public int getPostsInLastMonth(){
        LocalDateTime monthAgo = LocalDateTime.now().minusMonths(1);
        return postRepositoryInterface.getPostsInLastMonth(monthAgo);
    }

}
