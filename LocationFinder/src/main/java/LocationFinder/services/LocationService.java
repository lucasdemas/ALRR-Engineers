package LocationFinder.services;

import LocationFinder.models.Location;
import LocationFinder.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locRepository;

    public List<Location> getByTemplate(Integer loc_id, String loc_name, String loc_area, Float loc_cost) {
        return locRepository.findByTemplate(loc_id, loc_name, loc_area, loc_cost);
    }
}
