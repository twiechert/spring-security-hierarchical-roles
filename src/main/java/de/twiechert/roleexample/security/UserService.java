package de.twiechert.roleexample.security;

import de.twiechert.roleexample.dataaccess.UserRepository;
import de.twiechert.roleexample.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Tayfun Wiechert <tayfun.wiechert@gmail.com>
 */

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userDao;

    @Autowired
    public UserService(UserRepository userDao) {
        this.userDao = userDao;
    }

    @Override
    public SecureUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.findByEmailAddress(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email=%s was not found", email)));
        SecureUser secureUser = new SecureUser(user);
        return secureUser;
    }
}
