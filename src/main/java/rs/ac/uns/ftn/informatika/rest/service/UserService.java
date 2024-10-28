package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.rest.Interface.ServiceInterfaces.UserServiceInterface;
import rs.ac.uns.ftn.informatika.rest.repository.UserRepository;
import rs.ac.uns.ftn.informatika.rest.domain.User;
import rs.ac.uns.ftn.informatika.rest.dto.UserDTO;

import java.util.Collection;

@Service
public class UserService implements UserServiceInterface {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Collection<User> findAll() {
        Collection<User> users = userRepository.findAll();
        return users;
    }

    @Override
    public User findUserById(Long id) {
        User user = userRepository.findUserById(id);
        return user;
    }

    @Override
    public User create(UserDTO userDTO) throws Exception {
        if(userDTO.id !=null){
            throw new Exception("Id mora biti ne koriscen prilikom dodavanja novog entiteta");
        }
        User savedUser = userRepository.create(new User(userDTO));
        return savedUser;
    }

    @Override
    public User update(UserDTO user,Long id) throws Exception {
        User updateUser = findUserById(id);
        if(updateUser == null){
            throw new Exception("Korisnik za izmenu nije pronadjen");
        }
        updateUser.setName(user.name);
        updateUser.setSurname(user.surname);
        updateUser.setUsername(user.username);
        updateUser.setPassword(user.password);
        return updateUser;
    }

    @Override
    public User delete(Long id) {
        return userRepository.delete(id);
    }
}