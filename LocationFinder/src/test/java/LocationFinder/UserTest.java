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
import org.mockito.Mockito;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserTest {

    @Autowired
    private UserService userServ;

    @MockBean
    private UserRepository userRepo;

<<<<<<< HEAD


=======
    //Test to see if a new user can be added successfully
    @Test
>>>>>>> cfc71c8a245c38507543615fc2035b09addd136c
    public void addNewUser() {
        User bUser = new User(0,
                        "testUser",
                        "testUser@test.com");
        // save the user
        Mockito.when(userRepo.save(bUser)).thenReturn(bUser);

        //assert that the user_id gets correctly updated
        assertEquals(userServ.postUser(bUser).get(0).getId(), 0);
    }

    //Test to see if we can get a specific user from the repository
    @Test
    void getUser() {
        User bUser = new User(0,
                "testUser",
                "testUser@test.com");

        Mockito.when(userRepo.findByTemplate(null,
                null,
                "testUser@test.com")).thenReturn(List.of(bUser));

        User result = userServ.getUsersByTemplate(null,
                null,
                "testUser@test.com").get(0);

        System.out.println(bUser.getId() + bUser.getName());
        System.out.println(result.getId() + result.getName());
        assertEquals(bUser.getId(), result.getId());


    }
}
