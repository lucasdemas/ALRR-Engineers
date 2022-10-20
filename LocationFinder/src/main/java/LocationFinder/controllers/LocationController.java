package LocationFinder.controllers;

import LocationFinder.repositories.LocationRepository;
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

        Location loc = new Location(loc_name, loc_area, loc_cost);
        locService.addLocation(loc);
        return "Saved";
    }

    @GetMapping(path="/getall")
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
        List<Location> targetLoc = locService.getLocById(loc_id);

        if(targetLoc != null) {
            Location updatedLoc = new Location(targetLoc.get(0).getName(), targetLoc.get(0).getArea(), loc_cost);
            updatedLoc.setId(targetLoc.get(0).getId());
            updatedLoc.setClaim(targetLoc.get(0).getClaim());

            locRepository.save(updatedLoc);
//            locRepository.updateCost(loc_id, loc_cost);
            return "Updated location cost";
        }

        return "Could not find location";

    }

    @PostMapping(path="/updateClaim")
    public String updateLocClaim(@RequestParam Integer loc_id,
                                @RequestParam Boolean loc_claim) {
        //Get the location based on the id provided
        List<Location> targetLoc = locService.getLocById(loc_id);

        if(targetLoc != null) {
            Location updatedLoc = new Location(targetLoc.get(0).getName(), targetLoc.get(0).getArea(), targetLoc.get(0).getCost());
            updatedLoc.setId(targetLoc.get(0).getId());
            updatedLoc.setClaim(loc_claim);

            locRepository.save(updatedLoc);
            return "Updated claim status";
        }

        return "Could not find location";

    }
}
