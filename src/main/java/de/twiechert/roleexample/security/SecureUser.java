package de.twiechert.roleexample.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.twiechert.roleexample.model.User;
import de.twiechert.roleexample.util.Adapter;
import de.twiechert.roleexample.util.CollectionUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

/**
 * @author Tayfun Wiechert <tayfun.wiechert@gmail.com>
 */

public class SecureUser extends org.springframework.security.core.userdetails.User {


    @JsonIgnore
    private User user;

    public SecureUser(User user) {
        super(user.getEmailAddress(), user.getHashedPassword(), CollectionUtil.anyCollectionToGenericCollection(user.getRoles(), (Adapter<Role, GrantedAuthority>) systemRole -> new SimpleGrantedAuthority(systemRole.toString())));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getIdentifier() {
        return user.getIdentifier();
    }

    public Set<Role> getRoles() {
        return user.getRoles();
    }

    public String getEmailAddress() {
        return user.getEmailAddress();
    }


}