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

@Controller
@RequestMapping(path="/location")
public class LocationController {
    @Autowired
    private LocationRepository locRepository;

    @Autowired
    private LocationService locService;
    
}
