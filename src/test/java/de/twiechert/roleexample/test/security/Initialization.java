package de.twiechert.roleexample.test.security;

import de.twiechert.roleexample.Application;
import de.twiechert.roleexample.dataaccess.UserRepository;
import de.twiechert.roleexample.model.User;
import de.twiechert.roleexample.security.Role;
import de.twiechert.roleexample.util.JsonUtil;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

/**
 * @author Tayfun Wiechert <tayfun.wiechert@gmail.com>
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Initialization {

    private final static Logger logger = (Logger) LoggerFactory
            .getLogger(Initialization.class);

    public static final String ADMIN_MAIL = "admin@system.de";

    public static final String INTERNAL_EMPLOYEE_MAIL = "intenral@system.de";

    public static final String EXTERNAL_EMPLOYEE_MAIL = "external@system.de";


    @Autowired
    UserRepository userDao;


    @Test
    public void a1_initialize() {
        this.createAndPersistUser();
    }

    @Test
    public void a2_ckeckInitialization() {
        /**
         * Make sure that they are existent
         */

        Optional<User> user1 = userDao.findByEmailAddress(INTERNAL_EMPLOYEE_MAIL);
        logger.debug("User 1 is {}", JsonUtil.objectToString(user1.isPresent()?user1.get().getIdentifier():user1));

        Optional<User> user2 = userDao.findByEmailAddress(ADMIN_MAIL);
        logger.debug("User 2 is {}", JsonUtil.objectToString(user2.isPresent()?user2.get().getIdentifier():user2));

        Optional<User> user3 = userDao.findByEmailAddress(EXTERNAL_EMPLOYEE_MAIL);
        logger.debug("User 3 is {}", JsonUtil.objectToString(user3.isPresent()?user3.get().getIdentifier():user3));


        logger.debug("Checking if initialized users have been persisted and their passwords have been hashed.");

        assertTrue(user1.isPresent());
        assertTrue(user1.get().getHashedPassword() != null);
        assertTrue(user1.get().getPassword() == null);

        assertTrue(user2.isPresent());
        assertTrue(user2.get().getHashedPassword() != null);
        assertTrue(user2.get().getPassword() == null);

        assertTrue(user3.isPresent());
        assertTrue(user3.get().getHashedPassword() != null);
        assertTrue(user3.get().getPassword() == null);
    }

    public void createAndPersistUser() {
        /**
         * Initialization of user with simple employee role
         */
        User user1 = new User();
        user1.setPassword("171291");
        user1.setFirstname("Tayfun");
        user1.setLastname("Employee");
        user1.setEmailAddress(INTERNAL_EMPLOYEE_MAIL);
        user1.setRoles(new HashSet<Role>() {{
            this.add(Role.ROLE_INTERNAL_EMPLOYEE);
        }});

        /**
         * Initialization of user with admin role
         */
        User user2 = new User();
        user2.setPassword("171291");
        user2.setFirstname("Tayfun");
        user2.setLastname("Admin");
        user2.setEmailAddress(ADMIN_MAIL);
        user2.setRoles(new HashSet<Role>() {{
            this.add(Role.ROLE_ADMIN);
        }});

        /**
         * Initialization of user with external employee role
         */
        User user3 = new User();
        user3.setPassword("171291");
        user3.setFirstname("Tayfun");
        user3.setLastname("Employee");
        user3.setEmailAddress(EXTERNAL_EMPLOYEE_MAIL);
        user3.setRoles(new HashSet<Role>() {{
            this.add(Role.ROLE_EXTERNAL_EMPLOYEE);
        }});


        userDao.saveAndEncryptPassword(user1);
        userDao.saveAndEncryptPassword(user2);
        userDao.saveAndEncryptPassword(user3);
    }



    }