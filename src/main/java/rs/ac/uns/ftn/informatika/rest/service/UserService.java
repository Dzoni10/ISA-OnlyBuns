package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.rest.Interface.RepositoryInterfaces.UserRepositoryInterface;
import rs.ac.uns.ftn.informatika.rest.domain.User;
import rs.ac.uns.ftn.informatika.rest.dto.UserDTO;
import org.springframework.data.domain.Page;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import rs.ac.uns.ftn.informatika.rest.exception.UsernameAlreadyExistsException;

import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Propagation;

import javax.persistence.LockModeType;

@Service
public class UserService{


    private final UserRepositoryInterface userRepositoryInterface;
    private final BloomFilter<CharSequence> usernameBloomFilter;


    @Autowired
    public UserService(UserRepositoryInterface userRepositoryInterface) {
        this.userRepositoryInterface = userRepositoryInterface;

        List<User> allUsers = userRepositoryInterface.findAll();

        this.usernameBloomFilter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8),allUsers.size(),0.01);

        for (User user : allUsers) {
            usernameBloomFilter.put(user.getUsername());
            usernameBloomFilter.put(user.getEmail());
        }

    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Lock(LockModeType.PESSIMISTIC_WRITE)

    public boolean isUsernameExist(String username) {
        System.out.println("Acquiring lock for username: " + username);
            Optional<User> existingUser = userRepositoryInterface.findByUsernameWithLock(username);
        System.out.println("Lock acquired for username: " + username);
            return existingUser.isPresent();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateUserEmail(String username, String newEmail) {
        Optional<User> userOptional = userRepositoryInterface.findByUsernameWithLock(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEmail(newEmail);
            userRepositoryInterface.save(user);
        }
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateUserPostCount(String username, int number) {
        Optional<User> userOptional = userRepositoryInterface.findByUsernameWithLock(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPostCount(user.getPostCount()+ number);
            userRepositoryInterface.save(user);
        }
    }

    public boolean isEmailExist(String email){
        return usernameBloomFilter.mightContain(email);
    }

    public List<User> findUsersByCriteria(String name, String surname, String email, Integer minPosts, Integer maxPosts) {
        return userRepositoryInterface.findUsersByCriteria(name,surname,email,minPosts,maxPosts);
    }
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

    public User findByEmail(String email){
        return userRepositoryInterface.findUserByEmail(email);
    }

    public User findUserById(int id) {
        return userRepositoryInterface.findUserById(id);
    }

    @Transactional
    public synchronized User saveUsernameCheck(User user){

        if (userRepositoryInterface.findUserByUsername(user.getUsername())!=null) {
            throw new UsernameAlreadyExistsException("Korisničko ime je već zauzeto (service error)");
        }

        return userRepositoryInterface.save(user);
    }

    public User save(User user){
        return userRepositoryInterface.save(user);
    }

    public List<User> getUsersForPagination(int pageNumber) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize); // Spring koristi 0-indeksirane stranice
        return userRepositoryInterface.getUsersForPagination(pageable);
    }

    public void remove(int id) {
        userRepositoryInterface.deleteById(id);
    }

    public void deleteInactiveUsers() {
        List<User> inactiveUsers = userRepositoryInterface.findByLoginStatusFalse();
        for (User user : inactiveUsers) {
            userRepositoryInterface.delete(user);
        }
    }
}