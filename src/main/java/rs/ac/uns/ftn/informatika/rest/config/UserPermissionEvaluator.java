package rs.ac.uns.ftn.informatika.rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.rest.Interface.RepositoryInterfaces.UserRepositoryInterface;
import rs.ac.uns.ftn.informatika.rest.domain.User;

import java.util.Optional;

@Component("userPermissionEvaluator")
public class UserPermissionEvaluator {

    @Autowired
    private UserRepositoryInterface userRepositoryInterface;

    public boolean canInteract(String username){
        Optional<User> userOpt = Optional.ofNullable(userRepositoryInterface.findUserByUsername(username));

        if(!userOpt.isPresent()){
            return false;
        }
        return userOpt.get().getLoginStatus();
    }
}
