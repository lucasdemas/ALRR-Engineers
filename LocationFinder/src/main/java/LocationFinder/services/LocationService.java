package LocationFinder.services;

import LocationFinder.models.Location;
import LocationFinder.exceptions.NotFoundException;
import LocationFinder.exceptions.InvalidTypeException;
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

    public Location addLocation(Location loc) {
        loc.setClaim(false);
        locRepository.save(loc);
        return loc;
    }


    public Location getLocById(Integer loc_id) throws NotFoundException {
        Optional<Location> target = locRepository.findById(loc_id);
        if (target.isPresent()) {
            Location locResult = target.get();
            return locResult;
        } else {
            throw new NotFoundException("Location id does not exist");
        }
    }

    public void checkInvalid(Location loc) throws InvalidTypeException {
        if (loc.getCost() < 0){
            throw new InvalidTypeException("Location Cost can't be a negative number");
        }
    }

    public Location updateLocClaim(Location loc, Boolean loc_claim) {
        Location updatedLoc = new Location(loc.getName(), loc.getArea(), loc.getCost());
        updatedLoc.setId(loc.getId());
        updatedLoc.setClaim(loc_claim);
        locRepository.save(updatedLoc);
        return updatedLoc;
    }

    public Location updateLocCost(Location loc, Double loc_cost) {
        Location updatedLoc = new Location(loc.getName(), loc.getArea(), loc_cost);
        updatedLoc.setId(loc.getId());
        updatedLoc.setClaim(loc.getClaim());
        locRepository.save(updatedLoc);
        return updatedLoc;
    }
}

