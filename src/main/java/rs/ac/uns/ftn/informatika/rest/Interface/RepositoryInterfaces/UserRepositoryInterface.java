package rs.ac.uns.ftn.informatika.rest.Interface.RepositoryInterfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import rs.ac.uns.ftn.informatika.rest.domain.User;

import java.util.Collection;
import rs.ac.uns.ftn.informatika.rest.domain.User;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Lock;

public interface UserRepositoryInterface extends JpaRepository<User, Integer> {

        public Page<User> findAll(Pageable pageable);
        User findUserById(int id);
        public List<User> findAllByUsername(String username);
        public User findUserByEmail(String email);
        public User findUserByUsername(String username);
        @Query("SELECT u.username FROM User u")
        List<String> findAllUsernames();

        @Query("SELECT u FROM User u WHERE u.username = :username")
        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "5000")})
//        @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="0")})
        Optional<User> findByUsernameWithLock(@Param("username") String username);

        @Query("SELECT u FROM User u WHERE u.loginStatus = false")
        List<User> findByLoginStatusFalse();

        @Query("SELECT u FROM User u WHERE u.loginStatus = true")
        List<User> getUsersForPagination(Pageable pageable);

        @Query("SELECT u FROM User u WHERE " +
                "(LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')) OR :name IS NULL OR :name = '') AND " +
                "(LOWER(u.surname) LIKE LOWER(CONCAT('%', :surname, '%')) OR :surname IS NULL OR :surname = '') AND " +
                "(LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')) OR :email IS NULL OR :email = '') AND " +
                "(u.postCount BETWEEN :minPosts AND :maxPosts OR :minPosts IS NULL OR :maxPosts IS NULL) AND " +
                "u.loginStatus = true")
        List<User> findUsersByCriteria(String name, String surname, String email, Integer minPosts, Integer maxPosts);


        @Query("SELECT u.username FROM User u WHERE u.loginStatus = true")
        List<String> findAllLoggedInUsernames();

}
