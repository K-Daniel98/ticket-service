package com.epam.training.ticketservice.core.booking.model;

import com.epam.training.ticketservice.configuration.ApplicationConfiguration;
import com.epam.training.ticketservice.core.screening.model.Screening;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Screening screening;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<BookedSeat> seats;

    private long price;

    public Booking(Screening screening, Set<BookedSeat> seats, long price) {
        this.screening = screening;
        this.seats = seats;
        this.price = price;
    }

    protected Booking() {
    }

    public Screening getScreening() {
        return screening;
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
    }

    public Set<BookedSeat> getSeats() {
        return seats;
    }

    public void setSeats(Set<BookedSeat> seats) {
        this.seats = seats;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    @Override
    public String toString() {
        var seatsStr = seats.stream().map(BookedSeat::toString).collect(Collectors.joining(", "));

        return String.format("Seats %s on %s in room %s starting at %s for %d HUF",
            seatsStr,
            screening.getMovie().getName(),
            screening.getRoom().getName(),
            ApplicationConfiguration.formatter.format(screening.getScreeningTime()),
            price);
    }

}
