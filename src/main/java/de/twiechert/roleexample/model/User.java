package de.twiechert.roleexample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.twiechert.roleexample.security.Role;
import org.hibernate.validator.constraints.Email;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author Tayfun Wiechert <tayfun.wiechert@gmail.com>
 */
@Entity
public class User {


    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long identifier;

    @Column(nullable = false, unique = true)
    @Email
    private String emailAddress;

    private String firstname;

    private String lastname;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany
            //(fetch = FetchType.LAZY, mappedBy = "reservingUser")
    private Set<Reservation> reservations;

    @RestResource(exported = false)
    private String hashedPassword;

    @NotNull
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = Role.class)
    @CollectionTable //(name = "SYSTEM_USER_ROLES", joinColumns = @JoinColumn(name = "F_USER_ID"))
    @Enumerated(EnumType.ORDINAL)
    private Set<Role> roles;

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
