package rs.ac.uns.ftn.informatika.rest.domain;

import rs.ac.uns.ftn.informatika.rest.dto.UserDTO;

import javax.persistence.*;

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

    public User(){
        super();
    }

    public User(String name, String surname, String username, String password,String email, String adress, boolean loginStatus) {
        super();
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.adress = adress;
        this.loginStatus = loginStatus;
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

}
