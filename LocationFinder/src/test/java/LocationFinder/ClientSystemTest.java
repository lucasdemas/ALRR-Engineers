package LocationFinder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import org.mockito.Mockito;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;

import LocationFinder.repositories.ClientRepository;
import LocationFinder.services.ClientService;
import LocationFinder.controllers.ClientController;
import LocationFinder.models.Client;

import LocationFinder.exceptions.NotFoundException;
import LocationFinder.exceptions.InvaildInputException;




@WebMvcTest(ClientController.class)
public class ClientSystemTest {

    /**
     * An instance of the mockMvc.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * An instance of the client service.
     */
    @MockBean
    private ClientService clientServ;


    /**
     * An instance of the client repository.
     */
    @MockBean
    private ClientRepository clientRepo;


    /**
     * Test case: Success Client Authentication
     * Expected Result: return the correct client id.
     */
    @Test
    void clientAuthSuccess() throws Exception {
        Client client = new Client(1, "client 1", "client1@test.com", "1234");


        Mockito.when(clientServ.checkAuthTokenBlank("1234")).thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234")).thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234")).thenReturn(client);


        String content = (mockMvc.perform(get("/client/authenticate")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn())
                .getResponse().getContentAsString();


        assertEquals(content, "1");
    }

    /**
     * Test case: Failed Client Authentication
     * Scenario: Client Auth token does not exist.
     * Expected Result: HTTP Status Not found
     */
    @Test
    void clientAuthFail() throws Exception {

        Mockito.when(clientServ.decryptToken("1234"))
                .thenReturn("1234");

        Mockito.when(clientServ.getClientByAuth("1234"))
                .thenThrow(NotFoundException.class);


        mockMvc.perform(get("/client/authenticate")
                        .param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());


    }

    /**
     * Test case: Blank Authentication token
     * Scenario: Client Auth token does not exist.
     * Expected Result: HTTP Status Unprocessable Entity.
     */
    @Test
    void invalidAuthToken() throws Exception {


        Mockito.when(clientServ.checkAuthTokenBlank(""))
                .thenThrow(InvaildInputException.class);

        mockMvc.perform(get("/client/authenticate")
                        .param("clientAuthToken", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());


    }

    /**
     * Test case: Missing Params on Authentication
     * Expected Result: HTTP Status Bad Request.
     */

    @Test
    void missingInputTest() throws Exception {


        mockMvc.perform(get("/client/authenticate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());


    }

}

