package rs.ac.uns.ftn.informatika.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.rest.service.UserService;
import rs.ac.uns.ftn.informatika.rest.domain.User;

import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.informatika.rest.dto.UserDTO;

@RestController
@RequestMapping("/api/users")
public class UserController {

    //private final UserService userService;
    @Autowired
    private UserService userService;

    @Operation(description = "Get all users",method="GET")
    @GetMapping(value = "/all", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.findAll();

        List<UserDTO> usersDTO = new ArrayList<>();
        for (User user : users) {
            usersDTO.add(new UserDTO(user));
        }
        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsersPage(Pageable page){
        Page<User> users = userService.findAll(page);

        List<UserDTO> usersDTO = new ArrayList<>();

        for (User user : users) {
            usersDTO.add(new UserDTO(user));
        }
        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
    }
    @Operation(description = "Get special user by id", method = "GET")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUser(@PathVariable Integer id) {
        User user = userService.findUserById(id);

        if(user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new UserDTO(user),HttpStatus.OK);
    }

    @Operation(description = "Create new user", method = "POST")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO user) {
        User savedUser = new User();

            savedUser.setName(user.name);
            savedUser.setSurname(user.surname);
            savedUser.setPassword(user.password);
            savedUser.setUsername(user.username);
            savedUser.setEmail(user.email);
            savedUser.setAdress(user.adress);
            savedUser = userService.save(savedUser);
            return new ResponseEntity<>(new UserDTO(savedUser),HttpStatus.CREATED);
    }

    @Operation(description = "Updates an existing greeting",method = "PUT")
    @PutMapping( consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        User user= userService.findOne(userDTO.id);

        if(user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        user.setUsername(userDTO.username);
        user.setName(userDTO.name);
        user.setPassword(userDTO.password);
        user.setSurname(userDTO.surname);
        user.setAdress(userDTO.adress);
        user.setEmail(userDTO.email);

        user = userService.save(user);
        return new ResponseEntity<>(new UserDTO(user),HttpStatus.OK);
    }

    @Operation(description = "Delete special user", method = "DELETE")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        User deletedUser= userService.findOne(id);

        if(deletedUser == null) {
            userService.remove(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/findUsername")
    public ResponseEntity<List<UserDTO>> findUserByUsername(@RequestParam String username) {
        List<User> users = userService.findByUsername(username);

        List<UserDTO> usersDTO = new ArrayList<>();

        for(User user : users) {
            usersDTO.add(new UserDTO(user));
        }
        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
    }
}

