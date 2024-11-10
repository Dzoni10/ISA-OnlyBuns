package rs.ac.uns.ftn.informatika.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.rest.domain.Comment;
import rs.ac.uns.ftn.informatika.rest.dto.CommentDTO;
import rs.ac.uns.ftn.informatika.rest.dto.PostDTO;
import rs.ac.uns.ftn.informatika.rest.mapper.CommentDTOMapper;
import rs.ac.uns.ftn.informatika.rest.paged_result.PagedResults;
import rs.ac.uns.ftn.informatika.rest.service.CommentService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentDTOMapper commentDTOMapper;

    @Operation(description = "Get all comments", method = "GET")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResults<CommentDTO>> getAllComments() {
        List<Comment> comments = commentService.findAll();

        List<CommentDTO> commentsDTO = new ArrayList<>();
        for (Comment comment : comments) {
            commentsDTO.add(new CommentDTO(comment));
        }

        PagedResults<CommentDTO> pagedResults = new PagedResults<>();
        pagedResults.setResults(commentsDTO);
        pagedResults.setTotalCount(comments.size());
        return new ResponseEntity<>(pagedResults, HttpStatus.OK);
    }

    @Operation(description = "Get comment by ID", method = "GET")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Integer id) {
        Comment comment = commentService.findOne(id);
        if (comment != null) {
            return new ResponseEntity<>(new CommentDTO(comment), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(description = "Get comments by Post ID", method = "GET")
    @GetMapping(value = "/post/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResults<CommentDTO>> getCommentsByPostId(@PathVariable Integer postId) {
        List<Comment> comments = commentService.findByPostId(postId);

        List<CommentDTO> commentsDTO = new ArrayList<>();
        for (Comment comment : comments) {
            commentsDTO.add(new CommentDTO(comment));
        }
        PagedResults<CommentDTO> pagedResults = new PagedResults<>();
        pagedResults.setResults(commentsDTO);
        pagedResults.setTotalCount(comments.size());
        return new ResponseEntity<>(pagedResults, HttpStatus.OK);
    }

    @Operation(description = "Create new comment", method = "POST")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) {
        Comment newComment = new Comment();
        newComment.setPostId(commentDTO.postId);
        newComment.setUsername(commentDTO.username);
        newComment.setText(commentDTO.text);
        newComment.setCreationTime(LocalDateTime.now());

        newComment = commentService.save(newComment);

        return new ResponseEntity<>(new CommentDTO(newComment), HttpStatus.CREATED);
    }

    @Operation(description = "Delete comment by ID", method = "DELETE")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer id) {
        commentService.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
