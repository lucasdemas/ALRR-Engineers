package LocationFinder.controllers;

import LocationFinder.exceptions.InvaildInputException;
import LocationFinder.repositories.LocationRepository;
import LocationFinder.exceptions.NotFoundException;
import LocationFinder.exceptions.InvalidTypeException;
import LocationFinder.models.Location;
import LocationFinder.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/location")
public class LocationController {
    @Autowired
    private LocationRepository locRepository;

    @Autowired
    private LocationService locService;

    //Call to add a new location to the database
    @PostMapping(path="/add")
    public String addNewLoc(@RequestParam String loc_name,
                    @RequestParam String loc_area,
                    @RequestParam Double loc_cost) {
        try {
            //Convert the user input into a location entity
            Location loc = new Location(loc_name, loc_area, loc_cost);

            //Check that all of the data the user input is in a valid format
            //Possibly add checker for if the user inputs a string of spaces (which is invalid)
            locService.checkInvalid(loc);

            //Add the new location to the database
            locService.addLocation(loc);
            return "Saved";
        }
        catch (InvalidTypeException e)  {
            return e.getMessage();
        }
    }

    //Call to get back all of the location in the database
    @GetMapping(path="/getAll")
    Iterable<Location> getLocations() {
        return locRepository.findAll();
    }

    //Call to get all of the locations from a specific area
    @GetMapping(path="/get/{area}")
    List<Location> getLocByArea(@PathVariable String area) {
        return locRepository.findByArea(area);
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
    public String updateLocCost(@RequestParam Integer loc_id,
                                @RequestParam Double loc_cost) {
        //Get the location based on the id provided
        try {
            //Check to see if the location is in the DB and get it's data
            Location targetLoc = locService.getLocById(loc_id);

            //Update the location's data in the database given the provided information by the user
            Location updatedLoc = locService.updateLocCost(targetLoc, loc_cost);
            return "Updated location cost";
        }
        catch (NotFoundException e) {
            return e.getMessage();
        }
    }

    //Call to update the claim status of a given location
    @PostMapping(path="/updateClaim")
    public String updateLocClaim(@RequestParam Integer loc_id,
                                @RequestParam Boolean loc_claim) {
        try {
            //Check to see if the location is in the DB and get it's data
            Location targetLoc = locService.getLocById(loc_id);

            //Update the location's data in the database given the provided information by the user
            Location updatedLoc = locService.updateLocClaim(targetLoc, loc_claim);
            return "Updated claim status";
        }
        catch (NotFoundException e) {
            return e.getMessage();
        }
    }

    //Call to delete a location from the database
    @PostMapping(path="/delete")
    public String deleteLoc(@RequestParam Integer loc_id) {
        try {
            //Check to see if this is a valid location for this client
            Location targetLoc = locService.getLocById(loc_id);

            //Delete the location from the database
            locService.deleteLocationById(loc_id);
            return "Deleted location";
        }
        catch (NotFoundException e) {
            return e.getMessage();
        }
    }
}
