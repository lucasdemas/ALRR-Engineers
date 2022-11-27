package LocationFinder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;




import org.mockito.Mockito;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



import LocationFinder.repositories.LocationRepository;
import LocationFinder.services.LocationService;
import LocationFinder.controllers.LocationController;
import LocationFinder.models.Location;


import LocationFinder.services.ClientService;

import LocationFinder.models.Client;

import LocationFinder.exceptions.NotFoundException;
import LocationFinder.exceptions.InvaildInputException;

import com.sun.jdi.InvalidTypeException;

import java.util.List;
import java.util.LinkedList;



@WebMvcTest(LocationController.class)
public class LocationSystemTest {

    /**
     * An instance of the mockMvc.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * An instance of the location service.
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

    /**
     * Constant number for Location cost testing.
     */
    private static final double COST_NUM = 100.0;

    /**
     * Test case: Success Location Add.
     * Expected Result: HTTP Status Ok.
     */

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


    /**
     * Test case: Invalid Cost on Add Location.
     * Expected Result: HTTP Status Unprocessable Entity
     */
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

    /**
     * Test case: Missing param on Add Location.
     * Expected Result: HTTP Status Bad Request
     */
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


    /**
     * Test case: Blank Token on Add Location.
     * Expected Result: HTTP Status Unprocessable Entity.
     */
    @Test
    void locationAddInvalidToken() throws Exception {



        Mockito.when(clientServ.checkAuthTokenBlank(""))
                .thenThrow(InvaildInputException.class);


        mockMvc.perform(post("/location/add")
                        .param("locName", "Times Square")
                        .param("locArea", "New York")
                        .param("locCost", "1.0")
                        .param("clientId", "1")
                        .param("clientAuthToken", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());

    }


    /**
     * Test case: UnAuth Token on Add Location.
     * Expected Result: HTTP Status Forbidden.
     */
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

    /**
     * Test case: Invalid client id on Add Location.
     * Expected Result: HTTP Status Not found.
     */
    @Test
    void locationAddInvalidClientId() throws Exception {



        Mockito.when(clientServ.checkAuthTokenBlank("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234"))
                .thenThrow(NotFoundException.class);



        mockMvc.perform(post("/location/add")
                        .param("locName", "Times Square")
                        .param("locArea", "New York")
                        .param("locCost", "1.0")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }


    /**
     * Test case: Success get All locations of client.
     * Expected Result: HTTP Status Ok.
     */

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

    /**
     * Test case: Invalid client id
     * on get All locations of client.
     * Expected Result: HTTP Status Not found.
     */

    @Test
    void locationGetAllInvalidClientId() throws Exception {


        Client client = new Client(1, "client 1", "client1@test.com", "1234");

        Mockito.when(clientServ.checkAuthTokenBlank("1234")).thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234")).thenReturn("1234");
        Mockito.when(clientServ.getClientById(2))
                .thenThrow(NotFoundException.class);
        Mockito.when(clientServ.getClientByAuth("1234")).thenReturn(client);


        mockMvc.perform(get("/location/getAll")
                        .param("clientId", "2")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    /**
     * Test case: Unauth token
     * on get All locations of client.
     * Expected Result: HTTP Status Forbidden.
     */
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

    /**
     * Test case: Blank token
     * on get All locations of client.
     * Expected Result: HTTP Status Unprocessable Entity.
     */

    @Test
    void locationGetAllInvalidToken() throws Exception {



        Mockito.when(clientServ.checkAuthTokenBlank(""))
                .thenThrow(InvaildInputException.class);


        mockMvc.perform(get("/location/getAll")
                        .param("clientId", "1")
                        .param("clientAuthToken", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());

    }

    /**
     * Test case: Success get location by area.
     * Expected Result: HTTP Status Ok.
     */

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


    /**
     * Test case: Invalid area get location by area.
     * Expected Result: HTTP Status Unprocessable Entity.
     */

    @Test
    void locationGetAreaInvalidArea() throws Exception {


        Mockito.when(locServ.checkArea(""))
                .thenThrow(InvalidTypeException.class);

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

    /**
     * Test case: Invalid client id get location by area.
     * Expected Result: HTTP Status Not found.
     */
    @Test
    void locationGetAreaInvalidClientId() throws Exception {


        Mockito.when(locServ.checkArea("New York")).thenReturn("New York");


        Mockito.when(clientServ.getClientById(1))
                .thenThrow(NotFoundException.class);


        mockMvc.perform(get("/location/getArea")
                        .param("area", "New York")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    /**
     * Test case: Success get location by claim.
     * Expected Result: HTTP Status Ok.
     */

    @Test
    void locationGetClaimSuccess() throws Exception {
        List<Location> searchResults = new LinkedList<Location>();

        Client client = new Client(1, "client 1", "client1@test.com", "1234");

        Mockito.when(clientServ.checkAuthTokenBlank("1234")).thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234")).thenReturn("1234");
        Mockito.when(clientServ.getClientById(1)).thenReturn(client);
        Mockito.when(clientServ.getClientByAuth("1234")).thenReturn(client);

        Mockito.when(locServ.getLocationByClaim("claimed", 1))
                .thenReturn(searchResults);

        mockMvc.perform(get("/location/getClaim")
                        .param("isClaim", "claimed")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    /**
     * Test case: Invalid claim on get location by claim.
     * Expected Result: HTTP Status Unprocessable Entity.
     */
    @Test
    void locationGetClaimInvalid() throws Exception {


        Mockito.when(locServ.getLocationByClaim("Invalid", 1))
                .thenThrow(InvaildInputException.class);

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

    /**
     * Test case: Invalid client id on get location by claim.
     * Expected Result: HTTP Status Not Found.
     */

    @Test
    void locationGetClaimInvalidClientId() throws Exception {

        List<Location> searchResults = new LinkedList<Location>();
        Mockito.when(locServ.getLocationByClaim("claimed", 1))
                .thenReturn(searchResults);


        Mockito.when(clientServ.getClientById(1))
                .thenThrow(NotFoundException.class);


        mockMvc.perform(get("/location/getClaim")
                        .param("isClaim", "claimed")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    /**
     * Test case: Unauth token on get location by claim.
     * Expected Result: HTTP Status Forbidden.
     */

    @Test
    void locationGetClaimUnauthToken() throws Exception {



        Client client = new Client(1, "client 1", "client1@test.com", "1234");
        Client client2 = new Client(2, "client 2", "client2@test.com", "4321");


        List<Location> searchResults = new LinkedList<Location>();
        Mockito.when(locServ.getLocationByClaim("claimed", 1))
                .thenReturn(searchResults);

        Mockito.when(clientServ.checkAuthTokenBlank("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234"))
                .thenReturn(client);
        Mockito.when(clientServ.getClientById(1))
                .thenReturn(client);


        Mockito.when(clientServ.checkAuthTokenBlank("4321"))
                .thenReturn("4321");
        Mockito.when(clientServ.decryptToken("4321"))
                .thenReturn("4321");
        Mockito.when(clientServ.getClientByAuth("4321"))
                .thenReturn(client2);
        Mockito.when(clientServ.getClientById(2))
                .thenReturn(client2);




        mockMvc.perform(get("/location/getClaim")
                        .param("isClaim", "claimed")
                        .param("clientId", "2")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }


    /**
     * Test case: Success delete location.
     * Expected Result: HTTP Status Ok.
     */

    @Test
    void locationDeleteSuccess() throws Exception {

        Client client = new Client(1, "client 1", "client1@test.com", "1234");
        Location location = new Location("Soho", "New York", COST_NUM, 1);
        location.setId(1);


        Mockito.when(clientServ.checkAuthTokenBlank("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234"))
                .thenReturn(client);
        Mockito.when(locServ.getLocById(1))
                .thenReturn(location);


        mockMvc.perform(post("/location/delete")
                        .param("locId", "1")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }


    /**
     * Test case: Missing param on delete location.
     * Expected Result: HTTP Status Bad Request.
     */

    @Test
    void locationDeleteMissingInput() throws Exception {

        Client client = new Client(1, "client 1", "client1@test.com", "1234");
        Location location = new Location("Soho", "New York", COST_NUM, 1);
        location.setId(1);

        Mockito.when(locServ.getLocById(1)).thenReturn(location);

        Mockito.when(clientServ.checkAuthTokenBlank("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234"))
                .thenReturn(client);


        mockMvc.perform(post("/location/delete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }


    /**
     * Test case: Blank token on delete location.
     * Expected Result: HTTP Status Unprocessable Entity.
     */
    @Test
    void locationDeleteInvalidToken() throws Exception {



        Mockito.when(clientServ.checkAuthTokenBlank(""))
                .thenThrow(InvaildInputException.class);


        mockMvc.perform(post("/location/delete")
                        .param("locId", "1")
                        .param("clientId", "1")
                        .param("clientAuthToken", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());

    }

    /**
     * Test case: Unauth Client Id on delete location.
     * Expected Result: HTTP Status Forbidden.
     */
    @Test
    void locationDeleteUnauthToken() throws Exception {

        Client client = new Client(1, "client 1", "client1@test.com", "1234");
        Client client2 = new Client(2, "client 2", "client2@test.com", "4321");

        Location location = new Location("Soho", "New York", COST_NUM, 1);
        location.setId(1);

        Mockito.when(locServ.getLocById(1))
                .thenReturn(location);

        Mockito.when(clientServ.checkAuthTokenBlank("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234"))
                .thenReturn(client);

        Mockito.when(clientServ.checkAuthTokenBlank("4321"))
                .thenReturn("4321");
        Mockito.when(clientServ.decryptToken("4321"))
                .thenReturn("4321");
        Mockito.when(clientServ.getClientByAuth("4321"))
                .thenReturn(client2);





        mockMvc.perform(post("/location/delete")
                        .param("locId", "1")
                        .param("clientId", "2")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }


    /**
     * Test case: Invalid client id on delete location.
     * Expected Result: HTTP Status Not found.
     */
    @Test
    void locationDeleteInvalidClientId() throws Exception {



        Location location = new Location("Soho", "New York", COST_NUM, 1);
        location.setId(1);

        Mockito.when(locServ.getLocById(1)).thenReturn(location);

        Mockito.when(clientServ.checkAuthTokenBlank("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234"))
                .thenThrow(NotFoundException.class);



        mockMvc.perform(post("/location/delete")
                        .param("locId", "1")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }


    /**
     * Test case: UnAuth token on delete location.
     * Expected Result: HTTP Status Forbidden.
     */

    @Test
    void locationDeleteLocUnauth() throws Exception {

        Client client = new Client(1, "client 1", "client1@test.com", "1234");
        Client client2 = new Client(2, "client 2", "client2@test.com", "4321");

        Location location = new Location("Soho", "New York", COST_NUM, 2);
        location.setId(1);

        Mockito.when(locServ.getLocById(1)).thenReturn(location);

        Mockito.when(clientServ.checkAuthTokenBlank("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234"))
                .thenReturn(client);

        Mockito.when(clientServ.checkAuthTokenBlank("4321"))
                .thenReturn("4321");
        Mockito.when(clientServ.decryptToken("4321"))
                .thenReturn("4321");
        Mockito.when(clientServ.getClientByAuth("4321"))
                .thenReturn(client2);



        mockMvc.perform(post("/location/delete")
                        .param("locId", "1")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }


    /**
     * Test case: Success on Update Location Cost.
     * Expected Result: HTTP Status Ok.
     */
    @Test
    void locationUpdateCostSuccess() throws Exception {

        Client client = new Client(1, "client 1", "client1@test.com", "1234");

        Location location = new Location("Soho", "New York", COST_NUM, 1);
        location.setId(1);

        Mockito.when(locServ.getLocById(1)).thenReturn(location);


        Mockito.when(clientServ.checkAuthTokenBlank("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234"))
                .thenReturn(client);


        mockMvc.perform(post("/location/updateCost")
                        .param("locId", "1")
                        .param("locCost", "1.0")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();


    }

    /**
     * Test case: Invalid cost on Update Location Cost.
     * Expected Result: HTTP Status Unprocessable Entity.
     */

    @Test
    void locationUpdateInvalidCost() throws Exception {

        Client client = new Client(1, "client 1", "client1@test.com", "1234");


        Mockito.when(clientServ.checkAuthTokenBlank("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234"))
                .thenReturn(client);

        Location location = new Location("Soho", "New York", COST_NUM, 1);
        location.setId(1);

        Mockito.when(locServ.getLocById(1))
                .thenReturn(location);
        Mockito.when(locServ.checkCost(-1))
                .thenThrow(InvalidTypeException.class);



        mockMvc.perform(post("/location/updateCost")
                        .param("locId", "1")
                        .param("locCost", "-1")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity()).andReturn();

    }

    /**
     * Test case: Invalid loc Id on Update Location Cost.
     * Expected Result: HTTP Status Not found.
     */

    @Test
    void locationUpdateInvalidLocId() throws Exception {


        Client client = new Client(1, "client 1", "client1@test.com", "1234");

        Mockito.when(clientServ.checkAuthTokenBlank("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234"))
                .thenReturn(client);


        Mockito.when(locServ.getLocById(1))
                .thenThrow(NotFoundException.class);



        mockMvc.perform(post("/location/updateCost")
                        .param("locId", "1")
                        .param("locCost", "100")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();

    }

    /**
     * Test case: Invalid client Id on Update Location Cost.
     * Expected Result: HTTP Status Not found.
     */

    @Test
    void locationUpdateInvalidClientId() throws Exception {



        Location location = new Location("Soho", "New York", COST_NUM, 1);
        location.setId(1);
        Mockito.when(locServ.getLocById(1)).thenReturn(location);

        Mockito.when(clientServ.getClientById(1))
                .thenThrow(NotFoundException.class);



        mockMvc.perform(post("/location/updateCost")
                        .param("locId", "1")
                        .param("locCost", "101.1")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();

    }


    /**
     * Test case: UnAuth client Id on Update Location Cost.
     * Expected Result: HTTP Status Forbidden.
     */


        @Test
    void locationUpdateCostUnauthToken() throws Exception {



        Client client = new Client(1, "client 1", "client1@test.com", "1234");
        Client client2 = new Client(2, "client 2", "client2@test.com", "4321");

        Location location = new Location("Soho", "New York", COST_NUM, 1);
        location.setId(1);
        Mockito.when(locServ.getLocById(1))
                .thenReturn(location);


        Mockito.when(clientServ.checkAuthTokenBlank("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234"))
                .thenReturn(client);
        Mockito.when(clientServ.getClientById(1))
                .thenReturn(client);


        Mockito.when(clientServ.checkAuthTokenBlank("4321"))
                .thenReturn("4321");
        Mockito.when(clientServ.decryptToken("4321"))
                .thenReturn("4321");
        Mockito.when(clientServ.getClientByAuth("4321"))
                .thenReturn(client2);
        Mockito.when(clientServ.getClientById(2))
                .thenReturn(client2);



        mockMvc.perform(post("/location/updateCost")
                        .param("locId", "1")
                        .param("locCost", "101.1")
                        .param("clientId", "2")
                        .param("clientAuthToken", "4321")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    /**
     * Test case: UnAuth token on Update Location Cost.
     * Expected Result: HTTP Status Forbidden.
     */

    @Test
    void locationUpdateCostInvalidToken() throws Exception {



        Client client = new Client(1, "client 1", "client1@test.com", "1234");
        Client client2 = new Client(2, "client 2", "client2@test.com", "4321");

        Location location = new Location("Soho", "New York", COST_NUM, 1);
        location.setId(1);
        Mockito.when(locServ.getLocById(1)).thenReturn(location);


        Mockito.when(clientServ.checkAuthTokenBlank("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234"))
                .thenReturn(client);
        Mockito.when(clientServ.getClientById(1))
                .thenReturn(client);

        Mockito.when(clientServ.checkAuthTokenBlank("4321"))
                .thenReturn("4321");
        Mockito.when(clientServ.decryptToken("4321"))
                .thenReturn("4321");
        Mockito.when(clientServ.getClientByAuth("4321"))
                .thenReturn(client2);
        Mockito.when(clientServ.getClientById(2))
                .thenReturn(client2);



        mockMvc.perform(post("/location/updateCost")
                        .param("locId", "1")
                        .param("locCost", "101.0")
                        .param("clientId", "1")
                        .param("clientAuthToken", "4321")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }


    /**
     * Test case: Success Update Claim.
     * Expected Result: HTTP Status Ok.
     */

    @Test
    void locationUpdateClaimSuccess() throws Exception {

        Client client = new Client(1, "client 1", "client1@test.com", "1234");

        Location location = new Location("Soho", "New York", COST_NUM, 1);
        location.setId(1);
        location.setClaim(true);

        Mockito.when(locServ.getLocById(1)).thenReturn(location);


        Mockito.when(clientServ.checkAuthTokenBlank("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234"))
                .thenReturn(client);


        mockMvc.perform(post("/location/updateClaim")
                        .param("locId", "1")
                        .param("locClaim", "false")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();


    }

    /**
     * Test case: Invalid claim on Update Claim.
     * Expected Result: HTTP Status Unprocessable Entity.
     */

    @Test
    void locationUpdateInvalidClaim() throws Exception {

        Client client = new Client(1, "client 1", "client1@test.com", "1234");


        Mockito.when(clientServ.checkAuthTokenBlank("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234"))
                .thenReturn(client);

        Location location = new Location("Soho", "New York", COST_NUM, 1);
        location.setId(1);

        Mockito.when(locServ.getLocById(1)).thenReturn(location);
        Mockito.when(locServ.updateLocClaim(location, true))
                .thenThrow(InvalidTypeException.class);



        mockMvc.perform(post("/location/updateClaim")
                        .param("locId", "1")
                        .param("locClaim", "true")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity()).andReturn();

    }

    /**
     * Test case: Invalid loc Id on Update Claim.
     * Expected Result: HTTP Status Not Found.
     */

    @Test
    void locationUpdateClaimInvalidLocId() throws Exception {


        Client client = new Client(1, "client 1", "client1@test.com", "1234");

        Mockito.when(clientServ.checkAuthTokenBlank("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234"))
                .thenReturn(client);


        Mockito.when(locServ.getLocById(1))
                .thenThrow(NotFoundException.class);



        mockMvc.perform(post("/location/updateClaim")
                        .param("locId", "1")
                        .param("locClaim", "true")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();

    }

    /**
     * Test case: Invalid client Id on Update Claim.
     * Expected Result: HTTP Status Not Found.
     */
    @Test
    void locationUpdateClaimInvalidClientId() throws Exception {



        Location location = new Location("Soho", "New York", COST_NUM, 1);
        location.setId(1);
        Mockito.when(locServ.getLocById(1)).thenReturn(location);

        Mockito.when(clientServ.getClientById(1))
                .thenThrow(NotFoundException.class);



        mockMvc.perform(post("/location/updateClaim")
                        .param("locId", "1")
                        .param("locClaim", "true")
                        .param("clientId", "1")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();

    }

    /**
     * Test case: Unauth token on Update Claim.
     * Expected Result: HTTP Status Forbidden.
     */

    @Test
    void locationUpdateClaimUnauthToken() throws Exception {



        Client client = new Client(1, "client 1", "client1@test.com", "1234");
        Client client2 = new Client(2, "client 2", "client2@test.com", "4321");

        Location location = new Location("Soho", "New York", COST_NUM, 1);
        location.setId(1);
        location.setClaim(true);
        Mockito.when(locServ.getLocById(1))
                .thenReturn(location);


        Mockito.when(clientServ.checkAuthTokenBlank("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234"))
                .thenReturn(client);
        Mockito.when(clientServ.getClientById(1))
                .thenReturn(client);


        Mockito.when(clientServ.checkAuthTokenBlank("4321"))
                .thenReturn("4321");
        Mockito.when(clientServ.decryptToken("4321"))
                .thenReturn("4321");
        Mockito.when(clientServ.getClientByAuth("4321"))
                .thenReturn(client2);
        Mockito.when(clientServ.getClientById(2))
                .thenReturn(client2);



        mockMvc.perform(post("/location/updateClaim")
                        .param("locId", "1")
                        .param("locClaim", "false")
                        .param("clientId", "2")
                        .param("clientAuthToken", "4321")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }


    /**
     * Test case: Unauth client id on Update Claim.
     * Expected Result: HTTP Status Forbidden.
     */

    @Test
    void locationUpdateClaimInvalidToken() throws Exception {



        Client client = new Client(1, "client 1", "client1@test.com", "1234");
        Client client2 = new Client(2, "client 2", "client2@test.com", "4321");

        Location location = new Location("Soho", "New York", COST_NUM, 1);
        location.setId(1);
        location.setClaim(true);
        Mockito.when(locServ.getLocById(1))
                .thenReturn(location);


        Mockito.when(clientServ.checkAuthTokenBlank("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234"))
                .thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234"))
                .thenReturn(client);
        Mockito.when(clientServ.getClientById(1))
                .thenReturn(client);

        Mockito.when(clientServ.checkAuthTokenBlank("4321"))
                .thenReturn("4321");
        Mockito.when(clientServ.decryptToken("4321"))
                .thenReturn("4321");
        Mockito.when(clientServ.getClientByAuth("4321"))
                .thenReturn(client2);
        Mockito.when(clientServ.getClientById(2))
                .thenReturn(client2);



        mockMvc.perform(post("/location/updateClaim")
                        .param("locId", "1")
                        .param("locClaim", "false")
                        .param("clientId", "1")
                        .param("clientAuthToken", "4321")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }



}
