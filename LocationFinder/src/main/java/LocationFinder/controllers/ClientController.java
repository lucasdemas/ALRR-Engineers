package LocationFinder.controllers;

import LocationFinder.exceptions.InvaildInputException;
import LocationFinder.exceptions.NotFoundException;
import LocationFinder.repositories.ClientRepository;
import LocationFinder.models.Client;
import LocationFinder.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
     * An instance of the client service.
     */
    @Autowired
    private ClientService clientServ;

    /**
     * Exception handling for MissingServletRequestParameter.
     *
     * @param e The MissingServletRequestParameter Exception that is going to be handled
     * @return The response saying what input is missing in the request
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingInputs(
            final MissingServletRequestParameterException e) {

        return new ResponseEntity<>(e.getMessage(),
                HttpStatus.BAD_REQUEST);
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
                | InvaildInputException | IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
