package de.twiechert.roleexample.test.security.testutil;

import de.twiechert.roleexample.Application;
import de.twiechert.roleexample.configuration.ApplicationConfiguration;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import de.twiechert.roleexample.test.security.Initialization;

import static org.junit.Assert.assertEquals;

/**
 * @author Tayfun Wiechert <tayfun.wiechert@gmail.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@ActiveProfiles("test")
@WebIntegrationTest
public class RestServiceTest {

    @Autowired
    protected ApplicationConfiguration applicationConfiguration;

    private final static Logger logger = (Logger) LoggerFactory
            .getLogger(RestServiceTest.class);

    protected String API_BASEPATH;
    protected String APPLICATION_BASEPATH;

    protected SecuredRestClient adminRestTemplate = SecuredRestClient.withBasicAuthCredentials(Initialization.ADMIN_MAIL, "171291");

    protected SecuredRestClient internalEmployeeRestTemplate = SecuredRestClient.withBasicAuthCredentials(Initialization.INTERNAL_EMPLOYEE_MAIL, "171291");

    protected SecuredRestClient externalEmployeeRestTemplate = SecuredRestClient.withBasicAuthCredentials(Initialization.EXTERNAL_EMPLOYEE_MAIL, "171291");

    protected RestTemplate defaultRestTemplate = new TestRestTemplate();

    @Before
    public void setUp() {
        APPLICATION_BASEPATH = "http://localhost:" + applicationConfiguration.getApplicationHttpPort();
        API_BASEPATH = APPLICATION_BASEPATH + applicationConfiguration.getRestBasePath();
    }

    protected void testGet(RestTemplate providedRestTemplate, String path, HttpStatus expextedStatus) {
        try {
            ResponseEntity<Object> getResponse = providedRestTemplate.getForEntity(API_BASEPATH + path, Object.class);
            logger.debug("Status code was {}", getResponse.getStatusCode());
            assertEquals(getResponse.getStatusCode(), expextedStatus);
        } catch (HttpClientErrorException e) {
            assertEquals(expextedStatus, e.getStatusCode());
        }


    }

    protected void testSuccessGet(RestTemplate providedRestTemplate, String path) {
        this.testGet(providedRestTemplate, path, HttpStatus.OK);
    }

}
