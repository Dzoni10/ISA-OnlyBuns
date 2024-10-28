package rs.ac.uns.ftn.informatika.rest.domain;

import rs.ac.uns.ftn.informatika.rest.dto.UserDTO;


public class User {

    private Long id;
    private String name;
    private String surname;
    private String username;
    private String password;


    public User(){}

    public User(String name, String surname, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
    }

    public User(UserDTO user){
        this.id=user.id;
        this.name = user.name;
        this.surname = user.surname;
        this.username = user.username;
        this.password = user.password;
    }


    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getSurname() {return surname;}
    public void setSurname(String surname) {this.surname = surname;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

}
