package LocationFinder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.mockito.Mockito;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;

import LocationFinder.repositories.LocationRepository;
import LocationFinder.services.LocationService;
import LocationFinder.controllers.LocationController;
import LocationFinder.models.Location;

import LocationFinder.repositories.ClientRepository;
import LocationFinder.services.ClientService;
import LocationFinder.controllers.ClientController;
import LocationFinder.models.Client;

import LocationFinder.exceptions.NotFoundException;
import LocationFinder.exceptions.InvaildInputException;

import com.sun.jdi.InvalidTypeException;

import java.util.List;
import java.util.LinkedList;



@WebMvcTest(LocationController.class)
public class LocationSystemTest {
    @Autowired
    MockMvc mockMvc;

    /**
     * An instance of the client service.
     */
    @MockBean
    private LocationService locServ;

    /**
     * An instance of the client service.
     */
    @MockBean
    private ClientService clientServ;


    /**
     * An instance of the client repository.
     */
    @MockBean
    private LocationRepository locRepo;


    @Test
    void locationAddSuccess() throws Exception {

        Client client = new Client(1, "client 1", "client1@test.com", "1234");




        Mockito.when(clientServ.checkAuthTokenBlank("1234")).thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234")).thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234")).thenReturn(client);


        mockMvc.perform(post("/location/add")
                        .param("locName", "Times Square")
                        .param("locArea", "New York")
                        .param("locCost", "1.0")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

    }

    @Test
    void locationAddInvalidCost() throws Exception {

        Client client = new Client(1, "client 1", "client1@test.com", "1234");


        Mockito.when(clientServ.checkAuthTokenBlank("1234")).thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234")).thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234")).thenReturn(client);


        mockMvc.perform(post("/location/add")
                        .param("locName", "Times Square")
                        .param("locArea", "New York")
                        .param("locCost", "ab")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    void locationAddMissingInput() throws Exception {

        Client client = new Client(1, "client 1", "client1@test.com", "1234");


        Mockito.when(clientServ.checkAuthTokenBlank("1234")).thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234")).thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234")).thenReturn(client);


        mockMvc.perform(post("/location/add")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void locationAddInvalidToken() throws Exception {



        Mockito.when(clientServ.checkAuthTokenBlank("")).thenThrow(InvaildInputException.class);


        mockMvc.perform(post("/location/add")
                        .param("locName", "Times Square")
                        .param("locArea", "New York")
                        .param("locCost", "1.0")
                        .param("clientId", "1")
                        .param("clientAuthToken", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    void locationAddUnauthToken() throws Exception {

        Client client = new Client(1, "client 1", "client1@test.com", "1234");
        Client client2 = new Client(2, "client 2", "client2@test.com", "4321");

        Mockito.when(clientServ.checkAuthTokenBlank("1234")).thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234")).thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234")).thenReturn(client);

        Mockito.when(clientServ.checkAuthTokenBlank("4321")).thenReturn("4321");
        Mockito.when(clientServ.decryptToken("4321")).thenReturn("4321");
        Mockito.when(clientServ.getClientByAuth("4321")).thenReturn(client2);





        mockMvc.perform(post("/location/add")
                        .param("locName", "Times Square")
                        .param("locArea", "New York")
                        .param("locCost", "1.0")
                        .param("clientId", "2")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    void locationAddInvalidClientId() throws Exception {



        Mockito.when(clientServ.checkAuthTokenBlank("1234")).thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234")).thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234")).thenThrow(NotFoundException.class);



        mockMvc.perform(post("/location/add")
                        .param("locName", "Times Square")
                        .param("locArea", "New York")
                        .param("locCost", "1.0")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }


    @Test
    void locationGetAllSuccess() throws Exception {


        Client client = new Client(1, "client 1", "client1@test.com", "1234");

        Mockito.when(clientServ.checkAuthTokenBlank("1234")).thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234")).thenReturn("1234");
        Mockito.when(clientServ.getClientById(1)).thenReturn(client);
        Mockito.when(clientServ.getClientByAuth("1234")).thenReturn(client);


        mockMvc.perform(get("/location/getAll")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void locationGetAllInvalidClientId() throws Exception {


        Client client = new Client(1, "client 1", "client1@test.com", "1234");

        Mockito.when(clientServ.checkAuthTokenBlank("1234")).thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234")).thenReturn("1234");
        Mockito.when(clientServ.getClientById(2)).thenThrow(NotFoundException.class);
        Mockito.when(clientServ.getClientByAuth("1234")).thenReturn(client);


        mockMvc.perform(get("/location/getAll")
                        .param("clientId", "2")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void locationGetAllUnauthToken() throws Exception {

        Client client = new Client(1, "client 1", "client1@test.com", "1234");
        Client client2 = new Client(2, "client 2", "client2@test.com", "4321");

        Mockito.when(clientServ.checkAuthTokenBlank("1234")).thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234")).thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234")).thenReturn(client);

        Mockito.when(clientServ.checkAuthTokenBlank("4321")).thenReturn("4321");
        Mockito.when(clientServ.decryptToken("4321")).thenReturn("4321");
        Mockito.when(clientServ.getClientByAuth("4321")).thenReturn(client2);


        mockMvc.perform(get("/location/getAll")
                        .param("clientId", "2")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    void locationGetAllInvalidToken() throws Exception {



        Mockito.when(clientServ.checkAuthTokenBlank("")).thenThrow(InvaildInputException.class);


        mockMvc.perform(get("/location/getAll")
                        .param("clientId", "1")
                        .param("clientAuthToken", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());

    }


    @Test
    void locationGetAreaSuccess() throws Exception {


        Mockito.when(locServ.checkArea("New York")).thenReturn("New York");

        Client client = new Client(1, "client 1", "client1@test.com", "1234");

        Mockito.when(clientServ.checkAuthTokenBlank("1234")).thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234")).thenReturn("1234");
        Mockito.when(clientServ.getClientById(1)).thenReturn(client);
        Mockito.when(clientServ.getClientByAuth("1234")).thenReturn(client);


        mockMvc.perform(get("/location/getArea")
                        .param("area", "New York")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void locationGetAreaInvalidArea() throws Exception {


        Mockito.when(locServ.checkArea("")).thenThrow(InvalidTypeException.class);

        Client client = new Client(1, "client 1", "client1@test.com", "1234");

        Mockito.when(clientServ.checkAuthTokenBlank("1234")).thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234")).thenReturn("1234");
        Mockito.when(clientServ.getClientById(1)).thenReturn(client);
        Mockito.when(clientServ.getClientByAuth("1234")).thenReturn(client);


        mockMvc.perform(get("/location/getArea")
                        .param("area", "")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    void locationGetAreaInvalidClientId() throws Exception {


        Mockito.when(locServ.checkArea("New York")).thenReturn("New York");


        Mockito.when(clientServ.getClientById(1)).thenThrow(NotFoundException.class);


        mockMvc.perform(get("/location/getArea")
                        .param("area", "New York")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }


    @Test
    void locationGetClaimSuccess() throws Exception {
        List<Location> searchResults = new LinkedList<Location>();

        Client client = new Client(1, "client 1", "client1@test.com", "1234");

        Mockito.when(clientServ.checkAuthTokenBlank("1234")).thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234")).thenReturn("1234");
        Mockito.when(clientServ.getClientById(1)).thenReturn(client);
        Mockito.when(clientServ.getClientByAuth("1234")).thenReturn(client);

        Mockito.when(locServ.getLocationByClaim("claimed", 1)).thenReturn(searchResults);

        mockMvc.perform(get("/location/getClaim")
                        .param("isClaim", "claimed")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void locationGetClaimInvalid() throws Exception {


        Mockito.when(locServ.getLocationByClaim("Invalid", 1)).thenThrow(InvaildInputException.class);

        Client client = new Client(1, "client 1", "client1@test.com", "1234");

        Mockito.when(clientServ.checkAuthTokenBlank("1234")).thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234")).thenReturn("1234");
        Mockito.when(clientServ.getClientById(1)).thenReturn(client);
        Mockito.when(clientServ.getClientByAuth("1234")).thenReturn(client);


        mockMvc.perform(get("/location/getClaim")
                        .param("isClaim", "Invalid")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    void locationGetClaimInvalidClientId() throws Exception {

        List<Location> searchResults = new LinkedList<Location>();
        Mockito.when(locServ.getLocationByClaim("claimed", 1)).thenReturn(searchResults);


        Mockito.when(clientServ.getClientById(1)).thenThrow(NotFoundException.class);


        mockMvc.perform(get("/location/getClaim")
                        .param("isClaim", "claimed")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void locationGetClaimUnauthToken() throws Exception {



        Client client = new Client(1, "client 1", "client1@test.com", "1234");
        Client client2 = new Client(2, "client 2", "client2@test.com", "4321");


        List<Location> searchResults = new LinkedList<Location>();
        Mockito.when(locServ.getLocationByClaim("claimed", 1)).thenReturn(searchResults);

        Mockito.when(clientServ.checkAuthTokenBlank("1234")).thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234")).thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234")).thenReturn(client);
        Mockito.when(clientServ.getClientById(1)).thenReturn(client);


        Mockito.when(clientServ.checkAuthTokenBlank("4321")).thenReturn("4321");
        Mockito.when(clientServ.decryptToken("4321")).thenReturn("4321");
        Mockito.when(clientServ.getClientByAuth("4321")).thenReturn(client2);
        Mockito.when(clientServ.getClientById(2)).thenReturn(client2);




        mockMvc.perform(get("/location/getClaim")
                        .param("isClaim", "claimed")
                        .param("clientId", "2")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }


//    @Test
//    void locationGetAreaUnauthToken() throws Exception {
//
//
//
//        Client client = new Client(1, "client 1", "client1@test.com", "1234");
//        Client client2 = new Client(2, "client 2", "client2@test.com", "4321");
//
//
//        Mockito.when(locServ.checkArea("New York")).thenReturn("New York");
//
//        Mockito.when(clientServ.checkAuthTokenBlank("1234")).thenReturn("1234");
//        Mockito.when(clientServ.decryptToken("1234")).thenReturn("1234");
//        Mockito.when(clientServ.getClientByAuth("1234")).thenReturn(client);
//        Mockito.when(clientServ.getClientById(1)).thenReturn(client);
//
//
//        Mockito.when(clientServ.checkAuthTokenBlank("4321")).thenReturn("4321");
//        Mockito.when(clientServ.decryptToken("4321")).thenReturn("4321");
//        Mockito.when(clientServ.getClientByAuth("4321")).thenReturn(client2);
//        Mockito.when(clientServ.getClientById(2)).thenReturn(client2);
//
//
//
//        Mockito.when(locServ.checkArea("New York")).thenReturn("New York");
//
//
//        mockMvc.perform(get("/location/getArea")
//                        .param("area", "New York")
//                        .param("clientId", "2")
//                        .param("clientAuthToken", "1234")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isForbidden());
//
//    }



}
