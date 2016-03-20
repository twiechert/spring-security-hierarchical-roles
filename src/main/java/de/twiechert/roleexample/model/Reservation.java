package de.twiechert.roleexample.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Tayfun Wiechert <tayfun.wiechert@gmail.com>
 */
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long identifier;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date departureTime;

    @Column
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date arrivalTime;

    @ManyToOne(optional = false)
    @JoinColumn
    private User reservingUser;

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public User getReservingUser() {
        return reservingUser;
    }

    public void setReservingUser(User reservingUser) {
        this.reservingUser = reservingUser;
    }
}
