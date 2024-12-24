package rs.ac.uns.ftn.informatika.rest.domain;

import rs.ac.uns.ftn.informatika.rest.dto.UserDTO;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="surname",nullable = false)
    private String surname;

    @Column(name="username", nullable = false)
    private String username;

    @Column(name="password",nullable = false)
    private String password;

    @Column(name="email",nullable = false)
    private String email;

    @Column(name="adress",nullable = false,length = 50)
    private String adress;

    @Column(name="loginStatus",nullable = false)
    private boolean loginStatus;

    @Column(name="isAdmin",nullable = false)
    private boolean isAdmin;
    @Column(name="postCount",nullable = false)
    private int postCount;

    @Column(name="lastActivity",nullable = false)
    private LocalDateTime lastActivity;

    public User(){
        super();
    }

    public User(String name, String surname, String username, String password,String email, String adress, boolean loginStatus,boolean isAdmin, int postcount) {
        super();
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.adress = adress;
        this.loginStatus = loginStatus;
        this.isAdmin = isAdmin;
        this.postCount = postcount;
        this.lastActivity = LocalDateTime.now();
    }

    public User(String name, String surname, String username, String password,String email, String adress, boolean loginStatus,boolean isAdmin,int postCount, LocalDateTime lastActivity) {
        super();
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
    public User(int id,String name, String surname, String username, String password,String email, String adress, boolean loginStatus,boolean isAdmin,int postCount, LocalDateTime lastActivity) {
        super();
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

    public User(UserDTO user){
        this.id=user.id;
        this.name = user.name;
        this.surname = user.surname;
        this.username = user.username;
        this.password = user.password;
        this.adress = user.adress;
        this.email = user.email;
        this.loginStatus=user.loginStatus;
        this.isAdmin=user.isAdmin;
        this.postCount=user.postCount;
        this.lastActivity=LocalDateTime.now();
    }


    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getSurname() {return surname;}
    public void setSurname(String surname) {this.surname = surname;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getAdress() {return adress;}
    public void setAdress(String adress) {this.adress = adress;}
    public boolean getLoginStatus() {return loginStatus;}
    public void setLoginStatus(boolean loginStatus) {this.loginStatus = loginStatus;}
    public boolean getIsAdmin() {return isAdmin;}
    public void setIsAdmin(boolean isAdmin) {this.isAdmin = isAdmin;}
    public int getPostCount() {return postCount;}
    public void setPostCount(int postCount) {this.postCount = postCount;}
    public LocalDateTime getLastActivity() {return lastActivity;}
    public void setLastActivity(LocalDateTime lastActivity) {this.lastActivity = lastActivity;}

}
