package LocationFinder.services;


import LocationFinder.exceptions.EntityExistsException;
import LocationFinder.exceptions.NotFoundException;
import LocationFinder.exceptions.InvaildInputException;
import LocationFinder.models.Client;
import LocationFinder.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class ClientService {
    /**
     * An instance of the client repository.
     */
    @Autowired
    private ClientRepository clientRepo;

    /**
     * A method to add a client to the database.
     * @param client
     * @return
     *      The client that was saved
     */
    public Client addClient(final Client client) {
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

    /**
     * A method to encrypt the password of a new client using SHA-256
     * @param clientPassword
     * @throws NoSuchAlgorithmException
     *      The service does not have the encryption algorithm specified
     */
    public String encryptPass(String clientPassword) throws NoSuchAlgorithmException {
        //get the message digest to for SHA-256 to begin hashing password
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        //hash the password with the message digest to get the byte array of the hash
        byte[] shaPass = md.digest(clientPassword.getBytes(StandardCharsets.UTF_8));

        //convert the byte array into a single number representation
        BigInteger shaPassNum = new BigInteger(1, shaPass);

        //convert the single number version of the message digest into a hex value
        StringBuilder shaPassHex = new StringBuilder(shaPassNum.toString(16));

        //pad the hex value of the password with leading 0's if not 64 characters long
        while (shaPassHex.length() < 64)
        {
            shaPassHex.insert(0, '0');
        }

        //convert the hex version of the sha-256 encrypted password to a string and return it
        return shaPassHex.toString();
    }

    /**
     * A method to retrieve the client's information based on the provided email if they are in the database
     * @param clientEmail
     * @throws NotFoundException
     *      A client does not exist in the database for the given email
     */
    public Client getClientByEmail(String clientEmail) throws NotFoundException {
        Optional<Client> target = clientRepo.findByEmail(clientEmail);
        if (target.isPresent()) {
            Client clientResult = target.get();
            return clientResult;
        } else {
            throw new NotFoundException("There is no client with that email");
        }
    }

    /**
     * A method to check if there is already a client in the database with the provided email
     * @param clientEmail
     * @throws EntityExistsException
     *      A client exists in the database with the provided email already
     */
    public void checkEmailNew(String clientEmail) throws EntityExistsException {
        //Optional<Client> target = clientRepo.findByEmail(clientEmail);
        //if (target.isPresent()) {

        //I have changed thev if statment here for building the test case.
        //It still offer the same functionality.
        if(clientRepo.existsByEmail(clientEmail)){
            throw new EntityExistsException("There is already a client with that email!");
        }
    }

    public void checkPass(String clientPass) throws InvaildInputException {
        if (clientPass.trim().isEmpty()) {
            throw new InvaildInputException("The password cannot be blank!");
        }
    }
}
