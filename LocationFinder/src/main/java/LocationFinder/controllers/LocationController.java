package LocationFinder.controllers;

import LocationFinder.exceptions.InvaildInputException;
import LocationFinder.repositories.LocationRepository;
import LocationFinder.exceptions.NotFoundException;
import LocationFinder.models.Location;
import LocationFinder.models.Client;
import LocationFinder.services.ClientService;
import LocationFinder.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.sun.jdi.InvalidTypeException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/location")
public class LocationController {
    /**
     * An instance of Location Repository.
     */
    @Autowired
    private LocationRepository locRepository;
    /**
     * An instance of Location Service.
     */
    @Autowired
    private LocationService locService;

    /**
     * An instance of the client service.
     */
    @Autowired
    private ClientService clientServ;

    /**
     * Exception handling for InvalidTypeInput.
     * @param e
     *      The Number Format Exception that is going to be handled
     * @return
     *      The response for inputting a negative value for cost
     */
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<?> handleInvalidNumber(
        final NumberFormatException e) {

        return new ResponseEntity<>("Cost Must be a positive numeric value",
         HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Exception handling for InvalidNumberException.
     * @param e
     *      The Exception that is handled.
     * @return
     *      The response for the exception being handled.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleInvalidNumber(
        final IllegalArgumentException e) {

        return new ResponseEntity<>("Location Claim Must be a either true "
        + "for claimed or false for unclaimed",
         HttpStatus.UNPROCESSABLE_ENTITY);
    }



    /**
     * A method that adds a new location to the database.
     * @param locName
     *      The location name
     * @param locArea
     *      The location area
     * @param locCost
     *      The location cost
     * @param clientId
     *      The location client id
     * @return
     *      The response for a successfully created location
     *      or the response for a caught exception
     */
    @PostMapping(path = "/add")
    public ResponseEntity<?> addNewLoc(@RequestParam final String locName,
                    @RequestParam final String locArea,
                    @RequestParam final Double locCost,
                    @RequestParam final Integer clientId) {
        try {

            Client targetClient = clientServ.getClientById(clientId);
            //Convert the user input into a location entity
            Location loc = new Location(locName, locArea, locCost, clientId);

            //Check that all of the data the user input is in a valid format
            //Possibly add checker for if the user
            //inputs a string of spaces (which is invalid)
            locService.checkInvalid(loc);

            //Add the new location to the database
            Location savedLocation = locService.addLocation(loc);
            return new ResponseEntity<>(savedLocation, HttpStatus.CREATED);
        } catch (InvalidTypeException e)  {
            return new ResponseEntity<>(e.getMessage(),
             HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e)  {
            return new ResponseEntity<>(e.getMessage(),
             HttpStatus.UNPROCESSABLE_ENTITY);
        }


    }

    /**
     * A method to get all locations in the database.
     * @return
     *      The response from finding all the locations in the database.
     */
    @GetMapping(path = "/getAll")
    public ResponseEntity<?> getLocations() {
        return new ResponseEntity<>(locRepository.findAll(), HttpStatus.OK);
    }

    /**
     * A method to get all locations in the database by
     * which area they are located in.
     * @param area
     *      The area where the desired locations are
     * @return
     *      The response from finding all locations in the given area
     */
    @GetMapping(path = "/get/{area}")
    public ResponseEntity<?> getLocByArea(@PathVariable final String area) {
        return new ResponseEntity<>(locRepository.findByArea(area),
         HttpStatus.OK);
    }

    /**
     * A method to get all locations by claimed status.
     * @param isClaim
     *      The claimed status on which to search the database
     * @return
     *      The response from finding all locations by claimed
     *      status or the response from catching an exception
     */
    @GetMapping(path = "/getClaim/{isClaim}")
    public ResponseEntity<?> getLocByClaim(@PathVariable final String isClaim) {
        try {
            List<Location> searchResults =
            locService.getLocationByClaim(isClaim);
            return new ResponseEntity<>(searchResults, HttpStatus.OK);
        } catch (InvaildInputException e) {
            return new ResponseEntity<>(e.getMessage(),
             HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * A method to update the cost of an already existing location.
     * @param locId
     *      The id of the location in need of changing
     * @param locCost
     *      The new cost of the location
     * @param clientId
     *      The id of the client assosiated with the location.
     *      For authentication usage.
     * @return
     *      The response from updating the cost or the response
     *      from the location not existing
     */
    @PostMapping(path = "/updateCost")
    public ResponseEntity<?> updateLocCost(@RequestParam final Integer locId,
                                @RequestParam final Double locCost,
                                @RequestParam final Integer clientId) {
        //Get the location based on the id provided
        try {
            //Check to see if the location is in the DB and get it's data
            Location targetLoc = locService.getLocById(locId);
            //To Do: Add a client authentication method in service

            locService.checkInvalid(targetLoc);

            //Update the location's data in the
            //database given the provided information by the user
            Location updatedLoc = locService.updateLocCost(targetLoc, locCost);
            return new ResponseEntity<>(updatedLoc, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidTypeException e)  {
            return new ResponseEntity<>(e.getMessage(),
            HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e)  {
            return new ResponseEntity<>(e.getMessage(),
            HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    /**
     * A method to update the claimed status of an existing location.
     * @param locId
     *      The id of the location in need of updating
     * @param locClaim
     *      The new claimed status of the location
     * @param clientId
     *      The id of the client assosiated with the location.
     *      For authentication usage.
     * @return
     *      The response from updating the claimed status or the
     *      response from the location not existing
     */
    @PostMapping(path = "/updateClaim")
    public ResponseEntity<?> updateLocClaim(@RequestParam final Integer locId,
                                @RequestParam final Boolean locClaim,
                                @RequestParam final Integer clientId) {
        try {
            //Check to see if the location is in the DB and get it's data
            Location targetLoc = locService.getLocById(locId);
            //To Do: Add a client authentication method in service
            locService.checkInvalid(targetLoc);

            //Update the location's data in the
            //database given the provided information by the user
            Location updatedLoc =
             locService.updateLocClaim(targetLoc, locClaim);
            return new ResponseEntity<>(updatedLoc, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidTypeException e)  {
            return new ResponseEntity<>(e.getMessage(),
            HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e)  {
            return new ResponseEntity<>(e.getMessage(),
            HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * A method to delete an existing location.
     * @param locId
     *      The id of the location to be deleted
     * @param clientId
     *      The id of the client assosiated with the location.
     *      For authentication usage.
     * @return
     *      The response from successfully deleting the location or
     *      the response from the location not existing
     */
    @PostMapping(path = "/delete")
    public ResponseEntity<?> deleteLoc(@RequestParam final Integer locId,
                                       @RequestParam final Integer clientId) {
        try {
            //To Do: Add a client authentication method in service
            //Delete the location from the database
            locService.deleteLocationById(locId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
