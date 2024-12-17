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

        @Query("SELECT u FROM User u WHERE u.username = :username")
        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "5000")})
//        @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="0")})
        Optional<User> findByUsernameWithLock(@Param("username") String username);

}
