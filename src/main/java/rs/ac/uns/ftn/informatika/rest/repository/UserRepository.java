package rs.ac.uns.ftn.informatika.rest.repository;

import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.informatika.rest.domain.User;
import rs.ac.uns.ftn.informatika.rest.Interface.RepositoryInterfaces.UserRepositoryInterface;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository implements UserRepositoryInterface {

    private static AtomicLong counter = new AtomicLong();

    private final ConcurrentMap<Long,User> users = new ConcurrentHashMap<Long,User>();

    @Override
    public Collection<User> findAll() {
        return this.users.values();
    }

    @Override
    public User create(User user) {
        Long id = user.getId();

        if (id == null) {
            id = counter.incrementAndGet();
            user.setId(id);
        }
        user.setId(id);
        this.users.put(id, user);
        return user;
    }

    @Override
    public User findUserById(Long id) {
        return this.users.get(id);
    }

    @Override
    public User update(User user, Long id) {

        if(id!=null) {
            this.users.put(id, user);
        }
        return user;
    }

    @Override
    public User delete(Long id) {
        User user = this.users.remove(id);
        return user;
    }
}
