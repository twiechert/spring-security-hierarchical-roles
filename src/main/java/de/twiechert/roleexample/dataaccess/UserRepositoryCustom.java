package de.twiechert.roleexample.dataaccess;

import de.twiechert.roleexample.model.User;

/**
 * @author Tayfun Wiechert <tayfun.wiechert@gmail.com>
 */
public interface UserRepositoryCustom {
    User saveAndEncryptPassword(User user);

}
