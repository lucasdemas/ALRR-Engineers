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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
     *
     * @param e The Number Format Exception that is going to be handled
     * @return The response for inputting a negative value for cost
     */
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<?> handleInvalidNumber(
            final NumberFormatException e) {

        return new ResponseEntity<>("Cost Must be a positive numeric value",
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Exception handling for InvalidNumberException.
     *
     * @param e The Exception that is handled.
     * @return The response for the exception being handled.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleInvalidNumber(
            final IllegalArgumentException e) {

        return new ResponseEntity<>("The Authentication token must be a valid token!",
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * A method that adds a new location to the database.
     *
     * @param locName         The location name
     * @param locArea         The location area
     * @param locCost         The location cost
     * @param clientId        The location client id
     * @param clientAuthToken The authentication token of the client attempting to add a new location
     * @return The response for a successfully created location
     * or the response for a caught exception
     */
    @CrossOrigin()
    @PostMapping(path = "/add")
    public ResponseEntity<?> addNewLoc(@RequestParam final String locName,
                                       @RequestParam final String locArea,
                                       @RequestParam final Double locCost,
                                       @RequestParam final Integer clientId,
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
            //if they match proceed to check the other inputs to see if they are all valid and add the new location
            //if they do not match, throw an error
            if (clientId.equals(authClient.getId())) {
                //Convert the user input into a location entity
                Location loc = new Location(locName, locArea, locCost, clientId);

                //Check that all of the data the user input is in a valid format
                //Possibly add checker for if the user
                //inputs a string of spaces (which is invalid)
                locService.checkInvalid(loc);

                //Add the new location to the database
                Location savedLocation = locService.addLocation(loc);
                System.out.printf("The client with authentication token %s" +
                                " has successfully added a new location with the values: locName: %s," +
                                " locArea: %s, locCost: %f, clientId: %d%n", decryptedToken, locName, locArea,
                        locCost, clientId);
                return new ResponseEntity<>(savedLocation, HttpStatus.CREATED);
            } else {
                System.out.printf("A client has attempted to add a new location to the client with the id %d using the " +
                        "token: %s which is not the token associated with that client.%n", clientId, decryptedToken);
                return new ResponseEntity<>("The auth token does not match the client id " +
                        "that you are attempting to create a location for!", HttpStatus.FORBIDDEN);
            }
        } catch (InvalidTypeException | InvaildInputException
                | InvalidKeySpecException | BadPaddingException
                | InvalidKeyException | IllegalBlockSizeException
                | IllegalArgumentException e) {
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
     *
     * @param clientId        The id of the client who's locations are being retrieved
     * @param clientAuthToken The authentication token provided to retrieve all the client's locations
     * @return The response from finding all the locations in the database.
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
                System.out.printf("The client with id %d and authentication token %s" +
                        " has successfully retrieved all their location from the database.%n",
                         clientId, decryptedToken);
                return new ResponseEntity<>(ownedLocs, HttpStatus.OK);
            } else {
                System.out.printf("A client has attempted to retrieve all the locations of the client" +
                        " with the id %d using the " +
                        "token: %s which is not the token associated with that client.%n", clientId, decryptedToken);
                return new ResponseEntity<>("The auth token does not match the client id " +
                        "that you are attempting retrieve the location of!", HttpStatus.FORBIDDEN);
            }
        } catch (InvaildInputException | InvalidKeySpecException
                | BadPaddingException | InvalidKeyException
                | IllegalBlockSizeException | IllegalArgumentException e) {
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
     *
     * @param area            The area where the desired locations are
     * @param clientId        The id of the client who's filtering their locations by a specific area
     * @param clientAuthToken The authentication token provided to verify the client is who they say they are
     * @return The response from finding all locations in the given area
     */
    @CrossOrigin()
    @GetMapping(path = "/getArea")
    public ResponseEntity<?> getLocByArea(@RequestParam final String area,
                                          @RequestParam final Integer clientId,
                                          @RequestParam final String clientAuthToken) {
        try {
            //verify that the area is not blank
            locService.checkArea(area);

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
                List<Location> areaLocs = locRepository.findByArea(area, clientId);
                System.out.printf("The client with authentication token %s" +
                        " has successfully retrieved all their locations from the area: %s" +
                        " from the database.%n", decryptedToken, area);
                return new ResponseEntity<>(areaLocs, HttpStatus.OK);
            } else {
                System.out.printf("A client has attempted to retrieve all the locations of the client" +
                                " in the area %s, client with the id %d using the " +
                                "token: %s which is not the token associated with that client.%n", clientId,
                        area, decryptedToken);
                return new ResponseEntity<>("The auth token does not match the client id " +
                        "that you are attempting retrieve the location of!", HttpStatus.FORBIDDEN);
            }

        } catch (InvaildInputException | InvalidKeySpecException
                | BadPaddingException | InvalidKeyException
                | IllegalBlockSizeException | IllegalArgumentException
                | InvalidTypeException e) {
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
     * A method to get all locations by claimed status.
     *
     * @param isClaim The claimed status on which to search the database
     * @return The response from finding all locations by claimed
     * status or the response from catching an exception
     */
    @CrossOrigin()
    @GetMapping(path = "/getClaim")
    public ResponseEntity<?> getLocByClaim(@RequestParam final String isClaim,
                                           @RequestParam final Integer clientId,
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
                //Get a list of the locations that are either claimed or unclaimed specified by the input
                List<Location> searchResults =
                        locService.getLocationByClaim(isClaim, clientId);
                System.out.printf("The client with client id %d and authentication token %s" +
                        " has successfully retrieved all their %s locations" +
                        " from the database.%n", clientId, decryptedToken, isClaim);
                return new ResponseEntity<>(searchResults, HttpStatus.OK);
            } else {
                System.out.printf("A client has attempted to retrieve all the %s locations of the client" +
                                " with the id %d using the " +
                                "token: %s which is not the token associated with that client.%n", isClaim,
                        clientId, decryptedToken);
                return new ResponseEntity<>("The auth token does not match the client id " +
                        "that you are attempting retrieve the location of!", HttpStatus.FORBIDDEN);
            }
        } catch (InvaildInputException | NotFoundException
                | BadPaddingException | InvalidKeyException
                | IllegalBlockSizeException | InvalidKeySpecException
                | IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    /**
     * A method to update the cost of an already existing location.
     *
     * @param locId           The id of the location in need of changing
     * @param locCost         The new cost of the location
     * @param clientId        The id of the client assosiated with the location.
     *                        For authentication usage.
     * @param clientAuthToken The authentication token provided to verify the client making the request
     * @return The response from updating the cost or the response
     * from the location not existing
     */
    @CrossOrigin()
    @PostMapping(path = "/updateCost")
    public ResponseEntity<?> updateLocCost(@RequestParam final Integer locId,
                                           @RequestParam final Double locCost,
                                           @RequestParam final Integer clientId,
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

            //check that the cost specified is a positive number
            locService.checkCost(locCost);

            //make sure that the client id the auth token is associated to matches the one passed in the request
            //if they match proceed to retrieve a list of locations that belong to the specified client id
            //if they do not match, throw an error
            if (clientId.equals(authClient.getId())) {
                //Check to see if the location is in the DB and get it's data
                Location targetLoc = locService.getLocById(locId);

                //check to make sure the client id provided matches the client id that the location belongs to
                if (clientId.equals(targetLoc.getClientId())) {
                    //Update the location's data in the
                    //database given the provided information by the user
                    Location updatedLoc = locService.updateLocCost(targetLoc, locCost);
                    System.out.printf("The client with authentication token %s" +
                            " has successfully updated the location with the location id: %d" +
                            " to the new cost of %f in the database.%n", decryptedToken, locId, locCost);
                    return new ResponseEntity<>(updatedLoc, HttpStatus.OK);
                } else {
                    System.out.printf("A client with the client id: %d " +
                                    "has attempted to update the cost of the location with the " +
                                    " location id %d to the cost of %f " +
                                    "which does not belong to them.%n", clientId,
                            locId, locCost);
                    return new ResponseEntity<>("The location who's cost you are trying to update " +
                            "does not belong to the client information provided!", HttpStatus.FORBIDDEN);
                }
            } else {
                System.out.printf("A client has attempted to to update the cost of the location with the" +
                                " location id %d to the cost of %f belong to the client with the " +
                                "client id %d with the authentication  " +
                                "token: %s which is not the token associated with that client.%n", locId,
                        locCost, clientId, decryptedToken);
                return new ResponseEntity<>("The auth token does not match the client id " +
                        "that you are attempting to update the cost of a location!", HttpStatus.FORBIDDEN);
            }
        } catch (NotFoundException | NoSuchAlgorithmException
                | NoSuchPaddingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidTypeException | InvalidKeySpecException
                | BadPaddingException | InvalidKeyException
                | InvaildInputException | IllegalBlockSizeException |
                IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    /**
     * A method to update the claimed status of an existing location.
     *
     * @param locId           The id of the location in need of updating
     * @param locClaim        The new claimed status of the location
     * @param clientId        The id of the client assosiated with the location.
     *                        For authentication usage.
     * @param clientAuthToken The authentication token provided to verify the post request
     * @return The response from updating the claimed status or the
     * response from the location not existing
     */
    @CrossOrigin()
    @PostMapping(path = "/updateClaim")
    public ResponseEntity<?> updateLocClaim(@RequestParam final Integer locId,
                                            @RequestParam final Boolean locClaim,
                                            @RequestParam final Integer clientId,
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
                //Check to see if the location is in the DB and get it's data
                Location targetLoc = locService.getLocById(locId);

                //check to make sure the client id provided matches the client id that the location belongs to
                if (clientId.equals(targetLoc.getClientId())) {
                    //Update the location's data in the
                    //database given the provided information by the user
                    Location updatedLoc = locService.updateLocClaim(targetLoc, locClaim);

                    if (locClaim) {
                        System.out.printf("The client with authentication token %s" +
                                " has successfully updated the claimed status of the " +
                                "location with the location id: %d" +
                                " to claimed in the database.%n", decryptedToken, locId);
                    } else {
                        System.out.printf("The client with authentication token %s" +
                                " has successfully updated the claimed status of the " +
                                "location with the location id: %d" +
                                " to unclaimed in the database.%n", decryptedToken, locId);
                    }
                    return new ResponseEntity<>(updatedLoc, HttpStatus.OK);
                } else {
                    if (locClaim) {
                        System.out.printf("A client with the client id: %d " +
                                        "has attempted to update the claim status of the location with the " +
                                        " location id %d to claimed but the location" +
                                        " does not belong to them.%n", clientId,
                                locId);
                    } else {
                        System.out.printf("A client with the client id: %d " +
                                        "has attempted to update the claim status of the location with the " +
                                        " location id %d to unclaimed but the location" +
                                        " does not belong to them.%n", clientId,
                                locId);
                    }
                    return new ResponseEntity<>("The location who's cost you are trying to update " +
                            "does not belong to the client information provided!", HttpStatus.FORBIDDEN);
                }
            } else {
                if (locClaim) {
                    System.out.printf("A client has attempted to to update claim status of the location with the" +
                                    " location id %d to claimed belonging to the client with the " +
                                    "client id %d with the authentication  " +
                                    "token: %s which is not the token associated with that client.%n", locId,
                            clientId, decryptedToken);
                } else {
                    System.out.printf("A client has attempted to to update claim status of the location with the" +
                                    " location id %d to unclaimed belonging to the client with the " +
                                    "client id %d with the authentication  " +
                                    "token: %s which is not the token associated with that client.%n", locId,
                            clientId, decryptedToken);
                }
                return new ResponseEntity<>("The auth token does not match the client id " +
                        "that you are attempting to update the cost of a location!", HttpStatus.FORBIDDEN);
            }
        } catch (NotFoundException | NoSuchAlgorithmException
                | NoSuchPaddingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidTypeException | InvalidKeySpecException
                | BadPaddingException | InvalidKeyException
                | InvaildInputException | IllegalBlockSizeException |
                IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    /**
     * A method to delete an existing location.
     *
     * @param locId    The id of the location to be deleted
     * @param clientId The id of the client associated with the location.
     *                 For authentication usage.
     * @param clientAuthToken
     *      The authentication token provided to the entrypoint to perform the action
     * @return The response from successfully deleting the location or
     * the response from the location not existing
     */
    @CrossOrigin()
    @PostMapping(path = "/delete")
    public ResponseEntity<?> deleteLoc(@RequestParam final Integer locId,
                                       @RequestParam final Integer clientId,
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
                //Check to see if the location is in the DB and get it's data
                Location targetLoc = locService.getLocById(locId);

                //check to make sure the client id provided matches the client id that the location belongs to
                if (clientId.equals(targetLoc.getClientId())) {
                    //Delete the location from the database
                    locService.deleteLocationById(locId);
                    System.out.printf("The client with authentication token %s" +
                            " has successfully deleted the  " +
                            "location with the location id: %d" +
                            " in the database.%n", decryptedToken, locId);
                    return new ResponseEntity<>("Location deleted successfully!", HttpStatus.OK);
                } else {
                    System.out.printf("A client with the client id: %d " +
                                    "has attempted to delete the location with the " +
                                    " location id %d but the location" +
                                    " does not belong to them.%n", clientId,
                            locId);
                    return new ResponseEntity<>("You cannot delete a location that does not belong to you!"
                            , HttpStatus.FORBIDDEN);
                }
            } else {
                System.out.printf("A client has attempted to delete the location with the" +
                                " location id %d using the " +
                                "client id %d with the authentication  " +
                                "token: %s which is not the token associated with that client.%n", locId,
                        clientId, decryptedToken);
                return new ResponseEntity<>("You cannot delete a location that you do not " +
                        "have the credentials for!", HttpStatus.FORBIDDEN);
            }
        } catch (NotFoundException | NoSuchAlgorithmException
                | NoSuchPaddingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidKeySpecException | BadPaddingException
                | InvalidKeyException | InvaildInputException
                | IllegalBlockSizeException | IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}
