package LocationFinder;

import static org.junit.jupiter.api.Assertions.*;
import LocationFinder.models.Location;
import LocationFinder.controllers.LocationController;
import LocationFinder.repositories.LocationRepository;
import LocationFinder.services.LocationService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith (SpringRunner.class)
@SpringBootTest
class LocationTest {
    @Autowired
    private LocationService locServ;

    @Autowired
    private LocationController locController;

    @MockBean
    private LocationRepository locRepo;

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
