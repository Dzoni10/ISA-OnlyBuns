package rs.ac.uns.ftn.informatika.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.rest.domain.Location;
import rs.ac.uns.ftn.informatika.rest.domain.Post;
import rs.ac.uns.ftn.informatika.rest.dto.PostDTO;
import rs.ac.uns.ftn.informatika.rest.jwt.JwtUtil;
import rs.ac.uns.ftn.informatika.rest.mapper.PostDTOMapper;
import rs.ac.uns.ftn.informatika.rest.paged_result.PagedResults;
import rs.ac.uns.ftn.informatika.rest.service.PostService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PostDTOMapper postDTOMapper;

    @Operation(description = "Get all posts",method="GET")
    @GetMapping(value = "/all", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResults<PostDTO>> getAllPosts() {
        List<Post> posts = postService.findAll();

        List<PostDTO> postsDTO = new ArrayList<>();
        for (Post post : posts) {
            postsDTO.add(new PostDTO(post));
        }
        PagedResults<PostDTO> pagedResults = new PagedResults<>();
        pagedResults.setResults(postsDTO);
        pagedResults.setTotalCount(posts.size());
        return new ResponseEntity<>(pagedResults, HttpStatus.OK);
    }

    @Operation(description = "Create new post", method = "POST")
    @PostMapping(value="/create" ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> savePost(@RequestBody PostDTO postDTO) {
        Post savedPost = new Post();

        savedPost.setCreatorId(postDTO.creatorId);
        savedPost.setCreationTime(LocalDateTime.now());
        savedPost.setDescription(postDTO.description);
        if (postDTO.location != null) {
            Location location = new Location(postDTO.location.latitude, postDTO.location.longitude);
            savedPost.setLocation(location);
        }
        //savedPost.setPostname(postDTO.postname);
        //savedPost.setEmail(postDTO.email);
        //savedPost.setAdress(postDTO.adress);
        //savedPost.setLoginStatus(false);
        savedPost = postService.save(savedPost);

        return new ResponseEntity<>(new PostDTO(savedPost),HttpStatus.CREATED);
    }
}
