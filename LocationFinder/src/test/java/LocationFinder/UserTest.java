package LocationFinder;

import static org.junit.jupiter.api.Assertions.*;
import LocationFinder.models.User;
import LocationFinder.controllers.UserController;
import LocationFinder.repositories.UserRepository;
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
    private UserController userCon;

    @MockBean
    private UserRepository userRepo;


    @Test
    public void addNewUser() {
        String returnedString = userCon.addNewUser("test4", "test4@test.com");
        User testUser = new User();
        testUser.setName("test4");
        testUser.setEmail("test4@test.com");
        User afterUser = new User();
        afterUser.setName("test4");
        afterUser.setEmail("test4@test.com");
        Mockito.when(userRepo.save(testUser)).thenReturn(afterUser);
        assertEquals(afterUser.getName(), "test4");
    }

    @Test
    void getAllUsers() {
        userCon.addNewUser("test4", "test4@test.com");
        System.out.println();

    }
}