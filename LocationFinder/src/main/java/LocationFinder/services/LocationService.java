package LocationFinder.services;

import LocationFinder.models.Location;
import LocationFinder.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locRepository;

//    public List<Location> getByTemplate(Integer loc_id, String loc_name, String loc_area, Double loc_cost) {
//        return locRepository.findByTemplate(loc_id, loc_name, loc_area, loc_cost);
//    }

    public List<Location> addLocation(Location loc) {
        locRepository.save(loc);
        return List.of(loc);
    }

    public List<Location> getLocById(Integer loc_id) {
        Optional<Location> target = locRepository.findById(loc_id);
        if (target.isPresent()) {
            Location locResult = target.get();
            return List.of(locResult);
        }
        return null;
    }
}
