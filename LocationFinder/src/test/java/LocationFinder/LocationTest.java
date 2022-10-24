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
import com.sun.jdi.InvalidTypeException;

import java.util.List;
import java.util.*;


@RunWith (SpringRunner.class)
@SpringBootTest
class LocationTest {
    @Autowired
    private LocationService locServ;

    @MockBean
    private LocationRepository locRepo;

    @Test
    public void testDeleteLocationException() {
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
        loc1.setId(null);
        loc1.setClaim(false);
        loc1.setName("Times Square");
        loc1.setArea("New York");
        loc1.setCost(20.0);

        //Create a second mock location to represent that the mock client will be given the location id 100 when added to the repo
        Location loc2 = new Location();
        loc2.setClaim(false);
        loc2.setId(100);
        loc2.setName("Times Square");
        loc2.setArea("New York");
        loc2.setCost(20.0);

        //Have the mock return the second mock location when it will save the first one to the location repo
        Mockito.when(locRepo.save(loc1)).thenReturn(loc2);

        //Check to see that the fist mock location's id was updated to 100 after being added to the location repo
        assertEquals(locServ.addLocation(loc1).getId(), 100);
    }

    @Test
    public void testGetLocationById() throws NotFoundException {

        //Create a mock location that will represent the data that would be passed in for when a new location is added
        Location loc1 = new Location();
        loc1.setClaim(false);
        loc1.setId(100);
        loc1.setName("Times Square");
        loc1.setArea("New York");
        loc1.setCost(20.0);


        //Have the client be returned in the format that findById is looking for in the cleintRepo
        Optional<Location> optLocation = Optional.of(loc1);

        //Have the mock return the formatted client when it look for a client with the id 100
        Mockito.when(locRepo.findById(100)).thenReturn(optLocation);

        //Get the result of searching for a client with the id 100
        Location locationResult = locServ.getLocById(100);

        //Check to see that the results of the service returned the correct data
        assertEquals(locationResult.getClaim(), false);
        assertEquals(locationResult.getId(), 100);
        assertEquals(locationResult.getName(), "Times Square");
        assertEquals(locationResult.getArea(), "New York");
        assertEquals(locationResult.getCost(), 20.0);
    }

    @Test
    public void testGetLocationByIdException() {
        assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                //Tell the mock repo that there is no client with id 100
                Mockito.when(locRepo.existsById(100)).thenReturn(false);

                //Try and get a client with the id 100 (which results in a NotFound exception)
                locServ.getLocById(100);
            }
        });
    }
    @Test
    public void testGetLocationByClaim() throws InvaildInputException {

        //Create a mock location that will represent the data that would be passed in for when a new location is added
        Location loc1 = new Location();
        loc1.setClaim(false);
        loc1.setId(100);
        loc1.setName("Times Square");
        loc1.setArea("New York");
        loc1.setCost(20.0);

        Location loc2 = new Location();
        loc2.setClaim(false);
        loc2.setId(100);
        loc2.setName("Midtown");
        loc2.setArea("New York");
        loc2.setCost(30.0);

        List<Location> location_list = new LinkedList();
        location_list.add(loc1);
        location_list.add(loc2);


        Mockito.when(locRepo.findByClaim(false)).thenReturn(location_list);
        //Have the client be returned in the format that findById is looking for in the cleintRepo
        List<Location> searchResults = locServ.getLocationByClaim("unclaimed");

        //Have the mock return the formatted client when it look for a client with the id 100

        //Check to see that the results of the service returned the correct data
        assertEquals(searchResults.get(0).getClaim(), false);
        assertEquals(searchResults.get(0).getId(), 100);
        assertEquals(searchResults.get(0).getName(), "Times Square");
        assertEquals(searchResults.get(0).getArea(), "New York");
        assertEquals(searchResults.get(0).getCost(), 20.0);

        assertEquals(searchResults.get(1).getClaim(), false);
        assertEquals(searchResults.get(1).getId(), 100);
        assertEquals(searchResults.get(1).getName(), "Midtown");
        assertEquals(searchResults.get(1).getArea(), "New York");
        assertEquals(searchResults.get(1).getCost(), 30.0);
    }

    @Test
    public void testGetLocationByClaimException() throws InvaildInputException {
        assertThrows(InvaildInputException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                locServ.getLocationByClaim("invalid");
            }
        });
    }


    @Test
    public void testCheckInvalidClaim() throws InvalidTypeException {
        assertThrows(InvalidTypeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Location loc1 = new Location();
                loc1.setClaim(false);
                loc1.setId(100);
                loc1.setName("Times square");
                loc1.setArea(" ");
                loc1.setCost(20.0);
                locServ.checkInvalid(loc1);
            }
        });
    }
    @Test
    public void testCheckInvalidCost() throws InvalidTypeException {
        assertThrows(InvalidTypeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Location loc1 = new Location();
                loc1.setClaim(false);
                loc1.setId(100);
                loc1.setName("Times square");
                loc1.setArea("New York");
                loc1.setCost(-20.0);
                locServ.checkInvalid(loc1);
            }
        });
    }

    @Test
    public void testupdateLocClaim()  {
        //Create a mock client who's email we will update
        Location loc1 = new Location();
        loc1.setClaim(false);
        loc1.setId(100);
        loc1.setName("Times Square");
        loc1.setArea("New York");
        loc1.setCost(20.0);


        //Have the client be returned in the format that findById is looking for in the cleintRepo
        Optional<Location> optLoc = Optional.of(loc1);

        //Have the mock return the formatted client when it look for a client with the id 100
        Mockito.when(locRepo.findById(100)).thenReturn(optLoc);

        //Have the mock return true when it checks to see if there is a client with the id 100 in the repo
        Mockito.when(locRepo.existsById(100)).thenReturn(true);

        //Save the client to the repository
        Mockito.when(locRepo.save(loc1)).thenReturn(loc1);

        //Update the mock client's email
        Location locResult = locServ.updateLocClaim(loc1,true);

        //Check to see that the client's email was updated successfully
        assertEquals(locResult.getClaim(), true);
    }


    @Test
    public void testUpdateLocCost()  {
        //Create a mock client who's email we will update
        Location loc1 = new Location();
        loc1.setClaim(false);
        loc1.setId(100);
        loc1.setName("Times Square");
        loc1.setArea("New York");
        loc1.setCost(20.0);


        //Have the client be returned in the format that findById is looking for in the cleintRepo
        Optional<Location> optLoc = Optional.of(loc1);

        //Have the mock return the formatted client when it look for a client with the id 100
        Mockito.when(locRepo.findById(100)).thenReturn(optLoc);

        //Have the mock return true when it checks to see if there is a client with the id 100 in the repo
        Mockito.when(locRepo.existsById(100)).thenReturn(true);

        //Save the client to the repository
        Mockito.when(locRepo.save(loc1)).thenReturn(loc1);

        //Update the mock client's email
        Location locResult = locServ.updateLocCost(loc1,10000.0);

        //Check to see that the client's email was updated successfully
        assertEquals(locResult.getCost(), 10000.0);
    }
}
