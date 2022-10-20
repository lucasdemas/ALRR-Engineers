package LocationFinder.controllers;

import LocationFinder.exceptions.InvalidTypeException;
import LocationFinder.exceptions.NotFoundException;
import LocationFinder.repositories.ClientRepository;
import LocationFinder.models.Client;
import LocationFinder.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController // This means that this class is a Controller
@RequestMapping(path="/client")
public class ClientController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private ClientRepository clientRepo;

    @Autowired
    private ClientService clientServ;

    //Call to add a new client to the database
    @PostMapping(path="/add")
    public String addNewClient(@RequestParam String client_name,
                            @RequestParam String client_email) {
        try {
            //Create a new client and add the data provided by the user
            Client newClient = new Client();
            newClient.setName(client_name);
            newClient.setEmail(client_email);
            //Check that the inputted data is valid
            clientServ.checkInvalid(newClient);

            //If data is valid add new client to table
            clientServ.addClient(newClient);
            return "Saved";
        }
        catch (InvalidTypeException e)  {
            return e.getMessage();
        }
    }

    //Call to get all of the clients from our database table
    @GetMapping(path="/getAll")
    Iterable<Client> getClients() {
        return clientRepo.findAll();
    }

    //Call to get a specific client by their id
    @GetMapping(path="/get/{id}")
    String getClientById(@PathVariable Integer id) {
        //Search for the client in the client table based on the provided id (if there is a client with that id)
        try {
            Client targetClient = clientServ.getClientById(id);
            return "Client id: " + targetClient.getId().toString() +
                    "\nClient Name:  " + targetClient.getName() +
                    "\nClient Email:  " + targetClient.getEmail();
        }
        catch(NotFoundException e) {
            return e.getMessage();
        }
    }

    //Call to update the email of a client
    @PostMapping(path="/updateEmail")
    String updateClientEmail(@RequestParam Integer client_id,
                             @RequestParam String client_email) {
        try {
            //Check to see if there is a client with the specified id
            Client targetClient = clientServ.getClientById(client_id);

            //Update client with email provided
            Client updatedClient = clientServ.updateClientEmail(targetClient, client_email);
            return "Client email updated successfully";
        }
        //Catch exception of not finding a client with that id
        catch (NotFoundException e) {
            return e.getMessage();
        }
        //Catch exception of client not providing valid a valid email address
    }
}
