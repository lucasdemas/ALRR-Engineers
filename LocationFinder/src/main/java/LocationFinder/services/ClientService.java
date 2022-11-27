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
import java.nio.charset.StandardCharsets;

@Service
public class ClientService {
    /**
     * An instance of the client repository.
     */
    @Autowired
    private ClientRepository clientRepo;

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

    public Client getClientByAuth(String clientAuthToken) throws NotFoundException {
        Optional<Client> target = clientRepo.findByAuthToken(clientAuthToken);
        if (target.isPresent()) {
            return target.get();
        } else {
            throw new NotFoundException("There is no client with that authentication token!");
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

        return decryptedMessage;
    }

    public String checkAuthTokenBlank(String clientAuthToken) throws InvaildInputException {
        if (clientAuthToken.trim().isEmpty()) {
            throw new InvaildInputException("The authentication token cannot be blank!");
        }
        return clientAuthToken;
    }
}
