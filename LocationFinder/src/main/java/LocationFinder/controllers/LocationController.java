package LocationFinder.controllers;

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
    
    @PostMapping(path="/add")
    public String addNewLoc(@RequestParam String loc_name,
                    @RequestParam String loc_area,
                    @RequestParam Double loc_cost) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        try {
            Location loc = new Location(loc_name, loc_area, loc_cost);
            locService.checkInvalid(loc);

            locService.addLocation(loc);
            return "Saved";
        }
        catch (InvalidTypeException e)  {
            return e.getMessage();
        }
    }

    @GetMapping(path="/getAll")
    Iterable<Location> getLocations() {
        return locRepository.findAll();
    }
    
    @GetMapping(path="/get/{area}")
    List<Location> getLocByArea(@PathVariable String area) {
        return locRepository.findByArea(area);
    }

    @GetMapping(path="/getClaim/{isClaim}")
    List<Location> getLocByClaim(@PathVariable String isClaim) {
        if(isClaim.toLowerCase().equals("claimed")) {
            return locRepository.findByClaim(true);
        }
        else if(isClaim.toLowerCase().equals("unclaimed")) {
            return locRepository.findByClaim(false);
        }
        
        return locRepository.findByClaim(null);
    }

    @PostMapping(path="/updateCost")
    public String updateLocCost(@RequestParam Integer loc_id,
                                @RequestParam Double loc_cost) {
        //Get the location based on the id provided
        try {
            //Check to see if the location is in the DB and get it's data
            Location targetLoc = locService.getLocById(loc_id);

            //Update the location's data in the database given the provided information by the user
            Location updatedLoc = locService.updateLocCost(targetLoc, loc_cost);
//            locRepository.updateCost(loc_id, loc_cost);
            return "Updated location cost";
        }
        catch (NotFoundException e) {
            return e.getMessage();
        }
    }

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

    @PostMapping(path="/delete")
    public String deleteLoc(@RequestParam Integer loc_id) {
        try {
            //Check to see if this is a valid location for this client
            Location targetLoc = locService.getLocById(loc_id);
            locRepository.deleteById(loc_id);
            return "Deleted location";
        }
        catch (NotFoundException e) {
            return e.getMessage();
        }
    }
}
