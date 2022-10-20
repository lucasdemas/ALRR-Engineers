package LocationFinder.services;

import LocationFinder.exceptions.InvalidTypeException;
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

    public Client addClient(Client client) {
        clientRepo.save(client);
        return client;
    }

    public void checkInvalid(Client client) throws InvalidTypeException {
        if (client.getName().trim().isEmpty()){
            throw new InvalidTypeException("Client name cannot be blank");
        }
    }

}
