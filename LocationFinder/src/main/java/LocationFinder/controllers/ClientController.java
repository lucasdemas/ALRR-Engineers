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
     * A method to add a new Client to the database.
     * @param clientName
     *      The client name to be added
     * @param clientEmail
     *      The client email to be added
     * @return
     *      The response for a successfully added client or the
     *      response for an invalid input
     */
    @CrossOrigin()
    @PostMapping(path = "/add")
    public ResponseEntity<?> addNewClient(
                                    @RequestParam final String clientName,
                                    @RequestParam final String clientEmail,
                                    @RequestParam final String clientPassword) {
        try {
            //Create a new client and add the data provided by the user
            Client newClient = new Client();
            newClient.setName(clientName);
            newClient.setEmail(clientEmail);
            newClient.setPassword(clientPassword);

            //Check that the inputted data is valid
            clientServ.checkInvalid(newClient);

            //Check that the email provided is a valid email format
            clientServ.checkEmail(clientEmail);

            //If data is valid add new client to table
            Client addedClient = clientServ.addClient(newClient);
            return new ResponseEntity<>(addedClient, HttpStatus.CREATED);
        } catch (InvaildInputException e)  {
            return new ResponseEntity<>(e.getMessage(),
             HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * A method to login a client
     * @param clientEmail
     *      The email of the client trying to login
     */
//    @GetMapping(path = "/login")
//    public ResponseEntity clientLogin(@RequestParam final String clientEmail) {
//
//    }

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

    /**
     * A method to update the email of a client.
     * @param clientId
     *      The id of the client being updated
     * @param clientEmail
     *      The email to be updated
     * @return
     *      The response from successfully updating the email or
     *      the response from the client not existing or the
     *      response from the email being invalid
     */
    @PostMapping(path = "/updateEmail")
    public ResponseEntity<?> updateClientEmail(
                            @RequestParam final Integer clientId,
                            @RequestParam final String clientEmail) {
        try {
            //Check to see if there is a client with the specified id

            //Check to see if the email they provided is valid or not
            clientServ.checkEmail(clientEmail);

            //Update client with email provided
            Client updatedClient =
            clientServ.updateClientEmail(clientId, clientEmail);
            return new ResponseEntity<>(updatedClient, HttpStatus.OK);
        } catch (NotFoundException e) {
            //Catch exception of not finding a client with that id
            return new ResponseEntity<>(e.getMessage(),
             HttpStatus.NOT_FOUND);
        } catch (InvaildInputException e) {
            //Catch exception of client not
            //providing valid a valid email address
            return new ResponseEntity<>(e.getMessage(),
             HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * A method to delete an existing client
     * (and any locations belonging to them in the database).
     * @param clientId
     *      The id of the client being deleted
     * @return
     *      The response for a successful deletion or the response
     *      for the client not existing
     */
    @CrossOrigin("http://127.0.0.1:5000")
    @PostMapping(path = "/delete")
    public ResponseEntity<?> deleteClient(
                            @RequestParam final Integer clientId) {
        //find the client with the given id
        try {
            //Delete the client from the repository with the given id
            clientServ.deleteClientById(clientId);

            //Delete all the locations belonging to the client (if they had any)
            locRepo.deleteClientLocs(clientId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
