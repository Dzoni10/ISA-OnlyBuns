package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.rest.Interface.RepositoryInterfaces.UserRepositoryInterface;
import rs.ac.uns.ftn.informatika.rest.domain.User;
import rs.ac.uns.ftn.informatika.rest.dto.UserDTO;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

@Service
public class UserService{

    @Autowired
    private UserRepositoryInterface userRepositoryInterface;


    public List<User> findAll() {
        return userRepositoryInterface.findAll();
    }
    public User findOne(Integer id) {
        return userRepositoryInterface.findById(id).orElseGet(null);
    }

    public Page<User> findAll(Pageable page) {
        return userRepositoryInterface.findAll(page);
    }

    public List<User> findByUsername(String username)
    {
        return userRepositoryInterface.findAllByUsername(username);
    }

    public User findUserById(int id) {
        return userRepositoryInterface.findUserById(id);
    }

    public User save(User user){
        return userRepositoryInterface.save(user);
    }


    public void remove(int id) {
        userRepositoryInterface.deleteById(id);
    }
}