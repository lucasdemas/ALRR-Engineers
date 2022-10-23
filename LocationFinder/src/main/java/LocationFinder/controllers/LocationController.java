package LocationFinder.controllers;

import LocationFinder.exceptions.InvaildInputException;
import LocationFinder.repositories.LocationRepository;
import LocationFinder.exceptions.NotFoundException;
import LocationFinder.models.Location;
import LocationFinder.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sun.jdi.InvalidTypeException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.List;

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
     * A method that adds a new location to the database.
     * @param loc_name
     *      The location name
     * @param loc_area
     *      The location area
     * @param loc_cost
     *      The location cost
     * @return
     *      The response for a successfully created location
     *      or the response for a caught exception
     */
    @PostMapping(path = "/add")
    public ResponseEntity<?> addNewLoc(@RequestParam final String loc_name,
                    @RequestParam final String loc_area,
                    @RequestParam final Double loc_cost) {
        try {

            System.out.println("Before new location");
            //Convert the user input into a location entity
            Location loc = new Location(loc_name, loc_area, loc_cost);

            //Check that all of the data the user input is in a valid format
            //Possibly add checker for if the user
            //inputs a string of spaces (which is invalid)
            System.out.println("Before Check Invalid");
            locService.checkInvalid(loc);

            System.out.println("Before Add");
            //Add the new location to the database
            Location savedLocation = locService.addLocation(loc);
            return new ResponseEntity<>(savedLocation, HttpStatus.CREATED);
        } catch (InvalidTypeException e)  {
            return new ResponseEntity<>(e.getMessage(),
             HttpStatus.UNPROCESSABLE_ENTITY);
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
     * A methodto update the cost of an already existing location.
     * @param loc_id
     *      The id of the location in need of changing
     * @param loc_cost
     *      The new cost of the location
     * @return
     *      The response from updating the cost or the response
     *      from the location not existing
     */
    @PostMapping(path = "/updateCost")
    public ResponseEntity<?> updateLocCost(@RequestParam final Integer loc_id,
                                @RequestParam final Double loc_cost) {
        //Get the location based on the id provided
        try {
            //Check to see if the location is in the DB and get it's data
            Location targetLoc = locService.getLocById(loc_id);

            //Update the location's data in the
            //database given the provided information by the user
            Location updatedLoc = locService.updateLocCost(targetLoc, loc_cost);
            return new ResponseEntity<>(updatedLoc, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * A method to update the claimed status of an existing location.
     * @param loc_id
     *      The id of the location in need of updating
     * @param loc_claim
     *      The new claimed status of the location
     * @return
     *      The response from updating the claimed status or the
     *      response from the location not existing
     */
    @PostMapping(path = "/updateClaim")
    public ResponseEntity<?> updateLocClaim(@RequestParam final Integer loc_id,
                                @RequestParam final Boolean loc_claim) {
        try {
            //Check to see if the location is in the DB and get it's data
            Location targetLoc = locService.getLocById(loc_id);

            //Update the location's data in the
            //database given the provided information by the user
            Location updatedLoc =
             locService.updateLocClaim(targetLoc, loc_claim);
            return new ResponseEntity<>(updatedLoc, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * A method to delete an existing location.
     * @param loc_id
     *      The id of the location to be deleted
     * @return
     *      The response from successfully deleting the location or
     *      the response from the location not existing
     */
    @PostMapping(path = "/delete")
    public ResponseEntity<?> deleteLoc(@RequestParam final Integer loc_id) {
        try {
            //Check to see if this is a valid location for this client
            Location targetLoc = locService.getLocById(loc_id);

            //Delete the location from the database
            locService.deleteLocationById(loc_id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
