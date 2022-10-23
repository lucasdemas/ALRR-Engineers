package LocationFinder;

import static org.junit.jupiter.api.Assertions.*;
import LocationFinder.models.Client;
import LocationFinder.controllers.ClientController;
import LocationFinder.repositories.ClientRepository;
import LocationFinder.services.ClientService;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
//import org.junit.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import LocationFinder.exceptions.NotFoundException;
import org.junit.jupiter.params.ParameterizedTest;
import java.util.Optional;




@RunWith(SpringRunner.class)
@SpringBootTest
class ClientTest {
    @Autowired
    private ClientService clientServ;

    @MockBean
    private ClientRepository clientRepo;

    @Rule
    public ExpectedException exception = ExpectedException.none();
//Figure out catch exception junit 5

    @Test
    public void testDeleteClient() {

        assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Mockito.when(clientRepo.existsById(100)).thenReturn(false);
                clientServ.deleteClientById(100);
            }
        });
    }


    @Test
    public void testAddClient() {

        Client client1 = new Client(null, "Client Test", "ClientTest@client.com");

        Client client2 = new Client(100, "Client Test", "ClientTest@client.com");

        Mockito.when(clientRepo.save(client1)).thenReturn(client2);
        assertEquals(clientServ.addClient(client1).getId(), 100);
    }

    @Test
    public void TestGetClientById() throws NotFoundException {


        Client client1 = new Client(100, "Client Test", "ClientTest@client.com");
        Optional<Client> optClient = Optional.of(client1);
        Mockito.when(clientRepo.findById(100)).thenReturn(optClient);

        Client clientResult = clientServ.getClientById(100);

        assertEquals(clientResult.getId(), 100);
        assertEquals(clientResult.getName(), "Client Test");
        assertEquals(clientResult.getEmail(), "ClientTest@client.com");
    }


    @Test
    public void TestUpdateClientEmail() throws NotFoundException {


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


