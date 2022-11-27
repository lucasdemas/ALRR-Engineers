package LocationFinder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import LocationFinder.controllers.ClientController;
import LocationFinder.controllers.LocationController;

import LocationFinder.models.Location;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.jupiter.api.function.Executable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MissingServletRequestParameterException;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
class EndToEndTest {
    /**
     * An instance of the client controller.
     */
    @Autowired
    private ClientController clientCont;

    /**
     * An instance of the location controller.
     */
    @Autowired
    private LocationController locCont;

    @Test
    public void testAuthenticateSuccess() {
        String authToken = "FoEv6p4c6r2ilAvRkRH39eub9LF9J+7GdoA7vnZK/j7djqRN3F" +
                "IACngZuYjVeicz0jXtxkXgG3mgUEi0psES5OD687udbMBIy0TE/WBkURdWP/4O" +
                "Mep0XWgVvTg9RmblWb9BjzrWsv9LAMjeb5x4zcRI0QDXhqcEuWuLaJkgUOPA3y/" +
                "2/XzKTnIzbk5Jrccd8bTUSupUNmNjRw2WTrLYROGi/wvRUyW2zhYMIuy2fbzKra" +
                "3jClYkbYpBja9ZXWfnvgx3bAdaznWPF99aAOuS0gMBDmRhzjG4a+2L2lHrw6U8y" +
                "MNN2p9jz4Iy7e7Ky9QI5Hrjyrqwda6CfKmsbpxiuw==";
        ResponseEntity<?> clientId = clientCont.authenticateClient(authToken);

        //Make sure the value returned is correct and that the response code is the correct one
        assertEquals(clientId.getStatusCode(), HttpStatus.OK);
        assertEquals(clientId.getBody(), 1);
    }

    @Test
    public void testAuthenticateInvalidToken() {
        String authToken = "";
        ResponseEntity<?> clientId = clientCont.authenticateClient(authToken);

        //Make sure the value returned is correct and that the response code is the correct one
        assertEquals(clientId.getStatusCode(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void testAuthenticateNotFound() {
        String authToken = "TQByAAVqja+ztMsniKKS5Bp6Wk6QEISQHAqEv49lTJiosj7IQMX2U5jpEvN/" +
                "muJkcP06gLunn6wviBM7STluqfyvpkbWZuuirM2KPYC8Td5ml0LyTS1Bq6X3nKDGi3Km6oJA" +
                "Zezb8/y0lwfrCHRiOQoFSVWSSrZv/zqtAWsyPS5sPYvHxhj49F/BpYFlHFYWtpNXVBfMA0bN" +
                "RY//A9SFjQeEJRRhyo1A2CELbmY8L7eVMWSUOOzJa8Ke3ihEqYXHpxRDExf3eW9G+iUDoBj" +
                "DvxW7t77Q9pahOrXUgnHDo1QE91eX8NIP4qpcUFUT5lyF/CZXGmwl1apH1Bv0q4eCDA==";
        ResponseEntity<?> clientId = clientCont.authenticateClient(authToken);

        //Make sure the value returned is correct and that the response code is the correct one
        assertEquals(clientId.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test void testAddLocationSuccess() {
        String authToken = "FoEv6p4c6r2ilAvRkRH39eub9LF9J+7GdoA7vnZK/j7djqRN3F" +
                "IACngZuYjVeicz0jXtxkXgG3mgUEi0psES5OD687udbMBIy0TE/WBkURdWP/4O" +
                "Mep0XWgVvTg9RmblWb9BjzrWsv9LAMjeb5x4zcRI0QDXhqcEuWuLaJkgUOPA3y/" +
                "2/XzKTnIzbk5Jrccd8bTUSupUNmNjRw2WTrLYROGi/wvRUyW2zhYMIuy2fbzKra" +
                "3jClYkbYpBja9ZXWfnvgx3bAdaznWPF99aAOuS0gMBDmRhzjG4a+2L2lHrw6U8y" +
                "MNN2p9jz4Iy7e7Ky9QI5Hrjyrqwda6CfKmsbpxiuw==";
        String locName = "Test";
        String locArea = "TestArea";
        ResponseEntity<?> savedLocation = locCont.addNewLoc(locName, locArea, 20.0, 1, authToken);
        assertEquals(savedLocation.getStatusCode(), HttpStatus.CREATED);
    }

    @Test void testDeleteLocationSuccess() {
        //First add the location
        String authToken = "FoEv6p4c6r2ilAvRkRH39eub9LF9J+7GdoA7vnZK/j7djqRN3F" +
                "IACngZuYjVeicz0jXtxkXgG3mgUEi0psES5OD687udbMBIy0TE/WBkURdWP/4O" +
                "Mep0XWgVvTg9RmblWb9BjzrWsv9LAMjeb5x4zcRI0QDXhqcEuWuLaJkgUOPA3y/" +
                "2/XzKTnIzbk5Jrccd8bTUSupUNmNjRw2WTrLYROGi/wvRUyW2zhYMIuy2fbzKra" +
                "3jClYkbYpBja9ZXWfnvgx3bAdaznWPF99aAOuS0gMBDmRhzjG4a+2L2lHrw6U8y" +
                "MNN2p9jz4Iy7e7Ky9QI5Hrjyrqwda6CfKmsbpxiuw==";
        String locName = "Test";
        String locArea = "TestArea";
        ResponseEntity<?> savedLocation = locCont.addNewLoc(locName, locArea, 20.0, 1, authToken);

        //Extract the location id from the response body of the function that adds the location
        Location extractedLocation = (Location) savedLocation.getBody();
        Integer extractedId = extractedLocation.getId();

        //Execute the delete location when the client data matches
        ResponseEntity<?> deletedLocation = locCont.deleteLoc(extractedId, 1, authToken);

        assertEquals(deletedLocation.getStatusCode(), HttpStatus.OK);
    }

    @Test void testUpdateCostSuccess() {
        //First add the location
        String authToken = "FoEv6p4c6r2ilAvRkRH39eub9LF9J+7GdoA7vnZK/j7djqRN3F" +
                "IACngZuYjVeicz0jXtxkXgG3mgUEi0psES5OD687udbMBIy0TE/WBkURdWP/4O" +
                "Mep0XWgVvTg9RmblWb9BjzrWsv9LAMjeb5x4zcRI0QDXhqcEuWuLaJkgUOPA3y/" +
                "2/XzKTnIzbk5Jrccd8bTUSupUNmNjRw2WTrLYROGi/wvRUyW2zhYMIuy2fbzKra" +
                "3jClYkbYpBja9ZXWfnvgx3bAdaznWPF99aAOuS0gMBDmRhzjG4a+2L2lHrw6U8y" +
                "MNN2p9jz4Iy7e7Ky9QI5Hrjyrqwda6CfKmsbpxiuw==";
        String locName = "Test";
        String locArea = "TestArea";
        ResponseEntity<?> savedLocation = locCont.addNewLoc(locName, locArea, 20.0, 1, authToken);

        //Extract the location id from the response body of the function that adds the location
        Location extractedLocation = (Location) savedLocation.getBody();
        Integer extractedId = extractedLocation.getId();

        //Execute the delete location when the client data matches
        ResponseEntity<?> updatedLocation = locCont.updateLocCost(extractedId, 30.0, 1, authToken);

        //Extract the updated location from the response entity
        Location extractedLocationUpdated = (Location) updatedLocation.getBody();
        Double updatedCost = extractedLocationUpdated.getCost();

        //Check that cost was updated correctly
        assertEquals(updatedCost, 30.0);
    }

    @Test void testUpdateClaimSuccess() {
        //First add the location
        String authToken = "FoEv6p4c6r2ilAvRkRH39eub9LF9J+7GdoA7vnZK/j7djqRN3F" +
                "IACngZuYjVeicz0jXtxkXgG3mgUEi0psES5OD687udbMBIy0TE/WBkURdWP/4O" +
                "Mep0XWgVvTg9RmblWb9BjzrWsv9LAMjeb5x4zcRI0QDXhqcEuWuLaJkgUOPA3y/" +
                "2/XzKTnIzbk5Jrccd8bTUSupUNmNjRw2WTrLYROGi/wvRUyW2zhYMIuy2fbzKra" +
                "3jClYkbYpBja9ZXWfnvgx3bAdaznWPF99aAOuS0gMBDmRhzjG4a+2L2lHrw6U8y" +
                "MNN2p9jz4Iy7e7Ky9QI5Hrjyrqwda6CfKmsbpxiuw==";
        String locName = "Test";
        String locArea = "TestArea";
        ResponseEntity<?> savedLocation = locCont.addNewLoc(locName, locArea, 20.0, 1, authToken);

        //Extract the location id from the response body of the function that adds the location
        Location extractedLocation = (Location) savedLocation.getBody();
        Integer extractedId = extractedLocation.getId();

        //Execute the delete location when the client data matches
        ResponseEntity<?> updatedLocation = locCont.updateLocClaim(extractedId, true, 1, authToken);

        //Extract the updated location from the response entity
        Location extractedLocationUpdated = (Location) updatedLocation.getBody();
        Boolean updatedClaim = extractedLocationUpdated.getClaim();

        //Check that cost was updated correctly
        assertEquals(updatedClaim, true);
    }
}