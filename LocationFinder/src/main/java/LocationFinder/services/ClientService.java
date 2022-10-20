package LocationFinder.services;

import LocationFinder.exceptions.InvalidTypeException;
import LocationFinder.exceptions.NotFoundException;
import LocationFinder.models.Client;
import LocationFinder.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepo;

    //Function to save client to client repository
    public Client addClient(Client client) {
        clientRepo.save(client);
        return client;
    }

    //Function to get a client by a provided id
    public Client getClientById(Integer client_id) throws NotFoundException {
        Optional<Client> target = clientRepo.findById(client_id);
        if (target.isPresent()) {
            Client clientResult = target.get();
            return clientResult;
        } else {
            throw new NotFoundException("There is no client with that id");
        }
    }

    //Function to handle invalid entries for certain attributes given a clients values
    public void checkInvalid(Client client) throws InvalidTypeException {
        if (client.getName().trim().isEmpty()){
            throw new InvalidTypeException("Client name cannot be blank");
        }
    }

}
