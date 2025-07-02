package rs.ac.uns.ftn.informatika.rest.controller;

import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.rest.domain.Location;

import rs.ac.uns.ftn.informatika.rest.domain.Post;
import rs.ac.uns.ftn.informatika.rest.dto.PostDTO;
import rs.ac.uns.ftn.informatika.rest.jwt.JwtUtil;
import rs.ac.uns.ftn.informatika.rest.mapper.PostDTOMapper;
import rs.ac.uns.ftn.informatika.rest.paged_result.PagedResults;
import rs.ac.uns.ftn.informatika.rest.service.PostService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.informatika.rest.service.UserService;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PostDTOMapper postDTOMapper;
    private final MeterRegistry meterRegistry;

    @Value("${upload.directory}")
    private String uploadDir;

    public PostController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Operation(description = "Get all posts",method="GET")
    @GetMapping(value = "/all", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResults<PostDTO>> getAllPosts() {
        List<Post> posts = postService.findAllPostsWithoutImages();

        List<PostDTO> postsDTO = new ArrayList<>();
        for (Post post : posts) {
            postsDTO.add(new PostDTO(post));
        }
        PagedResults<PostDTO> pagedResults = new PagedResults<>();
        pagedResults.setResults(postsDTO);
        pagedResults.setTotalCount(posts.size());
        return new ResponseEntity<>(pagedResults, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Delete a post by ID", method = "DELETE")
    public ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        Post post = postService.findPostById(id);
        userService.updateUserPostCount(post.getUsername(),-1);
        postService.remove(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable int id, @RequestBody Post post) {
        Post updatedPost = postService.updatePost(id, post);
        if (updatedPost != null) {
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(description = "Create new post", method = "POST")
    @PostMapping(value = "/create", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<?> savePost(@RequestParam("creatorId") int creatorId,
                                      @RequestParam("description") String description,
                                      @RequestParam("username") String username,
                                      @RequestParam(value = "location.latitude", required = false) Double latitude,
                                      @RequestParam(value = "location.longitude", required = false) Double longitude,
                                      @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {

        long start = System.nanoTime();

        // Validate inputs
        if (creatorId <= 0 || description == null || description.isEmpty()) {
            return new ResponseEntity<>("Creator ID and description are required", HttpStatus.BAD_REQUEST);
        }

        // Initialize Post object
        Post savedPost = new Post();
        savedPost.setCreatorId(creatorId);
        savedPost.setUsername(username);
        savedPost.setDescription(description);
        savedPost.setCreationTime(LocalDateTime.now());

        // Set location if present
        if (latitude != null && longitude != null) {
            Location location = new Location(latitude, longitude);
            savedPost.setLocation(location);
        }

        // Handle image upload
        if (imageFile != null && !imageFile.isEmpty()) {
            //String uploadDir = System.getProperty("user.dir") + "/uploads/images/";

            // Create a unique file name
            String imageName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(imageFile.getOriginalFilename());
            Path imagePath = Paths.get(uploadDir, imageName);

            // Ensure the upload directory exists
            Files.createDirectories(imagePath.getParent());

            // Save the file to the defined path
            Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

            // Save the image path for the database
            String imagePathForDatabase = "uploads/images/" + imageName;
            savedPost.setImagePath(imagePathForDatabase);
        }

        // Save the post to the database
        savedPost = postService.save(savedPost);
        userService.updateUserPostCount(savedPost.getUsername(),1);

        long duration = System.nanoTime() - start;
        meterRegistry.timer("http.requests.create_post").record(duration, TimeUnit.NANOSECONDS);
        // Return the saved post as a DTO
        return new ResponseEntity<>(new PostDTO(savedPost), HttpStatus.CREATED);
    }



    @GetMapping("/popular/all-time")
    public ResponseEntity<PagedResults<PostDTO>> getPopularPostsAllTime() {
        List<Post> popularPosts = postService.getPopularPostsEver();
        List<PostDTO> postDTOs = popularPosts.stream()
                .map(PostDTO::new)
                .collect(Collectors.toList());
        PagedResults<PostDTO> pagedResults = new PagedResults<>();
        pagedResults.setResults(postDTOs);
        pagedResults.setTotalCount(popularPosts.size());
        return new ResponseEntity<>(pagedResults,HttpStatus.OK);
    }

    @GetMapping("/popular/last-7-days")
    public ResponseEntity<PagedResults<PostDTO>> getPopularPostsLast7Days() {
        List<Post> popularPosts = postService.getPopularPostsIn7days();
        List<PostDTO> postDTOs = popularPosts.stream()
                .map(PostDTO::new)
                .collect(Collectors.toList());
        PagedResults<PostDTO> pagedResults = new PagedResults<>();
        pagedResults.setResults(postDTOs);
        pagedResults.setTotalCount(popularPosts.size());
        return new ResponseEntity<>(pagedResults,HttpStatus.OK);
    }

    @GetMapping("/{postId}/likes")
    public ResponseEntity<Integer> getLikeCount(@PathVariable int postId) {
        int likeCount = postService.getLikeCountForPost(postId);
        return ResponseEntity.ok(likeCount);
    }

    @GetMapping(value = "/evictCache")
    public ResponseEntity<String> removeFromCache() {
        postService.evictCaches();
        return ResponseEntity.ok("Cache cleared successfully");
    }

    @GetMapping("/popular/count-last-month")
    public ResponseEntity<Integer> getPopularPostsLastMonth() {
        int postCount = postService.getPostsInLastMonth();
        return ResponseEntity.ok(postCount);
    }


}
