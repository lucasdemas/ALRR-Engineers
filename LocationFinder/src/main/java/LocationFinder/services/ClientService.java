package LocationFinder.services;


import com.sun.jdi.InvalidTypeException;
import LocationFinder.exceptions.NotFoundException;
import LocationFinder.models.Client;
import LocationFinder.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepo;

    //Function to save client to client repository
    public Client addClient(Client client) {
        Client fullClient = clientRepo.save(client);
        return fullClient;
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

    //Function to update a specific client's email address
    public Client updateClientEmail(Integer client_id, String new_email) throws NotFoundException{

        if (clientRepo.existsById(client_id)){

        //have a checker to see if the new email is valid, if not throw and exception

            Client updatedClient = getClientById(client_id);
            updatedClient.setEmail(new_email);
            clientRepo.save(updatedClient);

            return updatedClient;
    }
    else {
            throw new NotFoundException("There is no client with that id");
        }
    }

    //Function to handle invalid entries for certain attributes given a clients values
    public void checkInvalid(Client client) throws InvalidTypeException {
        if (client.getName().trim().isEmpty()){
            throw new InvalidTypeException("Client name cannot be blank");
        }
    }

    //Function that will check if the email format provided when adding a new client or updating an existing client's email is the correct format
    public void checkEmail(String email) throws InvalidTypeException{
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPat = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher emailMatcher = emailPat.matcher(email);
        if(!emailMatcher.find()) {
            throw new InvalidTypeException("Please enter a valid email format");
        }
    }

    public void deleteClientById(Integer id) throws NotFoundException{
        if (clientRepo.existsById(id)){

        clientRepo.deleteById(id);}

        else {
            throw new NotFoundException("There is no client with that id");
        }
    }

}
