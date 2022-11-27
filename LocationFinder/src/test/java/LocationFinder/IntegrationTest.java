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

import LocationFinder.models.Location;
import LocationFinder.repositories.LocationRepository;
import LocationFinder.services.LocationService;
import LocationFinder.controllers.LocationController;

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
public class IntegrationTest {

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
     * An instance of the location service.
     */
    @Autowired
    private LocationService locServ;


    /**
     * An instance of the location service.
     */
    @MockBean
    private LocationRepository locRepo;

@Test
    public void clientAuthTest() throws Exception {


    Client client1 = new Client(1, "Client Test", "ClientTest@client.com", "1234");

    Optional<Client> optClient = Optional.of(client1);

    //Encoded is 1234.
    String encoded = clientServ.decryptToken("D0AyTsyIvQ/syUasc36L+DYeNogP7ShmMKOL0KcepAWRwTk9U+2NZehm9O8AbetLunTovnKYzoNOHKcPdz1tH7qG2qnPIUV7aVorngU1uuZv3Zq8Iq+DyLVyNzIj4Zrvx6Jtjc6BDYm9yWOfTalDnVZkuUneCVz5+wiGjBS91KDECnvDF3qVJ17qedTrqdIcZd1+LDt32O6not/tNnNoOAWv01Esjx38tm7AbV1P4gMV1voWQEQyDAVcdAE5ilwu9Oe+nzNaBbKB2PRlhyk2jevXAPjAmkBdMNh3D4ZPtUUerZmwKr0kLDx6ru4z+uK7Viyl5bDKBJPB14rmdYl0TQ==");


    Mockito.when(clientRepo.findByAuthToken("1234")).thenReturn(optClient);
    Client client2 = clientServ.getClientByAuth(encoded);


    assertEquals(client2.getId(), 1);
    assertEquals(client2.getName(), "Client Test");
    assertEquals(client2.getEmail(), "ClientTest@client.com");
    assertEquals(client2.getAuthToken(), "1234");


}

    @Test
    public void getClientAndUpdateCost() throws Exception {


        Client client1 = new Client(1, "Client Test", "ClientTest@client.com", "1234");
        Optional<Client> optClient = Optional.of(client1);

        Location loc1 = new Location("Soho", "New York", 20.0, 1);
        loc1.setId(1);
        loc1.setClaim(true);

        //Mockito.when(clientServ.checkAuthTokenBlank("1234")).thenReturn("1234");
        Mockito.when(clientRepo.findById(1)).thenReturn(optClient);
        Mockito.when(clientRepo.findByAuthToken("1234")).thenReturn(optClient);

        //Have the client be returned in the format
        //that findById is looking for in the cleintRepo
        Optional<Location> optLocation = Optional.of(loc1);

        //Have the mock return the formatted client
        //when it look for a client with the id 1
        Mockito.when(locRepo.findById(1)).thenReturn(optLocation);

        Location location1 = locServ.getLocById(1);

        Location location2 = locServ.updateLocClaim(location1, false);

        assertEquals(location2.getClaim(), false);



    }


}

