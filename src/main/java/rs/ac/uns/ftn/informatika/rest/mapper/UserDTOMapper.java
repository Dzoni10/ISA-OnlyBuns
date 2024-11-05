package rs.ac.uns.ftn.informatika.rest.mapper;
import org.springframework.beans.factory.annotation.Autowired;
import rs.ac.uns.ftn.informatika.rest.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.rest.domain.User;

@Component
public class UserDTOMapper {

    private static ModelMapper modelMapper;

    @Autowired
    public UserDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User fromDTOtoUser(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }

    public UserDTO fromUserToDTO(User user){
        return modelMapper.map(user,UserDTO.class);
    }

}
