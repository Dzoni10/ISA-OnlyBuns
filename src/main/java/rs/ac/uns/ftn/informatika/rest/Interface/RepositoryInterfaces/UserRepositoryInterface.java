package rs.ac.uns.ftn.informatika.rest.Interface.RepositoryInterfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.informatika.rest.domain.User;

import java.util.Collection;
import rs.ac.uns.ftn.informatika.rest.domain.User;
import java.util.Collection;
import java.util.List;

public interface UserRepositoryInterface extends JpaRepository<User, Integer> {

        public Page<User> findAll(Pageable pageable);
        User findUserById(int id);
        public List<User> findAllByUsername(String username);
        public User findUserByEmail(String email);

}
