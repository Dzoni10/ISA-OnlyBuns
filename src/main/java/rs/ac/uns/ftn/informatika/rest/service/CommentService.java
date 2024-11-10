package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.ac.uns.ftn.informatika.rest.Interface.RepositoryInterfaces.CommentRepositoryInterface;
import rs.ac.uns.ftn.informatika.rest.domain.Comment;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepositoryInterface commentRepositoryInterface;

    public List<Comment> findAll() {
        return commentRepositoryInterface.findAll();
    }

    public Comment findOne(Integer id) {
        return commentRepositoryInterface.findById(id).orElseGet(null);
    }

    public Page<Comment> findAll(Pageable page) {
        return commentRepositoryInterface.findAll(page);
    }

    public List<Comment> findByPostId(Integer postId) {
        return commentRepositoryInterface.findAllByPostId(postId);
    }

    public Comment findCommentById(int id) {
        return commentRepositoryInterface.findCommentById(id);
    }

    public Comment save(Comment comment) {
        return commentRepositoryInterface.save(comment);
    }

    public void remove(int id) {
        commentRepositoryInterface.deleteById(id);
    }
}
