package de.twiechert.roleexample.test.security;

import de.twiechert.roleexample.Application;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Tayfun Wiechert <tayfun.wiechert@gmail.com>
 */
@SpringApplicationConfiguration(Application.class)
@RunWith(Suite.class)
@WebIntegrationTest
@ActiveProfiles("test")
@Suite.SuiteClasses({Initialization.class, AccessControlTest.class})
public class SecurityTestSuite {
}
