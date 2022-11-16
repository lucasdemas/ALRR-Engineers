package LocationFinder.services;


import LocationFinder.exceptions.NotFoundException;
import LocationFinder.exceptions.InvaildInputException;
import LocationFinder.models.Client;
import LocationFinder.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ClientService {
    /**
     * An instance of the client repository.
     */
    @Autowired
    private ClientRepository clientRepo;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * A method to add a client to the database.
     * @param client
     * @return
     *      The client that was saved
     */
    public Client addClient(final Client client) {
        //client.setPassword(bCryptPasswordEncoder.encode(client.getPassword()));
        Client fullClient = clientRepo.save(client);
        return fullClient;
    }

    /**
     * A method to get a client from the database by a given id.
     * @param clientId
     * @return
     *      The client
     * @throws NotFoundException
     *      The given id does not exist in the database
     */
    public Client getClientById(final Integer clientId)
     throws NotFoundException {
        Optional<Client> target = clientRepo.findById(clientId);
        if (target.isPresent()) {
            Client clientResult = target.get();
            return clientResult;
        } else {
            throw new NotFoundException("There is no client with that id");
        }

    }

    /**
     * A method to update the client's email given their id.
     * @param clientId
     * @param newEmail
     * @return
     *      The updated client
     * @throws NotFoundException
     *      The client does not exist in the database
     */
    public Client updateClientEmail(final Integer clientId,
     final String newEmail) throws NotFoundException {

        if (clientRepo.existsById(clientId)) {
        //have a checker to see if the new email is valid,
        //if not throw and exception

            Client updatedClient = getClientById(clientId);
            updatedClient.setEmail(newEmail);
            clientRepo.save(updatedClient);

            return updatedClient;
        } else {
            throw new NotFoundException("There is no client with that id");
        }
    }

    /**
     * A method to check for valid client name.
     * @param client
     * @throws InvaildInputException
     *      The client name is blank
     */
    public void checkInvalid(final Client client) throws InvaildInputException {
        if (client.getName().trim().isEmpty()) {
            throw new InvaildInputException("Client name cannot be blank");
        }
    }

    /**
     * A method to check if a given email is valid.
     * @param email
     * @throws InvaildInputException
     *      The email is not a valid format
     */
    public void checkEmail(final String email)
    throws InvaildInputException {
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPat = Pattern.compile(emailRegex,
         Pattern.CASE_INSENSITIVE);
        Matcher emailMatcher = emailPat.matcher(email);
        if (!emailMatcher.find()) {
            throw new
            InvaildInputException("Please enter a valid email format");
        }
    }

    /**
     * A method to delete a client by their id.
     * @param id
     * @throws NotFoundException
     *      A client does not exist in the database for the given id
     */
    public void deleteClientById(final Integer id) throws NotFoundException {
        if (clientRepo.existsById(id)) {

        clientRepo.deleteById(id);
        } else {
            throw new NotFoundException("There is no client with that id");
        }
    }

}
