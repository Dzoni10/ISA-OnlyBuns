package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.ac.uns.ftn.informatika.rest.Interface.RepositoryInterfaces.PostRepositoryInterface;
import rs.ac.uns.ftn.informatika.rest.domain.Post;

import java.util.List;

@Service
public class PostService {

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
}
