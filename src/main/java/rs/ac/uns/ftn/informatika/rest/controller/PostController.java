package rs.ac.uns.ftn.informatika.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.informatika.rest.domain.Post;
import rs.ac.uns.ftn.informatika.rest.dto.PostDTO;
import rs.ac.uns.ftn.informatika.rest.jwt.JwtUtil;
import rs.ac.uns.ftn.informatika.rest.mapper.PostDTOMapper;
import rs.ac.uns.ftn.informatika.rest.paged_result.PagedResults;
import rs.ac.uns.ftn.informatika.rest.service.PostService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService PostService;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PostDTOMapper postDTOMapper;

    @Operation(description = "Get all posts",method="GET")
    @GetMapping(value = "/all", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResults<PostDTO>> getAllPosts() {
        List<Post> posts = PostService.findAll();

        List<PostDTO> postsDTO = new ArrayList<>();
        for (Post post : posts) {
            postsDTO.add(new PostDTO(post));
        }
        PagedResults<PostDTO> pagedResults = new PagedResults<>();
        pagedResults.setResults(postsDTO);
        pagedResults.setTotalCount(posts.size());
        return new ResponseEntity<>(pagedResults, HttpStatus.OK);
    }
}
