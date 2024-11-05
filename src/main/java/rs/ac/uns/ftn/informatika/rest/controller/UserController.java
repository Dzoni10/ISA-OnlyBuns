package rs.ac.uns.ftn.informatika.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.rest.authentification.AuthenticationResponse;
import rs.ac.uns.ftn.informatika.rest.dto.LogInRequest;
import rs.ac.uns.ftn.informatika.rest.jwt.JwtUtil;
import rs.ac.uns.ftn.informatika.rest.mapper.UserDTOMapper;
import rs.ac.uns.ftn.informatika.rest.paged_result.PagedResults;
import rs.ac.uns.ftn.informatika.rest.paged_result.PagedResults;
import rs.ac.uns.ftn.informatika.rest.service.EmailService;
import rs.ac.uns.ftn.informatika.rest.service.UserService;
import rs.ac.uns.ftn.informatika.rest.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.informatika.rest.dto.UserDTO;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDTOMapper userDTOMapper;

    @Operation(description = "Get all users",method="GET")
    @GetMapping(value = "/all", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResults<UserDTO>> getAllUsers() {
        List<User> users = userService.findAll();

        List<UserDTO> usersDTO = new ArrayList<>();
        for (User user : users) {
            usersDTO.add(new UserDTO(user));
        }
        PagedResults<UserDTO> pagedResults = new PagedResults<>();
        pagedResults.setResults(usersDTO);
        pagedResults.setTotalCount(users.size());
        return new ResponseEntity<>(pagedResults, HttpStatus.OK);
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
    @PostMapping(value="/signup" ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveUser(@RequestBody UserDTO userDTO) {
        User savedUser = new User();

            savedUser.setName(userDTO.name);
            savedUser.setSurname(userDTO.surname);
            savedUser.setPassword(userDTO.password);
            savedUser.setUsername(userDTO.username);
            savedUser.setEmail(userDTO.email);
            savedUser.setAdress(userDTO.adress);
            savedUser.setLoginStatus(false);
            savedUser = userService.save(savedUser);

            String activationLink = "http://localhost:8080/api/users/verify?email=" +savedUser.getEmail();

            try {
                emailService.sendVerificationEmail(userDTO,activationLink);
            }catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }


            return new ResponseEntity<>(new UserDTO(savedUser),HttpStatus.CREATED);
    }


    @GetMapping(value = "/verify")
    public ResponseEntity<String> verifyUser(@RequestParam("email") String email){
        User user = userService.findByEmail(email);

        if(user!=null){
            user.setLoginStatus(true);
            userService.save(user);
            return new ResponseEntity<>("Success   Activation",HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>("UnsuccessefullAcitvation",HttpStatus.NOT_FOUND);
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
        user.setLoginStatus(userDTO.loginStatus);

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


    @PostMapping(value="/login")
    public ResponseEntity<?> login(@RequestBody LogInRequest logInRequest) {
        User user = userService.findByEmail(logInRequest.getEmail());

        if(user==null || !user.getEmail().equals(logInRequest.getEmail()))
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UserDTO userDTO = new UserDTO();

                if(user.getLoginStatus()) {
                    String token = jwtUtil.generateToken(user.getEmail());
                    AuthenticationResponse response = new AuthenticationResponse(token);
                    return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
                }
                else
                {
                    return new ResponseEntity<>(new UserDTO(user), HttpStatus.UNAUTHORIZED);
                }
    }
}

