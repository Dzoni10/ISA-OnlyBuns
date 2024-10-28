package rs.ac.uns.ftn.informatika.rest.Interface.ServiceInterfaces;


import java.util.Collection;

import rs.ac.uns.ftn.informatika.rest.domain.User;
import rs.ac.uns.ftn.informatika.rest.dto.UserDTO;

public interface UserServiceInterface {

    Collection<User> findAll();
    User findUserById(Long id);
    User create(UserDTO userDTO) throws Exception;
    User update(UserDTO userDTO,Long id) throws Exception;
    User delete(Long id);
}

