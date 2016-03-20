package de.twiechert.roleexample.dataaccess;

import de.twiechert.roleexample.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Tayfun Wiechert <tayfun.wiechert@gmail.com>
 */
public class UserRepositoryImpl implements UserRepositoryCustom {


    @Autowired
    private UserRepository userDao;


    @Override
    public User saveAndEncryptPassword(User user) {
        user.setHashedPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userDao.save(user);
    }

}
