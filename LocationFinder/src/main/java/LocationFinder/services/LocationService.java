package LocationFinder.services;


import LocationFinder.models.Location;
import LocationFinder.exceptions.NotFoundException;
import LocationFinder.exceptions.InvaildInputException;
import LocationFinder.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sun.jdi.InvalidTypeException;
import java.util.List;
import java.util.Optional;
import java.util.*;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locRepository;

    public Location addLocation(final Location loc) {
        loc.setClaim(false);
        Location fullLocation = locRepository.save(loc);
        return fullLocation;
    }


    public Location getLocById(final Integer loc_id) throws NotFoundException {
        Optional<Location> target = locRepository.findById(loc_id);
        if (target.isPresent()) {
            Location locResult = target.get();
            return locResult;
        } else {
            throw new NotFoundException("Location id does not exist");
        }
    }

    public void checkInvalid(final Location loc) throws InvalidTypeException {
        if (loc.getName().trim().isEmpty()) {
            throw new InvalidTypeException("Location name cannot be blank");
        } else if (loc.getArea().trim().isEmpty()) {
            throw new InvalidTypeException("Location area cannot be blank");
        } else if (loc.getCost() < 0) {
            throw new
            InvalidTypeException("Location Cost can't be a negative number");
        }
    }


    public List<Location> getLocationByClaim(String claim_status) throws InvaildInputException {
        if(claim_status.toLowerCase().equals("claimed")) {
            List<Location> location_list = new LinkedList();
            location_list = locRepository.findByClaim(true);
            return location_list;
        }
        else if(claim_status.toLowerCase().equals("unclaimed")) {
            List<Location> location_list = new LinkedList();
            location_list = locRepository.findByClaim(false);
            return location_list;
        }
        else {
            throw new InvaildInputException("Please specify whether you are searching for claimed or unclaimed spots");
        }
    }

    public Location updateLocClaim(Location loc, Boolean loc_claim) throws IllegalArgumentException{
        Location updatedLoc = new Location(loc.getName(), loc.getArea(), loc.getCost());
        updatedLoc.setId(loc.getId());
        updatedLoc.setClaim(loc_claim);
        locRepository.save(updatedLoc);
        return updatedLoc;
    }

    public Location updateLocCost(final Location loc, final Double loc_cost) {
        Location updatedLoc =
        new Location(loc.getName(), loc.getArea(), loc_cost);
        updatedLoc.setId(loc.getId());
        updatedLoc.setClaim(loc.getClaim());
        locRepository.save(updatedLoc);
        return updatedLoc;
    }

    public void deleteLocationById(Integer id) throws NotFoundException{
        if(locRepository.existsById(id)) {
        locRepository.deleteById(id);
        }
        else {
            throw new NotFoundException("There is no location with that id");

        }

    }
}

