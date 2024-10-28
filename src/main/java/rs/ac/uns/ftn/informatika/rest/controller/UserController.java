package rs.ac.uns.ftn.informatika.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import rs.ac.uns.ftn.informatika.rest.Interface.ServiceInterfaces.UserServiceInterface;
import rs.ac.uns.ftn.informatika.rest.domain.User;
import java.util.Collection;
import rs.ac.uns.ftn.informatika.rest.dto.UserDTO;

@Tag(name="User controller", description="Controler for User entity")
@RestController
@RequestMapping("/api/users")
public class UserController {

    //private final UserService userService;
    @Autowired
    private UserServiceInterface userServiceInterface;



    @Operation(description = "Get all users",method="GET")
    @GetMapping(produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<User>> getUsers() {
        Collection<User> users = userServiceInterface.findAll();
        return new ResponseEntity<Collection<User>>(users, HttpStatus.OK);
    }

    @Operation(description = "Get special user by id", method = "GET")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        User user = userServiceInterface.findUserById(id);

        if(user == null) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user,HttpStatus.OK);
    }

    @Operation(description = "Create new user", method = "POST")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody UserDTO user) {
        User savedUser = new User();
        try{
            savedUser = userServiceInterface.create(user);
            return new ResponseEntity<User>(savedUser,HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<User>(savedUser,HttpStatus.CONFLICT);
        }
    }

    @Operation(description = "Updates an existing greeting",method = "PUT")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@RequestBody UserDTO user,@PathVariable Long id) {
        try{
            User updatedUser = userServiceInterface.update(user,id);
            return new ResponseEntity<>(updatedUser,HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(description = "Delete special user", method = "DELETE")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") Long id) {
        User deletedUser = userServiceInterface.delete(id);
        if(deletedUser == null) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }
}
