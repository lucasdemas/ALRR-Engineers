package LocationFinder.services;

import LocationFinder.models.User;
import LocationFinder.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public List<User> getByID(int userID) {
        Optional<User> result = userRepo.findById(userID);
        if (result.isPresent()) {
            User userResult = result.get();
            return List.of(userResult);
        }
        return Collections.emptyList();
    }

    // get operation
    public List<User> getUsersByTemplate(Integer user_id,
                                         String username,
                                         String email) {
        return userRepo.findByTemplate(user_id, username, email);
    }

    // post operation
    public List<User> postUser(User user) {
        User result = userRepo.save(user);
        return List.of(result);
    }
}
