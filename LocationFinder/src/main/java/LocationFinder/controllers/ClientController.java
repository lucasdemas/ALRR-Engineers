package LocationFinder.controllers;

import LocationFinder.exceptions.InvaildInputException;
import LocationFinder.exceptions.NotFoundException;
import LocationFinder.repositories.ClientRepository;
import LocationFinder.models.Client;
import LocationFinder.repositories.LocationRepository;
import LocationFinder.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


@RestController
@RequestMapping(path = "/client")
public class ClientController {
    /**
     * An instance of the client repository.
     */
    @Autowired
    private ClientRepository clientRepo;

    /**
     * An instance of the client repository.
     */
    @Autowired
    private LocationRepository locRepo;

    /**
     * An instance of the client service.
     */
    @Autowired
    private ClientService clientServ;

    /**
     * Exception Handler for Number Format Exception.
     * @param e
     * @return
     *      The response for a number format exception
     */
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<?> handleInvalidNumber(
            final NumberFormatException e) {

        return new ResponseEntity<>("Client id must be integer",
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * A method to authenticate the client based on their api token.
     * @param clientAuthToken
     *      The authentication token of the client trying to use our API's services (encrypted using the public key)
     * @return
     *      The client's id
     */
    @CrossOrigin()
    @GetMapping(path = "/authenticate")
    public ResponseEntity<?> authenticateClient(@RequestParam final String clientAuthToken) {
        try {
            //verify that the auth token for the new client is not blank
            clientServ.checkAuthTokenBlank(clientAuthToken);

            String decryptedToken = clientServ.decryptToken(clientAuthToken);

            //check to see if there is any client with the decrypted version
            // of the provided authentication token
            Client fetchedClient = clientServ.getClientByAuth(decryptedToken);

            //Print to terminal that there is an authentication occurring with the provided
            //authentication token
            System.out.println("A client has authenticated with the authentication token: " +
                    decryptedToken);

            //return the client's id that matches the authentication token
            return new ResponseEntity<>(fetchedClient.getId(), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidKeySpecException | BadPaddingException
                | IllegalBlockSizeException | InvalidKeyException
                | InvaildInputException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * A method to get all clients.
     * @return
     *      The response from getting all clients
     */
    @GetMapping(path = "/getAll")
    public ResponseEntity<?> getClients() {
        return new ResponseEntity<>(clientRepo.findAll(), HttpStatus.OK);
    }

    /**
     * A method to get a client instance by the client id.
     * @param id
     *      The id of the client
     * @return
     *      The response for successfully getting the client or the
     *      response for the client not existing
     */
    @GetMapping(path = "/get/{id}")
    public ResponseEntity<?> getClientById(
        @PathVariable final Integer id) throws NumberFormatException {
        //Search for the client in the client table
        //based on the provided id (if there is a client with that id)
        try {
            Client targetClient = clientServ.getClientById(id);
            return new ResponseEntity<>(targetClient, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
