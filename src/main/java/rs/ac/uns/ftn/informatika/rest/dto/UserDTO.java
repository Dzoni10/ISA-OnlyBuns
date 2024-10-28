package rs.ac.uns.ftn.informatika.rest.dto;


public class UserDTO {
    public Long id;
    public String name;
    public String surname;
    public String username;
    public String password;

    public UserDTO() {}

    public UserDTO(Long id, String name, String surname, String username, String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;

    }
}
