package LocationFinder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import LocationFinder.exceptions.InvaildInputException;
import LocationFinder.models.Client;
import LocationFinder.repositories.ClientRepository;
import LocationFinder.services.ClientService;
import LocationFinder.controllers.ClientController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import LocationFinder.exceptions.NotFoundException;
import LocationFinder.exceptions.EntityExistsException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


@RunWith(SpringRunner.class)
@SpringBootTest
class ClientTest {
    /**
     * An instance of the client service.
     */
    @Autowired
    private ClientService clientServ;

    @Autowired
    private ClientController clientCont;

    /**
     * An instance of the client repository.
     */
    @MockBean
    private ClientRepository clientRepo;


    /**
     * Client Unit test cases section, mock services are used
     * Not actual changes to the database
     * Total number of test cases: 7
     */

    /**
     * A test to check if a client does not exist the correct
     * exception is thrown.
     * @throws NotFoundException
     *      The id does not exist in the database
     */

    @Test
    public void testGetClientById() throws NotFoundException {
        //Create a mock client who we will search for by their id
        Client client1 = new Client(1, "Client Test",
        "ClientTest@client.com");

        //Have the client be returned in the format
        //that findById is looking for in the cleintRepo
        Optional<Client> optClient = Optional.of(client1);

        //Have the mock return the formatted client
        //when it look for a client with the id 100
        Mockito.when(clientRepo.findById(1)).thenReturn(optClient);

        //Get the result of searching for a client with the id 1
        Client clientResult = clientServ.getClientById(1);

        //Check to see that the results of
        //the service returned the correct data
        assertEquals(clientResult.getId(), 1);
        assertEquals(clientResult.getName(), "Client Test");
        assertEquals(clientResult.getEmail(), "ClientTest@client.com");
    }

    /**
     * A test to see if the correct exception is thrown when
     * a client is not found in the database.
     */
    @Test
    public void testGetClientByIdException() {
        assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                //Tell the mock repo that there is no client with id 100
                Mockito.when(clientRepo.existsById(1)).thenReturn(false);

                //Try and get a client with the id 1
                //(which results in a NotFound exception)
                clientServ.getClientById(1);
            }
        });
    }



    /**
     * testing decrypt token
     */

    @Test
    public void TestDecryptToken() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        File publicKeyFile = new File("public.key");
        byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());


        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        keyFactory.generatePublic(publicKeySpec);

        String secretToken = "1234";
        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] secretMessageBytes = secretToken.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);

        String encodedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);
        assertEquals(clientServ.decryptToken(encodedMessage), "1234");

    }

    /**
     * test case for invalid (blank) authentication token
     */

    @Test
    public void InvalidClientAuth() {
        assertThrows(InvaildInputException.class, new Executable() {
            @Override
            public void execute() throws Throwable {

                clientServ.checkAuthTokenBlank("    ");
            }
        });
    }


    /**
     * test case for valid (none blank) authentication token
     */

    @Test
    public void ValidClientAuth() throws InvaildInputException {

        //Test a valid auth token
        //Expected, no exception thrown or return statments
        clientServ.checkAuthTokenBlank("1234");

    }


    /**
     * A test to get client by authentication token
     */
    @Test
    public void testGetClientByAuth() throws NotFoundException {
        //Create a mock client who we will search for by their id
        Client client1 = new Client(1, "Client Test",
                "ClientTest@client.com", "1234");

        //Have the client be returned in the format
        //that findById is looking for in the cleintRepo
        Optional<Client> optClient = Optional.of(client1);

        //Have the mock return the formatted client
        //when it look for a client with the auth token 1234
        Mockito.when(clientRepo.findByAuthToken("1234")).thenReturn(optClient);

        //Get the result of searching for a client with the auth token 1234
        Client clientResult = clientServ.getClientByAuth("1234");

        //Check to see that the results of
        //the service returned the correct data
        assertEquals(clientResult.getId(), 1);
        assertEquals(clientResult.getName(), "Client Test");
        assertEquals(clientResult.getEmail(), "ClientTest@client.com");
        assertEquals(clientResult.getAuthToken(), "1234");
    }

    /**
     * A test to see if the correct exception is thrown when
     * a client auth is not found in the database.
     */
    @Test
    public void testGetClientByAuthException() {
        assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                //Tell the mock repo that there is no client with id 100
                Mockito.when(clientRepo.existsByAuth("1234")).thenReturn(false);

                //Try and get a client with the auth token 1234
                //(which results in a NotFound exception)
                clientServ.getClientByAuth("1234");
            }
        });
    }


    /**
     * Client System test cases section, chain test cases
     * method is used, test cases are created with the
     * assumption of already existed clients in the database
     * Total number of test cases: 7
     */

    @Test
    public void TestGetClients() {

        ResponseEntity response = clientCont.getClients();
        System.out.println("Athy");
        System.out.println(response.getBody());

    }


    // These test cases are not used currently but might be useful in the future


    /**
     * A negative test for deleting a client.
     */
//    @Test
//    public void testDeleteClientException() {
//        assertThrows(NotFoundException.class,
//         new Executable() {
//            @Override
//            public void execute() throws Throwable {
//                //Have the mock repo say that
//                //there is no client with the id 100
//                Mockito.when(clientRepo.existsById(1)).thenReturn(false);
//
//                //Try to delete the client with id
//                //100 and get the thrown error
//                clientServ.deleteClientById(1);
//            }
//        });
//    }

    /**
     * A test to succesfully add a client to the database.
     */
//    @Test
//    public void testAddClient() {
//        //Create a mock client that will represent
//        //the data that would be passed in for when a new client is added
//        Client client1 = new Client(null,
//        "Client Test", "ClientTest@client.com");
//
//        //Create a second mock client to represent that
//        //the mock client will be given the client id 100 when added to the repo
//        Client client2 = new Client(1, "Client Test",
//        "ClientTest@client.com");
//
//        //Have the mock return the second mock client
//        //when it will save the first one to the client repo
//        Mockito.when(clientRepo.save(client1)).thenReturn(client2);
//
//        //Check to see that the fist mock client's id
//        //was updated to 100 after being added to the client repo
//        assertEquals(clientServ.addClient(client1).getId(), 1);
//    }


//    /**
//     * A test to successfully update a client's email.
//     * @throws NotFoundException
//     *      The client id does not exist in the database
//     */
//    @Test
//    public void testUpdateClientEmail() throws NotFoundException {
//        //Create a mock client who's email we will update
//        Client client1 = new Client(1, "Client Test",
//        "ClientTest@client.com");
//
//        //Have the client be returned in the format
//        //that findById is looking for in the cleintRepo
//        Optional<Client> optClient = Optional.of(client1);
//
//        //Have the mock return the formatted client
//        //when it look for a client with the id 1
//        Mockito.when(clientRepo.findById(1)).thenReturn(optClient);
//
//        //Have the mock return true when it checks
//        //to see if there is a client with the id 1 in the repo
//        Mockito.when(clientRepo.existsById(1)).thenReturn(true);
//
//        //Save the client to the repository
//        Mockito.when(clientRepo.save(client1)).thenReturn(client1);
//
//        //Update the mock client's email
//        Client clientResult = clientServ.updateClientEmail(1,
//        "UpdatedClientTest@client.com");
//
//        //Check to see that the client's email was updated successfully
//        assertEquals(clientResult.getEmail(), "UpdatedClientTest@client.com");
//    }

//    /**
//     * A test to throw exception when an email is in a invailid format.
//     */
//    @Test
//    public void invalidEmailException() {
//        assertThrows(InvaildInputException.class, new Executable() {
//            @Override
//            public void execute() throws Throwable {
//                clientServ.checkEmail("@invlaidformat.com");
//            }
//        });
//    }

//    /**
//     * A test to see if the client provided a blank string for their name.
//     */
//    @Test
//    public void invalidClientNameException() {
//        assertThrows(InvaildInputException.class, new Executable() {
//            @Override
//            public void execute() throws Throwable {
//                //Create a dummy client with a blank string for the client name
//                Client dummyClient = new Client(1, "      ",
//                 "ClientTest@client.com");
//
//                //Test to see if their input has an invalid name
//                clientServ.checkInvalid(dummyClient);
//
//            }
//        });
//    }

//    /**
//     * A test for getting a client by their email.
//     */
//    @Test
//    public void testGetClientByEmail() throws NotFoundException {
//        //Create a mock client who we will search for by their email
//        Client client1 = new Client(1, "Client Test",
//                "ClientTest@client.com");
//
//        //Have the client be returned in the format
//        //that findByEmail is looking for in the cleintRepo
//        Optional<Client> optClient = Optional.of(client1);
//
//        //Have the mock return the formatted client
//        //when it look for a client with the email "ClientTest@client.com"
//        Mockito.when(clientRepo.findByEmail("ClientTest@client.com")).thenReturn(optClient);
//
//        //Get the result of searching for a client with the email "ClientTest@client.com"
//        Client clientResult = clientServ.getClientByEmail("ClientTest@client.com");
//
//        //Check to see that the results of
//        //the service returned the correct data
//        assertEquals(clientResult.getId(), 1);
//        assertEquals(clientResult.getName(), "Client Test");
//        assertEquals(clientResult.getEmail(), "ClientTest@client.com");
//    }

//    /**
//     * A test for getting a client by their email when email does not exist.
//     * Excpected: NotFoundException exception
//     */
//
//    @Test
//    public void testGetClientByEmailException() {
//        assertThrows(NotFoundException.class, new Executable() {
//            @Override
//            public void execute() throws Throwable {
//                //Tell the mock repo that there is no client with email "ClientTest@client.com"
//                Mockito.when(clientRepo.existsByEmail("ClientTest@client.com")).thenReturn(false);
//
//                //Try and get a client with the email "ClientTest@client.com"
//                //(which results in a NotFound exception)
//                clientServ.getClientByEmail("ClientTest@client.com");
//            }
//        });
//    }

    /**
     * A test for adding a client with an email that already exists.
     * Excpected: EntityExistsException exception
     */

//    @Test
//    public void ClientEmailExistException() {
//        assertThrows(EntityExistsException.class, new Executable() {
//            @Override
//            public void execute() throws Throwable {
//                //Tell the mock repo that there is a client with email "ClientTest@client.com"
//                Mockito.when(clientRepo.existsByEmail("ClientTest@client.com")).thenReturn(true);
//
//                //Check if the client with email "ClientTest@client.com" exists
//                //(which results in a EntityExistsException)
//                clientServ.checkEmailNew("ClientTest@client.com");
//            }
//        });
//    }

    /**
     * A test for adding a client with an email that already exists.
     * Excpected: EntityExistsException exception
     */

//    @Test
//    public void NewEmailTest() throws EntityExistsException {
//
//
//        //Tell the mock repo that there is not client with email "ClientTest@client.com"
//        Mockito.when(clientRepo.existsByEmail("ClientTest@client.com")).thenReturn(false);
//
//        //Check if the client with email "ClientTest@client.com" exists
//        //(which results in no exception of EntityExistsException)
//        clientServ.checkEmailNew("ClientTest@client.com");
//
//
//
//
//
//
//    }
//
//    /**
//     * A test for updating a client email of a client that do not exist.
//     * Excpected: NotFoundException exception
//     */
//
//    @Test
//    public void updateClientEmailException() {
//        assertThrows(NotFoundException.class, new Executable() {
//            @Override
//            public void execute() throws Throwable {
//                //Tell the mock repo that there is no client with id 1
//                Mockito.when(clientRepo.existsById(1)).thenReturn(false);
//
//                //Try to update the email of client with id 1
//                //(which results in a NotFoundException)
//                clientServ.updateClientEmail(1, "ClientTest@client.com");
//            }
//        });
//
//    }

//    /**
//     * A test of an empty string password.
//     * Excpected: InvaildInputException exception
//     */
//
//    @Test
//    public void invalidPasswordException() {
//        assertThrows(InvaildInputException.class, new Executable() {
//            @Override
//            public void execute() throws Throwable {
//
//                //Send an empty string password
//                clientServ.checkPass("      ");
//
//            }
//        });
//    }
//
//
//    /**
//     * A test to check symmetric password functionality.
//     * Excpected: hash(x) == hash(x) (in other word, entering the same password will
//     * return the same hash, no randomization involved).
//     * This will insure the success of login feature.
//     */
//
//    @Test
//    public void testSymmetricPassword() throws NoSuchAlgorithmException  {
//
//        //Create two password with same value
//        String pass1 = clientServ.encryptPass("1234");
//        String pass2 = clientServ.encryptPass("1234");
//
//
//        //Generate the hash of each password
//        String EncPass1 = clientServ.encryptPass(pass1);
//        String EncPass2 = clientServ.encryptPass(pass2);
//
//        //Check that password are equal and hashs are equal
//        assertEquals(pass1, pass2);
//        assertEquals(EncPass1, EncPass2);
//
//    }
}


