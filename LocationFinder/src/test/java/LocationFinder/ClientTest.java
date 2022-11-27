package LocationFinder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;



import LocationFinder.exceptions.InvaildInputException;
import LocationFinder.models.Client;
import LocationFinder.repositories.ClientRepository;
import LocationFinder.services.ClientService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import LocationFinder.exceptions.NotFoundException;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import javax.crypto.BadPaddingException;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.IOException;


import java.security.InvalidKeyException;


import java.security.spec.InvalidKeySpecException;




@RunWith(SpringRunner.class)
@SpringBootTest
class ClientTest {
    /**
     * An instance of the client service.
     */
    @Autowired
    private ClientService clientServ;

    /**
     * An instance of the client repository.
     */
    @MockBean
    private ClientRepository clientRepo;



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
     * Test case: Decrypting the encrypted token.
     * Expected: Decrypted token
     * equals 1234 (the decryption of
     * the encrpted token).
     */

    @Test
    public void testDecryptToken()
            throws IOException,
            NoSuchAlgorithmException,
            InvalidKeySpecException,
            NoSuchPaddingException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException {


        assertEquals(clientServ
                .decryptToken("D0AyTsyIvQ/syUasc36L+"
                        + "DYeNogP7ShmMKOL0KcepAWRwTk9U+"
                        + "2NZehm9O8AbetLunTovnKYzoNOHK"
                        + "cPdz1tH7qG2qnPIUV7aVorngU1uuZ"
                        + "v3Zq8Iq+DyLVyNzIj4Zrvx6Jtjc6BDY"
                        + "m9yWOfTalDnVZkuUneCVz5+wiGjBS91K"
                        + "DECnvDF3qVJ17qedTrqdIcZd1+LDt32O6"
                        + "not/tNnNoOAWv01Esjx38tm7AbV1P4gMV1"
                        + "voWQEQyDAVcdAE5ilwu9Oe+nzNaBbKB2PRl"
                        + "hyk2jevXAPjAmkBdMNh3D4ZPtUUerZmwKr0"
                        + "kLDx6ru4z+uK7Viyl5bDKBJPB14rmdYl0TQ=="),
                "1234");

    }

    /**
     * Test case: invalid (blank) authentication token.
     * Excepted: @throws InvaildInputException
     */

    @Test
    public void invalidClientAuth() {
        assertThrows(InvaildInputException.class, new Executable() {
            @Override
            public void execute() throws Throwable {

                clientServ.checkAuthTokenBlank("    ");
            }
        });
    }



    /**
     * Test case: valid (none blank) authentication token.
     * Excepted: No excpetion thrown
     */

    @Test
    public void validClientAuth() throws InvaildInputException {

        //Test a valid auth token
        //Expected, no exception thrown or return statments
        clientServ.checkAuthTokenBlank("1234");

    }


    /**
     * Test case: Get client by Auth token.
     * Excepted: Return the client assosiated with
     * Specific auth token.
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



}


