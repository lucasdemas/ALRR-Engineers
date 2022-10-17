package LocationFinder;

import static org.junit.jupiter.api.Assertions.*;
import LocationFinder.models.User;
import LocationFinder.controllers.UserController;
import LocationFinder.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserTest {

    @Autowired
    private UserController userCon;

    @MockBean
    private UserRepository userRepo;


    @Test
    void addNewUser() {
        String returnedString = userCon.addNewUser("test4", "test4@test.com");
        assertEquals("Saved", returnedString);
    }

    @Test
    void getAllUsers() {

    }
}