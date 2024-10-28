package rs.ac.uns.ftn.informatika.rest.Interface.RepositoryInterfaces;

import rs.ac.uns.ftn.informatika.rest.domain.User;

import java.util.Collection;
import rs.ac.uns.ftn.informatika.rest.domain.User;
import java.util.Collection;
public interface UserRepositoryInterface {

        Collection<User> findAll();
        User create(User user);
        User findUserById(Long id);
        User update(User user,Long id);
        User delete(Long id);

}
