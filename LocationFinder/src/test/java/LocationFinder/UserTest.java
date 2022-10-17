package LocationFinder;

import static org.junit.jupiter.api.Assertions.*;
import LocationFinder.models.User;
import LocationFinder.controllers.UserController;
import LocationFinder.repositories.UserRepository;
import LocationFinder.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserTest {

    @Autowired
    private UserService userServ;

    @MockBean
    private UserRepository userRepo;


    @Test
    public void addNewUser() {
        User bUser = new User(0,
                        "testUser",
                        "testUser@test.com");
        // save the user
        Mockito.when(userRepo.save(bUser)).thenReturn(bUser);

        //assert that the user_id gets correctly updated
        assertEquals(userServ.postUser(bUser).get(0).getId(), 0);
    }

    @Test
    void getAllUsers() {

    }
}