package LocationFinder.controllers;

import LocationFinder.exceptions.InvaildInputException;
import LocationFinder.repositories.LocationRepository;
import LocationFinder.exceptions.NotFoundException;
import LocationFinder.models.Location;
import LocationFinder.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.sun.jdi.InvalidTypeException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@RestController
@RequestMapping(path="/location")
public class LocationController {
    @Autowired
    private LocationRepository locRepository;

    @Autowired
    private LocationService locService;

    //Exception handling for InvalidTypeInput
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<?> handleInvalidNumber(NumberFormatException e){

        return new ResponseEntity<>("Cost Must be a positive numeric value", HttpStatus.UNPROCESSABLE_ENTITY);
    }



    //Call to add a new location to the database
    @PostMapping(path="/add")
    public ResponseEntity<?> addNewLoc(@RequestParam String loc_name,
                    @RequestParam String loc_area,
                    @RequestParam Double loc_cost) {
        try {

            System.out.println("Before new location");
            //Convert the user input into a location entity
            Location loc = new Location(loc_name, loc_area, loc_cost);

            //Check that all of the data the user input is in a valid format
            //Possibly add checker for if the user inputs a string of spaces (which is invalid)
            System.out.println("Before Check Invalid");
            locService.checkInvalid(loc);

            System.out.println("Before Add");
            //Add the new location to the database
            Location savedLocation = locService.addLocation(loc);
            return new ResponseEntity<>(savedLocation, HttpStatus.CREATED);
        }
        catch (InvalidTypeException e)  {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        catch (Exception e)  {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }


    }

    //Call to get back all of the location in the database
    @GetMapping(path="/getAll")
    public ResponseEntity<?> getLocations() {
        return new ResponseEntity<>(locRepository.findAll(), HttpStatus.OK);
    }

    //Call to get all of the locations from a specific area
    @GetMapping(path="/get/{area}")
    public ResponseEntity<?> getLocByArea(@PathVariable String area) {
        return new ResponseEntity<>(locRepository.findByArea(area), HttpStatus.OK);
    }

    //Call to get all of the locations that are either claimed or unclaimed given the user's input
    @GetMapping(path="/getClaim/{isClaim}")
    public ResponseEntity<?> getLocByClaim(@PathVariable String isClaim) {
        try {
            List<Location> searchResults = locService.getLocationByClaim(isClaim);
            return new ResponseEntity<>(searchResults, HttpStatus.OK);
        }
        catch (InvaildInputException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    //Call to update the cost of a location
    @PostMapping(path="/updateCost")
    public ResponseEntity<?> updateLocCost(@RequestParam Integer loc_id,
                                @RequestParam Double loc_cost) {
        //Get the location based on the id provided
        try {
            //Check to see if the location is in the DB and get it's data
            Location targetLoc = locService.getLocById(loc_id);

            //Update the location's data in the database given the provided information by the user
            Location updatedLoc = locService.updateLocCost(targetLoc, loc_cost);
            return new ResponseEntity<>(updatedLoc, HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    //Call to update the claim status of a given location
    @PostMapping(path="/updateClaim")
    public ResponseEntity<?> updateLocClaim(@RequestParam Integer loc_id,
                                @RequestParam Boolean loc_claim) {
        try {
            //Check to see if the location is in the DB and get it's data
            Location targetLoc = locService.getLocById(loc_id);

            //Update the location's data in the database given the provided information by the user
            Location updatedLoc = locService.updateLocClaim(targetLoc, loc_claim);
            return new ResponseEntity<>(updatedLoc, HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    //Call to delete a location from the database
    @PostMapping(path="/delete")
    public ResponseEntity<?> deleteLoc(@RequestParam Integer loc_id) {
        try {
            //Check to see if this is a valid location for this client
            Location targetLoc = locService.getLocById(loc_id);

            //Delete the location from the database
            locService.deleteLocationById(loc_id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
