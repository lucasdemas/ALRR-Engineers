package LocationFinder.services;


import LocationFinder.exceptions.EntityExistsException;
import LocationFinder.exceptions.NotFoundException;
import LocationFinder.exceptions.InvaildInputException;
import LocationFinder.models.Client;
import LocationFinder.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.charset.StandardCharsets;

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
     *      The client that will be added to the database
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
     *      The id of the client who want's to update their email
     * @param newEmail
     *      The email the client wants to update their current email to
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
     * A method to retrieve the client's information based on the provided email if they are in the database
     * @param clientEmail
     *      The email of the client trying to access their data
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
     *      The email provided, used to check if a client already has that email
     * @throws EntityExistsException
     *      A client exists in the database with the provided email already
     */
    public void checkEmailNew(String clientEmail) throws EntityExistsException {
        if(clientRepo.existsByEmail(clientEmail)){
            throw new EntityExistsException("There is already a client with that email!");
        }
    }

    public void checkPass(String clientPass) throws InvaildInputException {
        if (clientPass.trim().isEmpty()) {
            throw new InvaildInputException("The password cannot be blank!");
        }
    }

    public Client getClientByAuth(String clientAuthToken) throws NotFoundException {
        Optional<Client> target = clientRepo.findByAuthToken(clientAuthToken);
        if (target.isPresent()) {
            return target.get();
        } else {
            throw new NotFoundException("There is no client with that authentication token!");
        }
    }

    public void checkAuthTokenExists(String clientAuthToken) throws EntityExistsException {
        Optional<Client> target = clientRepo.findByAuthToken(clientAuthToken);
        if (target.isPresent()) {
            throw new EntityExistsException("There is already a client with that auth token!");
        }
    }

    public void checkValidRole(String clientRole) throws InvaildInputException {
        if (clientRole.trim().isEmpty()) {
            throw new InvaildInputException("The new client's role cannot be blank!");
        }
        else if (!(clientRole.trim().equals("ADMIN") || (clientRole.trim().equals("USER")))) {
            throw new InvaildInputException("The new client's role needs to be ADMIN or USER!");
        }
    }

    public String decryptToken(String userAuthToken) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        File privateKeyFile = new File("private.key");
        byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        String decryptedMessage = new String(decryptCipher.doFinal(Base64.getDecoder().decode(userAuthToken)), StandardCharsets.UTF_8);

        System.out.println(decryptedMessage);

        return decryptedMessage;
    }

    public void checkAuthTokenBlank(String clientAuthToken) throws InvaildInputException {
        if (clientAuthToken.trim().isEmpty()) {
            throw new InvaildInputException("The authentication token cannot be blank!");
        }
    }
}
