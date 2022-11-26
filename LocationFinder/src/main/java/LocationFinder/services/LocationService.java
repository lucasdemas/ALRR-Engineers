package LocationFinder.services;


import LocationFinder.models.Location;
import LocationFinder.exceptions.NotFoundException;
import LocationFinder.exceptions.InvaildInputException;
import LocationFinder.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sun.jdi.InvalidTypeException;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    /**
     * An instance of location repository.
     */
    @Autowired
    private LocationRepository locRepository;

    /**
     * A method to add a location.
     * @param loc
     * @return
     *      The location that is saved to the database
     */
    public Location addLocation(final Location loc) {
        loc.setClaim(false);
        Location fullLocation = locRepository.save(loc);
        return fullLocation;
    }

    /**
     * A method to get a location by its id.
     * @param locId
     * @return
     *      The location corresponding to the given id
     * @throws NotFoundException
     *      The location does not exist in the database
     */
    public Location getLocById(final Integer locId) throws NotFoundException {
        Optional<Location> target = locRepository.findById(locId);
        if (target.isPresent()) {
            Location locResult = target.get();
            return locResult;
        } else {
            throw new NotFoundException("Location id does not exist");
        }
    }

    /**
     * A method to check if an input is of invalid type.
     * @param loc
     * @throws InvalidTypeException
     *      The input is of invalid type
     */
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

    public String checkArea(final String locArea) throws InvalidTypeException {
        if (locArea.trim().isEmpty()) {
            throw new InvalidTypeException("Location area cannot be blank");
        }
        return locArea;
    }

    public double checkCost(final double locCost) throws InvalidTypeException {
        if (locCost < 0) {
            throw new InvalidTypeException("Location Cost can't be a negative number");
        }
        return locCost;
    }

    /**
     * A method to get locations by claimed status.
     * @param claimStatus
     * @return
     *      A list of locations
     * @throws InvaildInputException
     *      The claimed status input is invalid
     */
    public List<Location> getLocationByClaim(
        final String claimStatus, final Integer clientId) throws InvaildInputException {
        if (claimStatus.toLowerCase().equals("claimed")) {
            return locRepository.findByClaim(true, clientId);
        } else if (claimStatus.toLowerCase().equals("unclaimed")) {
            return locRepository.findByClaim(false, clientId);
        } else {
            throw new InvaildInputException(
                "Please specify whether you are searching"
                + "for claimed or unclaimed spots");
        }
    }

    /**
     * A method to update the claimed status of an existing location.
     * @param loc
     * @param locClaim
     * @return
     *      The updated location
     * @throws IllegalArgumentException
     *      One of the inputs is illegal
     */
    public Location updateLocClaim(
        final Location loc,
        final Boolean locClaim)
        throws IllegalArgumentException, InvalidTypeException {

        Location updatedLoc = new Location(
            loc.getName(), loc.getArea(), loc.getCost(), loc.getClientId());
        updatedLoc.setId(loc.getId());
        updatedLoc.setClaim(locClaim);
        checkInvalid(updatedLoc);
        locRepository.save(updatedLoc);
        return updatedLoc;
    }

    /**
     * A method to update the cost of a location.
     * @param loc
     * @param locCost
     * @return
     *      The updated location
     */
    public Location updateLocCost(
        final Location loc, final Double locCost)
        throws InvalidTypeException {
        Location updatedLoc =
        new Location(loc.getName(), loc.getArea(), locCost, loc.getClientId());
        updatedLoc.setId(loc.getId());
        updatedLoc.setClaim(loc.getClaim());
        checkInvalid(updatedLoc);
        locRepository.save(updatedLoc);
        return updatedLoc;
    }

    /**
     * A method to delete a location by its id.
     * @param id
     * @throws NotFoundException
     *      The location does not exist in the database.
     */
    public void deleteLocationById(final Integer id) throws NotFoundException {
        if (locRepository.existsById(id)) {
        locRepository.deleteById(id);
        } else {
            throw new NotFoundException("There is no location with that id");

        }

    }
}

