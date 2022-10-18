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
        locRepository.save(loc);
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
}
