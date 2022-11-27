package LocationFinder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import LocationFinder.controllers.ClientController;
import LocationFinder.controllers.LocationController;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.jupiter.api.function.Executable;
import org.springframework.web.bind.MissingServletRequestParameterException;

@RunWith(SpringRunner.class)
@SpringBootTest
class EndToEndTest {
    /**
     * An instance of the client controller.
     */
    @Autowired
    private ClientController clientCont;

    /**
     * An instance of the location controller.
     */
    @Autowired
    private LocationController locCont;

    //can't test this as it won't compile when missing parameters
//    /**
//     * A test to show what happens when a client controller entrypoint is missing a parameter
//     */
//    @Test
//    public void testMissingEntryClientCont() {
//        assertThrows(MissingServletRequestParameterException.class,
//         new Executable() {
//            @Override
//            public void execute() throws Throwable {
//                clientCont.authenticateClient();
//            }
//        });
//    }

    @Test
    public void testMissingEntryClientCont() {
        String authToken = "FoEv6p4c6r2ilAvRkRH39eub9LF9J+7GdoA7vnZK/j7djqRN3F" +
                "IACngZuYjVeicz0jXtxkXgG3mgUEi0psES5OD687udbMBIy0TE/WBkURdWP/4O" +
                "Mep0XWgVvTg9RmblWb9BjzrWsv9LAMjeb5x4zcRI0QDXhqcEuWuLaJkgUOPA3y/" +
                "2/XzKTnIzbk5Jrccd8bTUSupUNmNjRw2WTrLYROGi/wvRUyW2zhYMIuy2fbzKra" +
                "3jClYkbYpBja9ZXWfnvgx3bAdaznWPF99aAOuS0gMBDmRhzjG4a+2L2lHrw6U8y" +
                "MNN2p9jz4Iy7e7Ky9QI5Hrjyrqwda6CfKmsbpxiuw==";
        ResponseEntity<?> clientId = clientCont.authenticateClient(authToken);
        System.out.println(clientId);
        System.out.println(clientId.getBody());

        //Make sure the value returned is correct and that the response code is the correct one
        assertEquals(clientId.getStatusCode(), HttpStatus.Forbidden);
        assertEquals(clientId.getBody(), 1);
    }
}