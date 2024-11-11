package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.ac.uns.ftn.informatika.rest.Interface.RepositoryInterfaces.LikeRepositoryInterface;
import rs.ac.uns.ftn.informatika.rest.domain.Like;

import java.util.List;

@Service
public class LikeService {

    @Autowired
    private LikeRepositoryInterface likeRepositoryInterface;

    public List<Like> findAll() {
        return likeRepositoryInterface.findAll();
    }

    public Like findOne(Integer id) {
        return likeRepositoryInterface.findById(id).orElseGet(null);
    }

    public Page<Like> findAll(Pageable page) {
        return likeRepositoryInterface.findAll(page);
    }

    public List<Like> findByPostId(Integer postId) {
        return likeRepositoryInterface.findAllByPostId(postId);
    }

    public Like findLikeById(int id) {
        return likeRepositoryInterface.findLikeById(id);
    }

    public Like save(Like like) {
        boolean alreadyLiked = likeRepositoryInterface.existsByPostIdAndUsername(like.getPostId(), like.getUsername());

        if (alreadyLiked) {
            throw new IllegalStateException("You already liked this post");
        }

        // Save and return the new like if it doesn't already exist
        return likeRepositoryInterface.save(like);
    }

    public void remove(int id) {
        likeRepositoryInterface.deleteById(id);
    }

    public void removeByPostIdAndUsername(int postId, String username)
    {
        boolean alreadyLiked = likeRepositoryInterface.existsByPostIdAndUsername(postId, username);
        if (alreadyLiked) {
            Like likeToDelete = likeRepositoryInterface.findLikeByPostIdAndUsername(postId, username);
            likeRepositoryInterface.deleteById(likeToDelete.getId());
        }

    }
}
