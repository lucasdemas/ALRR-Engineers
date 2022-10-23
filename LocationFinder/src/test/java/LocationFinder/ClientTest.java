package LocationFinder;

import static org.junit.jupiter.api.Assertions.*;

import LocationFinder.exceptions.InvaildInputException;
import LocationFinder.models.Client;
import LocationFinder.repositories.ClientRepository;
import LocationFinder.services.ClientService;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import LocationFinder.exceptions.NotFoundException;
import java.util.Optional;




@RunWith(SpringRunner.class)
@SpringBootTest
class ClientTest {
    @Autowired
    private ClientService clientServ;

    @MockBean
    private ClientRepository clientRepo;

//Figure out catch exception junit 5

    //Test for attempting to delete a client who does not exist (no client with the specified client id)
    @Test
    public void testDeleteClient() {
        assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                //Have the mock repo say that there is no client with the id 100
                Mockito.when(clientRepo.existsById(100)).thenReturn(false);

                //Try to delete the client with id 100 and get the thrown error
                clientServ.deleteClientById(100);
            }
        });
    }

    //Test to successfully add a client to the database
    @Test
    public void testAddClient() {

        Client client1 = new Client(null, "Client Test", "ClientTest@client.com");

        Client client2 = new Client(100, "Client Test", "ClientTest@client.com");

        Mockito.when(clientRepo.save(client1)).thenReturn(client2);
        assertEquals(clientServ.addClient(client1).getId(), 100);
    }

    //Test to successfully get a specific client based on the client id
    @Test
    public void testGetClientById() throws NotFoundException {


        Client client1 = new Client(100, "Client Test", "ClientTest@client.com");
        Optional<Client> optClient = Optional.of(client1);
        Mockito.when(clientRepo.findById(100)).thenReturn(optClient);

        Client clientResult = clientServ.getClientById(100);

        assertEquals(clientResult.getId(), 100);
        assertEquals(clientResult.getName(), "Client Test");
        assertEquals(clientResult.getEmail(), "ClientTest@client.com");
    }

    //Test to get a client id that does not exist in the database
    @Test
    public void testGetClientByIdException() {
        assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                //Tell the mock repo that there is no client with id 100
                Mockito.when(clientRepo.existsById(100)).thenReturn(false);

                //Try and get a client with the id 100 (which results in a NotFound exception)
                clientServ.getClientById(100);
            }
        });
    }

    //Test to successfully update a client's email
    @Test
    public void testUpdateClientEmail() throws NotFoundException {


        Client client1 = new Client(100, "Client Test", "ClientTest@client.com");

        //Optional<Client> optClient = Optional.of(client1);
        //Mockito.when(clientRepo.findById(100)).thenReturn(optClient);

        Optional<Client> optClient = Optional.of(client1);
        Mockito.when(clientRepo.findById(100)).thenReturn(optClient);

        Mockito.when(clientRepo.existsById(100)).thenReturn(true);

        //Mockito.when(clientServ.getClientById(100)).thenReturn(client1);
        Mockito.when(clientRepo.save(client1)).thenReturn(client1);

        Client clientResult = clientServ.updateClientEmail(100,"UpdatedClientTest@client.com");

        assertEquals(clientResult.getEmail(), "UpdatedClientTest@client.com");
    }

    //Test to throw exception when an email is in a invailid format
    @Test
    public void invalidEmailException() {
        assertThrows(InvaildInputException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                clientServ.checkEmail("@invlaidformat.com");
            }
        });
    }




    // Testing to see if we can add a new location
//    @Test
//    public void addNewLocTest() {
//        Location loc = new Location("Mudd Building", "NYC", 30.50);
//        // Mockito.when(locRepo.save(loc)).thenReturn(loc);
//        // System.out.println(loc.getName());
//        String result = locController.addNewLoc("Mudd Building", "NYC", 30.50);
//        assertEquals("Saved", result);
//    }
}


