package rs.ac.uns.ftn.informatika.rest.dto;
import rs.ac.uns.ftn.informatika.rest.domain.User;

import java.time.LocalDateTime;

public class UserDTO {
    public int id;
    public int postCount;
    public String name;
    public String surname;
    public String username;
    public String password;
    public String email;
    public String adress;
    public boolean loginStatus;
    public boolean isAdmin;
    public LocalDateTime lastActivity;


    public UserDTO() {}

    public UserDTO(User user)
    {
        this(user.getId(),user.getName(),user.getSurname(),user.getUsername(),user.getPassword(),user.getEmail(),user.getAdress(),user.getLoginStatus(),user.getIsAdmin(),user.getPostCount(), user.getLastActivity());
    }

    public UserDTO(int id, String name, String surname, String username, String password,String email,String adress,boolean loginStatus, boolean isAdmin,int postCount, LocalDateTime lastActivity) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.adress = adress;
        this.loginStatus = loginStatus;
        this.isAdmin = isAdmin;
        this.postCount = postCount;
        this.lastActivity = lastActivity;
    }
}
