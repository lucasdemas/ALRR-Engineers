package LocationFinder.controllers;

import LocationFinder.exceptions.InvaildInputException;
import LocationFinder.repositories.LocationRepository;
import LocationFinder.exceptions.NotFoundException;
import LocationFinder.models.Location;
import LocationFinder.models.Client;
import LocationFinder.services.ClientService;
import LocationFinder.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.sun.jdi.InvalidTypeException;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@RestController
@RequestMapping(path = "/location")
public class LocationController {
    /**
     * An instance of Location Repository.
     */
    @Autowired
    private LocationRepository locRepository;
    /**
     * An instance of Location Service.
     */
    @Autowired
    private LocationService locService;

    /**
     * An instance of the client service.
     */
    @Autowired
    private ClientService clientServ;

    /**
     * Exception handling for InvalidTypeInput.
     * @param e
     *      The Number Format Exception that is going to be handled
     * @return
     *      The response for inputting a negative value for cost
     */
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<?> handleInvalidNumber(
        final NumberFormatException e) {

        return new ResponseEntity<>("Cost Must be a positive numeric value",
         HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Exception handling for InvalidNumberException.
     * @param e
     *      The Exception that is handled.
     * @return
     *      The response for the exception being handled.
     */
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<?> handleInvalidNumber(
//        final IllegalArgumentException e) {
//
//        return new ResponseEntity<>("The Authentication token must be a valid token!",
//         HttpStatus.UNPROCESSABLE_ENTITY);
//    }

    /**
     * A method that adds a new location to the database.
     * @param locName
     *      The location name
     * @param locArea
     *      The location area
     * @param locCost
     *      The location cost
     * @param clientId
     *      The location client id
     * @param clientAuthToken
     *      The authentication token of the client attempting to add a new location
     * @return
     *      The response for a successfully created location
     *      or the response for a caught exception
     */
    @CrossOrigin()
    @PostMapping(path = "/add")
    public ResponseEntity<?> addNewLoc(@RequestParam final String locName,
                    @RequestParam final String locArea,
                    @RequestParam final Double locCost,
                    @RequestParam final Integer clientId,
                    @RequestParam final String clientAuthToken){
        try {
            System.out.println("you are here");
            //verify that the auth token provided by the client is not blank
            clientServ.checkAuthTokenBlank(clientAuthToken);
            System.out.println("you are here1");
            //verify that there is a client with the provided client id in the database
            clientServ.getClientById(clientId);
            System.out.println("you are here2");
            //Decrypt the provided token
            String decryptedToken = clientServ.decryptToken(clientAuthToken);
            System.out.println("you are here3");
            //get the client that the auth token belongs to
            Client authClient = clientServ.getClientByAuth(decryptedToken);

            //make sure that the client id the auth token is associated to matches the one passed in the request
            //if they match proceed to check the other inputs to see if they are all valid and add the new location
            //if they do not match, throw an error
            if (clientId.equals(authClient.getId())) {
                System.out.println("you are here4");
                //Convert the user input into a location entity
                Location loc = new Location(locName, locArea, locCost, clientId);

                //Check that all of the data the user input is in a valid format
                //Possibly add checker for if the user
                //inputs a string of spaces (which is invalid)
                locService.checkInvalid(loc);

                //Add the new location to the database
                Location savedLocation = locService.addLocation(loc);
                System.out.printf("The client with authentication token %s" +
                        " has successfully added a new location with the values", decryptedToken);
                System.out.println(savedLocation);
                return new ResponseEntity<>(savedLocation, HttpStatus.CREATED);
            } else {
                System.out.printf("A client has attempted to add a new location to the client with the id %d using the " +
                        "token: %s which is not the token associated with that client.%n", clientId, decryptedToken);
                return new ResponseEntity<>("The auth token does not match the client id " +
                        "that you are attempting to create a location for!", HttpStatus.FORBIDDEN);
            }
        } catch (InvalidTypeException | InvaildInputException
                | InvalidKeySpecException | BadPaddingException
                | InvalidKeyException | IllegalBlockSizeException e)  {
            return new ResponseEntity<>(e.getMessage(),
             HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (NotFoundException | NoSuchAlgorithmException
                | NoSuchPaddingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    /**
     * A method to get all locations in the database for a specific client.
     * @param clientId
     *      The id of the client who's locations are being retrieved
     * @param clientAuthToken
     *      The authentication token provided to retrieve all the client's locations
     * @return
     *      The response from finding all the locations in the database.
     */
    @CrossOrigin()
    @GetMapping(path = "/getAll")
    public ResponseEntity<?> getLocations(@RequestParam final Integer clientId,
                                          @RequestParam final String clientAuthToken) {
        try {
            //verify that the auth token provided by the client is not blank
            clientServ.checkAuthTokenBlank(clientAuthToken);

            //verify that there is a client with the provided client id in the database
            clientServ.getClientById(clientId);

            //Decrypt the provided token
            String decryptedToken = clientServ.decryptToken(clientAuthToken);

            //get the client that the auth token belongs to
            Client authClient = clientServ.getClientByAuth(decryptedToken);

            //make sure that the client id the auth token is associated to matches the one passed in the request
            //if they match proceed to retrieve a list of locations that belong to the specified client id
            //if they do not match, throw an error
            if (clientId.equals(authClient.getId())) {
                //Return all the locations with the specified client id
                Iterable<Location> ownedLocs = locRepository.getAllByClientId(clientId);
                System.out.printf("The client with authentication token %s" +
                        " has successfully retrieved all their location from the database", decryptedToken);
                System.out.println(ownedLocs);
                return new ResponseEntity<>(ownedLocs, HttpStatus.CREATED);
            } else {
                System.out.printf("A client has attempted to retrieve all the locations of the client" +
                        " client with the id %d using the " +
                        "token: %s which is not the token associated with that client.%n", clientId, decryptedToken);
                return new ResponseEntity<>("The auth token does not match the client id " +
                        "that you are attempting retrieve the location of!", HttpStatus.FORBIDDEN);
            }
        } catch (InvaildInputException | InvalidKeySpecException
                | BadPaddingException | InvalidKeyException
                | IllegalBlockSizeException | IllegalArgumentException e)  {
            return new ResponseEntity<>(e.getMessage(),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (NotFoundException | NoSuchAlgorithmException
                | NoSuchPaddingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    /**
     * A method to get all locations in the database by
     * which area they are located in.
     * @param area
     *      The area where the desired locations are
     * @return
     *      The response from finding all locations in the given area
     */
    @GetMapping(path = "/get/{area}")
    public ResponseEntity<?> getLocByArea(@PathVariable final String area) {
        return new ResponseEntity<>(locRepository.findByArea(area),
         HttpStatus.OK);
    }

    /**
     * A method to get all locations by claimed status.
     * @param isClaim
     *      The claimed status on which to search the database
     * @return
     *      The response from finding all locations by claimed
     *      status or the response from catching an exception
     */
    @CrossOrigin()
    @GetMapping(path = "/getClaim/{isClaim}")
    public ResponseEntity<?> getLocByClaim(@PathVariable final String isClaim) {
        try {
            List<Location> searchResults =
            locService.getLocationByClaim(isClaim);
            return new ResponseEntity<>(searchResults, HttpStatus.OK);
        } catch (InvaildInputException e) {
            return new ResponseEntity<>(e.getMessage(),
             HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * A method to update the cost of an already existing location.
     * @param locId
     *      The id of the location in need of changing
     * @param locCost
     *      The new cost of the location
     * @param clientId
     *      The id of the client assosiated with the location.
     *      For authentication usage.
     * @return
     *      The response from updating the cost or the response
     *      from the location not existing
     */
    @PostMapping(path = "/updateCost")
    public ResponseEntity<?> updateLocCost(@RequestParam final Integer locId,
                                @RequestParam final Double locCost,
                                @RequestParam final Integer clientId) {
        //Get the location based on the id provided
        try {
            //Check to see if the location is in the DB and get it's data
            Location targetLoc = locService.getLocById(locId);
            //To Do: Add a client authentication method in service

            locService.checkInvalid(targetLoc);

            //Update the location's data in the
            //database given the provided information by the user
            Location updatedLoc = locService.updateLocCost(targetLoc, locCost);
            return new ResponseEntity<>(updatedLoc, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidTypeException e)  {
            return new ResponseEntity<>(e.getMessage(),
            HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e)  {
            return new ResponseEntity<>(e.getMessage(),
            HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    /**
     * A method to update the claimed status of an existing location.
     * @param locId
     *      The id of the location in need of updating
     * @param locClaim
     *      The new claimed status of the location
     * @param clientId
     *      The id of the client assosiated with the location.
     *      For authentication usage.
     * @return
     *      The response from updating the claimed status or the
     *      response from the location not existing
     */
    @PostMapping(path = "/updateClaim")
    public ResponseEntity<?> updateLocClaim(@RequestParam final Integer locId,
                                @RequestParam final Boolean locClaim,
                                @RequestParam final Integer clientId) {
        try {
            //Check to see if the location is in the DB and get it's data
            Location targetLoc = locService.getLocById(locId);
            //To Do: Add a client authentication method in service
            locService.checkInvalid(targetLoc);

            //Update the location's data in the
            //database given the provided information by the user
            Location updatedLoc =
             locService.updateLocClaim(targetLoc, locClaim);
            return new ResponseEntity<>(updatedLoc, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidTypeException e)  {
            return new ResponseEntity<>(e.getMessage(),
            HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e)  {
            return new ResponseEntity<>(e.getMessage(),
            HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * A method to delete an existing location.
     * @param locId
     *      The id of the location to be deleted
     * @param clientId
     *      The id of the client assosiated with the location.
     *      For authentication usage.
     * @return
     *      The response from successfully deleting the location or
     *      the response from the location not existing
     */
    @PostMapping(path = "/delete")
    public ResponseEntity<?> deleteLoc(@RequestParam final Integer locId,
                                       @RequestParam final Integer clientId) {
        try {
            //To Do: Add a client authentication method in service
            //Delete the location from the database
            locService.deleteLocationById(locId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
