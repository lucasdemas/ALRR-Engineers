package LocationFinder;

import static org.junit.jupiter.api.Assertions.*;

import LocationFinder.exceptions.InvaildInputException;
import LocationFinder.models.Location;
import LocationFinder.repositories.LocationRepository;
import LocationFinder.services.LocationService;
import LocationFinder.controllers.LocationController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import LocationFinder.exceptions.NotFoundException;
import java.util.Optional;

import java.util.List;

import java.util.List;

@RunWith (SpringRunner.class)
@SpringBootTest
class LocationTest {
    @Autowired
    private LocationService locServ;

    @MockBean
    private LocationRepository locRepo;

    @Test
    public void testDeleteLocation() {
        assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                //Have the mock repo say that there is no client with the id 100
                Mockito.when(locRepo.existsById(100)).thenReturn(false);

                //Try to delete the client with id 100 and get the thrown error
                locServ.deleteLocationById(100);
            }
        });
    }

    //Test to successfully add a client to the database
    @Test
    public void testAddLocation() {
        //Create a mock location that will represent the data that would be passed in for when a new location is added
        Location loc1 = new Location();
        loc1.setName("Location Name Test");
        loc1.setArea("Location area Test");
        loc1.setCost(20.0);

        //Create a second mock location to represent that the mock client will be given the location id 100 when added to the repo
        Location loc2 = new Location();
        loc1.setId(100);
        loc1.setName("Location Name Test");
        loc1.setArea("Location area Test");
        loc1.setCost(20.0);

        //Have the mock return the second mock client when it will save the first one to the client repo
        Mockito.when(locRepo.save(loc1)).thenReturn(loc2);

        //Check to see that the fist mock client's id was updated to 100 after being added to the client repo
        assertEquals(locServ.addLocation(loc1).getId(), 100);
    }



    // Testing to see if we can add a new location
//    @Test
//    public void addNewLocTest() {
//        Location loc = new Location("Mudd Building", "NYC", 30.50);
//        // Mockito.when(locRepo.save(loc)).thenReturn(loc);
//        // System.out.println(loc.getName());
//        String result = locController.addNewLoc("Mudd Building", "NYC", 30.50);
//        assertEquals("Saved", result);
//    }
}
