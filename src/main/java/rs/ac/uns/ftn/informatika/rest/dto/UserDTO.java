package rs.ac.uns.ftn.informatika.rest.dto;
import rs.ac.uns.ftn.informatika.rest.domain.User;

public class UserDTO {
    public int id;
    public String name;
    public String surname;
    public String username;
    public String password;
    public String email;
    public String adress;

    public UserDTO() {}

    public UserDTO(User user)
    {
        this(user.getId(),user.getName(),user.getSurname(),user.getUsername(),user.getPassword(),user.getEmail(),user.getAdress());
    }

    public UserDTO(int id, String name, String surname, String username, String password,String email,String adress) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.adress = adress;
    }
}
