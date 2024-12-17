package rs.ac.uns.ftn.informatika.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.rest.domain.Following;
import rs.ac.uns.ftn.informatika.rest.service.FollowingService;

import java.util.List;

@RestController
@RequestMapping("/api/following")
public class FollowingController {

    @Autowired
    private FollowingService followingService;


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
    public ResponseEntity<Following> createFollowing(@RequestBody Following following) {
        Following newFollowing = followingService.addFollowing(following);
        return new ResponseEntity<>(newFollowing, HttpStatus.CREATED);
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
}
