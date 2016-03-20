package de.twiechert.roleexample.test.security;

import de.twiechert.roleexample.controller.SampleController;
import de.twiechert.roleexample.test.security.testutil.RestServiceTest;
import de.twiechert.roleexample.test.security.testutil.SecuredRestClient;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;

/**
 * @author Tayfun Wiechert <tayfun.wiechert@gmail.com>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccessControlTest extends RestServiceTest {

    private final static Logger logger = (Logger) LoggerFactory
            .getLogger(AccessControlTest.class);

    private static final String FIND_OWN_USER_PATH = "/users/search/findOwnUser";
    private static final String ALL_USER_PATH = "/users";

    @Test
    public void testAccessOfOwnUser() {
        testSuccessGet(adminRestTemplate, FIND_OWN_USER_PATH);
        testSuccessGet(internalEmployeeRestTemplate, FIND_OWN_USER_PATH);
        testSuccessGet(externalEmployeeRestTemplate, FIND_OWN_USER_PATH);
        testGet(defaultRestTemplate, FIND_OWN_USER_PATH, HttpStatus.UNAUTHORIZED);

    }

    @Test
    public void testAccessOfAllUser() {
        testSuccessGet(adminRestTemplate, ALL_USER_PATH);
        testGet(internalEmployeeRestTemplate, ALL_USER_PATH, HttpStatus.FORBIDDEN);
        testGet(externalEmployeeRestTemplate, ALL_USER_PATH, HttpStatus.FORBIDDEN);
        testGet(defaultRestTemplate, FIND_OWN_USER_PATH, HttpStatus.UNAUTHORIZED);

    }


    @Test
    public void testHeartbeatAccessFromAdmin() {
        // uses admin role --> tests if role hierachy works
        testSuccessGet(adminRestTemplate, SampleController.ADMIN_HEARTBEAT);
        testSuccessGet(adminRestTemplate, SampleController.ANY_HEARTBEAT);
        testSuccessGet(adminRestTemplate, SampleController.INTERNAL_HEARTBEAT);
    }

    @Test
    public void testHeartbeatAccessFromInternalEmployee() {
        testSuccessGet(internalEmployeeRestTemplate, SampleController.ANY_HEARTBEAT);
        testSuccessGet(internalEmployeeRestTemplate, SampleController.INTERNAL_HEARTBEAT);
    }

    @Test
    public void testHeartbeatAccessFromExternalEmployee() {
        testSuccessGet(externalEmployeeRestTemplate, SampleController.ANY_HEARTBEAT);
    }


    @Test
    public void testDeletionFromNonAdmin() {
        try {
            ResponseEntity deleteResponse = internalEmployeeRestTemplate.exchange(API_BASEPATH + SampleController.DELETE_ACTION, HttpMethod.DELETE, null, Object.class);

            if (deleteResponse.getStatusCode() != HttpStatus.UNAUTHORIZED) {
                fail("Provided credentials of user with insufficient role, but able to access protected resource.");
                logger.debug("Status code was {}", deleteResponse.getStatusCode());

            }
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.FORBIDDEN, e.getStatusCode());
        }

    }

    @Test
    public void testUserDeletionWithInsufficientRole() {
        try {
            ResponseEntity deleteResponse = internalEmployeeRestTemplate.exchange(API_BASEPATH + "/users/1", HttpMethod.DELETE, null, Object.class);

            if (deleteResponse.getStatusCode() != HttpStatus.FORBIDDEN) {
                fail("Provided credentials of user with insufficient role, but able to access protected resource.");
                logger.debug("Status code was {}", deleteResponse.getStatusCode());

            }
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.FORBIDDEN, e.getStatusCode());
        }

    }




}