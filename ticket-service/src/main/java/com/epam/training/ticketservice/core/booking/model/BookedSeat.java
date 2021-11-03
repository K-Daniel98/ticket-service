package com.epam.training.ticketservice.core.booking.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class BookedSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int rowNumber;
    private int columnNumber;

    @ManyToOne
    private Booking booking;

    public BookedSeat(int rowNumber, int columnNumber) {
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
    }

    protected BookedSeat() {
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookedSeat seat = (BookedSeat) o;
        return rowNumber == seat.rowNumber && columnNumber == seat.columnNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowNumber, columnNumber);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", rowNumber, columnNumber);
    }
}