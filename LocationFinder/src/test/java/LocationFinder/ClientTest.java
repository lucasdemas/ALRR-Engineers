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
import LocationFinder.exceptions.EntityExistsException;
import java.util.Optional;


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
     * A negative test for deleting a client.
     */
    @Test
    public void testDeleteClientException() {
        assertThrows(NotFoundException.class,
         new Executable() {
            @Override
            public void execute() throws Throwable {
                //Have the mock repo say that
                //there is no client with the id 100
                Mockito.when(clientRepo.existsById(1)).thenReturn(false);

                //Try to delete the client with id
                //100 and get the thrown error
                clientServ.deleteClientById(1);
            }
        });
    }

    /**
     * A test to succesfully add a client to the database.
     */
    @Test
    public void testAddClient() {
        //Create a mock client that will represent
        //the data that would be passed in for when a new client is added
        Client client1 = new Client(null,
        "Client Test", "ClientTest@client.com");

        //Create a second mock client to represent that
        //the mock client will be given the client id 100 when added to the repo
        Client client2 = new Client(1, "Client Test",
        "ClientTest@client.com");

        //Have the mock return the second mock client
        //when it will save the first one to the client repo
        Mockito.when(clientRepo.save(client1)).thenReturn(client2);

        //Check to see that the fist mock client's id
        //was updated to 100 after being added to the client repo
        assertEquals(clientServ.addClient(client1).getId(), 1);
    }


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
     * A test to successfully update a client's email.
     * @throws NotFoundException
     *      The client id does not exist in the database
     */
    @Test
    public void testUpdateClientEmail() throws NotFoundException {
        //Create a mock client who's email we will update
        Client client1 = new Client(1, "Client Test",
        "ClientTest@client.com");

        //Have the client be returned in the format
        //that findById is looking for in the cleintRepo
        Optional<Client> optClient = Optional.of(client1);

        //Have the mock return the formatted client
        //when it look for a client with the id 1
        Mockito.when(clientRepo.findById(1)).thenReturn(optClient);

        //Have the mock return true when it checks
        //to see if there is a client with the id 1 in the repo
        Mockito.when(clientRepo.existsById(1)).thenReturn(true);

        //Save the client to the repository
        Mockito.when(clientRepo.save(client1)).thenReturn(client1);

        //Update the mock client's email
        Client clientResult = clientServ.updateClientEmail(1,
        "UpdatedClientTest@client.com");

        //Check to see that the client's email was updated successfully
        assertEquals(clientResult.getEmail(), "UpdatedClientTest@client.com");
    }

    /**
     * A test to throw exception when an email is in a invailid format.
     */
    @Test
    public void invalidEmailException() {
        assertThrows(InvaildInputException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                clientServ.checkEmail("@invlaidformat.com");
            }
        });
    }

    /**
     * A test to see if the client provided a blank string for their name.
     */
    @Test
    public void invalidClientNameException() {
        assertThrows(InvaildInputException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                //Create a dummy client with a blank string for the client name
                Client dummyClient = new Client(1, "      ",
                 "ClientTest@client.com");

                //Test to see if their input has an invalid name
                clientServ.checkInvalid(dummyClient);

            }
        });
    }
    @Test
    public void testGetClientByEmail() throws NotFoundException {
        //Create a mock client who we will search for by their email
        Client client1 = new Client(1, "Client Test",
                "ClientTest@client.com");

        //Have the client be returned in the format
        //that findByEmail is looking for in the cleintRepo
        Optional<Client> optClient = Optional.of(client1);

        //Have the mock return the formatted client
        //when it look for a client with the email "ClientTest@client.com"
        Mockito.when(clientRepo.findByEmail("ClientTest@client.com")).thenReturn(optClient);

        //Get the result of searching for a client with the email "ClientTest@client.com"
        Client clientResult = clientServ.getClientByEmail("ClientTest@client.com");

        //Check to see that the results of
        //the service returned the correct data
        assertEquals(clientResult.getId(), 1);
        assertEquals(clientResult.getName(), "Client Test");
        assertEquals(clientResult.getEmail(), "ClientTest@client.com");
    }

    @Test
    public void testGetClientByEmailException() {
        assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                //Tell the mock repo that there is no client with email "ClientTest@client.com"
                Mockito.when(clientRepo.existsByEmail("ClientTest@client.com")).thenReturn(false);

                //Try and get a client with the email "ClientTest@client.com"
                //(which results in a NotFound exception)
                clientServ.getClientByEmail("ClientTest@client.com");
            }
        });
    }


}


