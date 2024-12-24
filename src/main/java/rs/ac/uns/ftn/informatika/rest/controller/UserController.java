package rs.ac.uns.ftn.informatika.rest.controller;

import ch.qos.logback.core.boolex.EvaluationException;
import io.github.bucket4j.Bucket;
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
import rs.ac.uns.ftn.informatika.rest.authentification.LoginRateLimiter;
import rs.ac.uns.ftn.informatika.rest.dto.LogInRequest;
import rs.ac.uns.ftn.informatika.rest.exception.UsernameAlreadyExistsException;
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

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private LoginRateLimiter rateLimiter;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDTOMapper userDTOMapper;
    @Autowired
    private LoginRateLimiter loginRateLimiter;

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
    @Operation(description = "Get all users for pagination",method="GET")
    @GetMapping(value = "/users", produces= MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<PagedResults<UserDTO>> getUsersForPagination(@RequestParam(defaultValue = "1") int pageNumber) {
        List<User> users = userService.getUsersForPagination(pageNumber);
        List<UserDTO> usersDTO = new ArrayList<>();
        for (User user : users) {
            usersDTO.add(new UserDTO(user));
        }
        PagedResults<UserDTO> pagedResults = new PagedResults<>();
        pagedResults.setResults(usersDTO);
        pagedResults.setTotalCount(users.size());
        return new ResponseEntity<>(pagedResults, HttpStatus.OK);    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsersPage(Pageable page){
        Page<User> users = userService.findAll(page);

        List<UserDTO> usersDTO = new ArrayList<>();

        for (User user : users) {
            usersDTO.add(new UserDTO(user));
        }
        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
    }
    @Operation(description = "Get users by search criteria", method = "GET")
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResults<UserDTO>> getUsersByCriteria(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer minPosts,
            @RequestParam(required = false) Integer maxPosts
    ) {
        List<User> users = userService.findUsersByCriteria(name, surname, email, minPosts, maxPosts);
        List<UserDTO> usersDTO = new ArrayList<>();
        for (User user : users) {
            usersDTO.add(new UserDTO(user));
        }
        PagedResults<UserDTO> pagedResults = new PagedResults<>();
        pagedResults.setResults(usersDTO);
        pagedResults.setTotalCount(users.size());
        return new ResponseEntity<>(pagedResults, HttpStatus.OK);
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


        if (userService.isUsernameExist(userDTO.username)) {
            if (userService.isEmailExist(userDTO.email)) {
                return new ResponseEntity<>("Email i korisnicko ime su vec zauzeti", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Korisnicko ime je vec zauzeto", HttpStatus.CONFLICT);
        }

        if (userService.isEmailExist(userDTO.email)) {
            return new ResponseEntity<>("Email je vec zauzet", HttpStatus.UNAUTHORIZED);
        }


        User savedUser = new User();

        savedUser.setName(userDTO.name);
        savedUser.setSurname(userDTO.surname);
        savedUser.setPassword(userDTO.password);
        savedUser.setUsername(userDTO.username);
        savedUser.setEmail(userDTO.email);
        savedUser.setAdress(userDTO.adress);
        savedUser.setLoginStatus(false);
        savedUser.setIsAdmin(false);
        try {
            savedUser = userService.saveUsernameCheck(savedUser);

            String activationLink = "http://localhost:8080/api/users/verify?email=" + savedUser.getEmail();


            emailService.sendVerificationEmail(userDTO, activationLink);
            return new ResponseEntity<>(new UserDTO(savedUser), HttpStatus.CREATED);

        } catch (UsernameAlreadyExistsException e) {
            return new ResponseEntity<>("Korisničko ime je već zauzeto (Kontroler)", HttpStatus.CONFLICT);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping(value="/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody UserDTO userDTO){
            User user = new User();

            user.setId(userDTO.id);
            user.setName(userDTO.name);
            user.setSurname(userDTO.surname);
            user.setPassword(userDTO.password);
            user.setUsername(userDTO.username);
            user.setEmail(userDTO.email);
            user.setAdress(userDTO.adress);
            user.setLoginStatus(userDTO.loginStatus);
            user.setIsAdmin(userDTO.isAdmin);

            userService.save(user);

            return new ResponseEntity<>("Password changed", HttpStatus.OK);

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
        user.setIsAdmin(userDTO.isAdmin);

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
    public ResponseEntity<?> login(HttpServletRequest request, @RequestBody LogInRequest logInRequest) {

        String clientIp = getClientIP(request);

        Bucket bucket = loginRateLimiter.resolveBucket(clientIp);
        if(!bucket.tryConsume(1)){
            return new ResponseEntity<>("Prekoracili ste broj pokusaja za prijavu, pokusajte ponovo za 5 min",HttpStatus.TOO_MANY_REQUESTS);
        }


        User user = userService.findByEmail(logInRequest.getEmail());

        if(user==null || ( !user.getEmail().equals(logInRequest.getEmail()) || !user.getPassword().equals(logInRequest.getPassword())))
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UserDTO userDTO = new UserDTO();

                if(user.getLoginStatus()) {
                    String token = jwtUtil.generateToken(user.getEmail(),user.getId());
                    AuthenticationResponse response = new AuthenticationResponse(token);
                    return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
                }
                else
                {
                    return new ResponseEntity<>(new UserDTO(user), HttpStatus.UNAUTHORIZED);
                }
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if(xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}