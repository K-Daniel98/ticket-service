package com.epam.training.ticketservice.core.booking.model;

import com.epam.training.ticketservice.configuration.ApplicationConfiguration;
import com.epam.training.ticketservice.core.screening.model.Screening;
import com.epam.training.ticketservice.core.user.model.User;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Screening screening;

    @ManyToOne
    private User user;

    private long price;

    private int rowNumber;

    private int columnNumber;

    public Booking(Screening screening, User user, int rowNumber, int columnNumber, long price) {
        this.screening = screening;
        this.user = user;
        this.price = price;
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
    }

    protected Booking() {}

    public Screening getScreening() {
        return screening;
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Booking booking = (Booking) o;
        return rowNumber == booking.rowNumber && columnNumber == booking.columnNumber &&
            screening.equals(booking.screening);
    }

    @Override
    public int hashCode() {
        return Objects.hash(screening, rowNumber, columnNumber);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", rowNumber, columnNumber);
    }

}
