package rs.ac.uns.ftn.informatika.rest.controller;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.rest.domain.Comment;
import rs.ac.uns.ftn.informatika.rest.domain.Following;
import rs.ac.uns.ftn.informatika.rest.dto.CommentDTO;
import rs.ac.uns.ftn.informatika.rest.dto.FollowingDTO;
import rs.ac.uns.ftn.informatika.rest.paged_result.PagedResults;
import rs.ac.uns.ftn.informatika.rest.service.FollowingService;

import java.time.Duration;
import java.util.ArrayList; 
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/following")
public class FollowingController {

    @Autowired
    private FollowingService followingService;

    private final ConcurrentHashMap<String, Bucket> followingBuckets = new ConcurrentHashMap<>();

    @Operation(description = "Get all followings", method = "GET")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResults<FollowingDTO>> getAllComments() {
        List<Following> followings = followingService.findAll();

        List<FollowingDTO> followingDTO = new ArrayList<>();
        for (Following following : followings) {
            followingDTO.add(new FollowingDTO(following));
        }

        PagedResults<FollowingDTO> pagedResults = new PagedResults<>();
        pagedResults.setResults(followingDTO);
        pagedResults.setTotalCount(followings.size());
        return new ResponseEntity<>(pagedResults, HttpStatus.OK);
    }

    @Operation(description = "Get all users followed by a specific username", method = "GET")
    @GetMapping(value = "/followed-by/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Following>> getAllFollowedByUsername(@PathVariable String username) {
        List<Following> followedUsers = followingService.getAllFollowedByUsername(username);
        return new ResponseEntity<>(followedUsers, HttpStatus.OK);
    }

    @Operation(description = "Get all followers of a specific username", method = "GET")
    @GetMapping(value = "/followers/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Following>> getAllFollowersByUsername(@PathVariable String username) {
        List<Following> followers = followingService.getAllFollowersByUsername(username);
        return new ResponseEntity<>(followers, HttpStatus.OK);
    }
    @Operation(description = "Create a new following record", method = "POST")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createFollowing(@RequestBody Following following) {
        String followerUsername = following.getUsername();
        Bucket bucket = followingBuckets.computeIfAbsent(followerUsername, this::newFollowingBucket);
        if (bucket.tryConsume(1)) {
            Following newFollowing = followingService.addFollowing(following);
            return new ResponseEntity<>(newFollowing, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Too many follow requests, please try again later.", HttpStatus.TOO_MANY_REQUESTS);
        }
    }

    @Operation(description = "Check if a user follows another user", method = "GET")
    @GetMapping(value = "/exists/{username}/{following}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> doesUserFollow(@PathVariable String username, @PathVariable String following) {
        boolean exists = followingService.doesUserFollow(username, following);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    @Operation(description = "Delete a following record by ID", method = "DELETE")
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deleteFollowing(@PathVariable int id) {
        followingService.removeFollowing(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



    private Bucket newFollowingBucket(String username) {
        return Bucket4j.builder()
                .addLimit(Bandwidth.classic(50, Refill.greedy(50, Duration.ofMinutes(1))))
                .build();
    }
}
