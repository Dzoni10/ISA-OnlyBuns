package rs.ac.uns.ftn.informatika.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.rest.domain.Like;
import rs.ac.uns.ftn.informatika.rest.dto.LikeDTO;
import rs.ac.uns.ftn.informatika.rest.dto.PostDTO;
import rs.ac.uns.ftn.informatika.rest.mapper.LikeDTOMapper;
import rs.ac.uns.ftn.informatika.rest.paged_result.PagedResults;
import rs.ac.uns.ftn.informatika.rest.service.LikeService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private LikeDTOMapper likeDTOMapper;

    @Operation(description = "Get all likes", method = "GET")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResults<LikeDTO>> getAllLikes() {
        List<Like> likes = likeService.findAll();

        List<LikeDTO> likesDTO = new ArrayList<>();
        for (Like like : likes) {
            likesDTO.add(new LikeDTO(like));
        }

        PagedResults<LikeDTO> pagedResults = new PagedResults<>();
        pagedResults.setResults(likesDTO);
        pagedResults.setTotalCount(likes.size());
        return new ResponseEntity<>(pagedResults, HttpStatus.OK);
    }

    @Operation(description = "Get like by ID", method = "GET")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LikeDTO> getLikeById(@PathVariable Integer id) {
        Like like = likeService.findOne(id);
        if (like != null) {
            return new ResponseEntity<>(new LikeDTO(like), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(description = "Get likes by Post ID", method = "GET")
    @GetMapping(value = "/post/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResults<LikeDTO>> getLikesByPostId(@PathVariable Integer postId) {
        List<Like> likes = likeService.findByPostId(postId);

        List<LikeDTO> likesDTO = new ArrayList<>();
        for (Like like : likes) {
            likesDTO.add(new LikeDTO(like));
        }
        PagedResults<LikeDTO> pagedResults = new PagedResults<>();
        pagedResults.setResults(likesDTO);
        pagedResults.setTotalCount(likes.size());
        return new ResponseEntity<>(pagedResults, HttpStatus.OK);
    }

    @Operation(description = "Create new like", method = "POST")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createLike(@RequestBody LikeDTO likeDTO) {
        try {
            // Map LikeDTO to Like entity
            Like newLike = new Like();
            newLike.setPostId(likeDTO.postId);
            newLike.setUsername(likeDTO.username);

            // Save the like using the service
            newLike = likeService.save(newLike);

            // Return a response with the saved Like
            return new ResponseEntity<>(new LikeDTO(newLike), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            // If user has already liked the post, return a conflict status with an error message
            return new ResponseEntity<>("You already liked this post", HttpStatus.CONFLICT);
        }
    }

    //@Operation(description = "Delete like by postID and username", method = "DELETE")
    //@DeleteMapping("/delete")
    //public ResponseEntity<Void> deleteLike(@RequestBody LikeDTO likeDTO) {
        //Like newLike = new Like();
        //newLike.setPostId(likeDTO.postId);
        //newLike.setUsername(likeDTO.username);
        //likeService.removeByPostIdAndUsername(newLike);
        //return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    //}

    @Operation(description = "Delete like by postID and username", method = "DELETE")
    @DeleteMapping("/delete/{postId}/{username}")
    public ResponseEntity<Void> deleteLike(
            @PathVariable int postId,
            @PathVariable String username) {
        likeService.removeByPostIdAndUsername(postId, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}