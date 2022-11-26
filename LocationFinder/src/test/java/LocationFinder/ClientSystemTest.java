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

import LocationFinder.repositories.ClientRepository;
import LocationFinder.services.ClientService;
import LocationFinder.controllers.ClientController;
import LocationFinder.models.Client;

import LocationFinder.exceptions.NotFoundException;
import LocationFinder.exceptions.InvaildInputException;



@WebMvcTest(ClientController.class)
public class ClientSystemTest {
    @Autowired
    MockMvc mockMvc;

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


    @Test
    void clientAuthSuccess() throws Exception {
        Client client = new Client(1, "client 1", "client1@test.com", "1234");



        Mockito.when(clientServ.checkAuthTokenBlank("1234")).thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234")).thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234")).thenReturn(client);


        String content = (mockMvc.perform(get("/client/authenticate").param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn()).getResponse().getContentAsString();

        //String content = result.getResponse().getContentAsString();
        assertEquals(content, "1");
    }

    @Test
    void clientAuthFail() throws Exception {


        Mockito.when(clientServ.checkAuthTokenBlank("1234")).thenReturn("1234");
        Mockito.when(clientServ.decryptToken("1234")).thenReturn("1234");
        Mockito.when(clientServ.getClientByAuth("1234")).thenThrow(NotFoundException.class);

       mockMvc.perform(get("/client/authenticate").param("clientAuthToken", "1234")
                        .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());



    }

    @Test
    void InvalidAuthToken() throws Exception {


        Mockito.when(clientServ.checkAuthTokenBlank("")).thenThrow(InvaildInputException.class);

        mockMvc.perform(get("/client/authenticate").param("clientAuthToken", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());



    }

    @Test
    void MissingInputTest() throws Exception {


        mockMvc.perform(get("/client/authenticate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());


    }





//    @Test
//    public void clientAuth_success() throws Exception {
//        Client client = Client.builder()
//                .id(1)
//                .name("client 1")
//                .email("client1@test.com")
//                .authToken("FoEv6p4c6r2ilAvRkRH39eub9LF9J+7GdoA7vnZK/j7djqRN3F" +
//                        "IACngZuYjVeicz0jXtxkXgG3mgUEi0psES5OD687udbMBIy0TE/WBkURdWP/4O" +
//                        "Mep0XWgVvTg9RmblWb9BjzrWsv9LAMjeb5x4zcRI0QDXhqcEuWuLaJkgUOPA3y/" +
//                        "2/XzKTnIzbk5Jrccd8bTUSupUNmNjRw2WTrLYROGi/wvRUyW2zhYMIuy2fbzKra" +
//                        "3jClYkbYpBja9ZXWfnvgx3bAdaznWPF99aAOuS0gMBDmRhzjG4a+2L2lHrw6U8y" +
//                        "MNN2p9jz4Iy7e7Ky9QI5Hrjyrqwda6CfKmsbpxiuw==")
//                .build();
//
//        Mockito.when(clientRepo.save(client)).thenReturn(client);
//
//        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/client/authenticate")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(this.mapper.writeValueAsString(client));
//
//        mockMvc.perform(mockRequest)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", notNullValue()))
//                .andExpect(jsonPath("$.id", is(1)));
//    }
}