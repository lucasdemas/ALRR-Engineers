
import LocationFinder.repositories.UserRepository;
import LocationFinder.models.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;



@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {

    @Autowired
    private UserService userService;


    // Test that the id is correctly updated by postUser method
    @Test
    public void testAddUser() {

        //Test adding a new user 
        assertEquals(addNewUser("atheer_test@gmail.com","atheer123"), "Saved");

    }

}

