package LocationFinder;

import static org.junit.jupiter.api.Assertions.*;
import LocationFinder.models.Client;
import LocationFinder.controllers.ClientController;
import LocationFinder.repositories.ClientRepository;
import LocationFinder.services.ClientService;
import org.junit.jupiter.api.Test;
//import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import LocationFinder.exceptions.NotFoundException;
import org.junit.jupiter.params.ParameterizedTest;




@RunWith(SpringRunner.class)
@SpringBootTest
class ClientTest {
    @Autowired
    private ClientService clientServ;

    @MockBean
    private ClientRepository clientRepo;
/*
    @Test(expected = NotFoundException.class)
    public void testDeleteClient(ClientTest client) throws NotFoundException{

        Mockito.when(clientRepo.existsById(100)).thenReturn(false);

        clientServ.deleteClientById(100);

    }

 */

    @Test
    public void testAddClient() {

        Client client1 = new Client(null, "Client Test", "ClientTest@client.com");

        Client client2 = new Client(100, "Client Test", "ClientTest@client.com");

        Mockito.when(clientRepo.save(client1)).thenReturn(client2);
        assertEquals(clientServ.addClient(client1).getId(), 100);
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


