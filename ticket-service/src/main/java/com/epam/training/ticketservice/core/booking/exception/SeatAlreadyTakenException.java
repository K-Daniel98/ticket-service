package com.epam.training.ticketservice.core.booking.exception;

import com.epam.training.ticketservice.core.booking.model.Booking;

public class SeatAlreadyTakenException extends RuntimeException {

    public SeatAlreadyTakenException(Booking booking) {
        super(String.format("Seat %s is already taken", booking));
    }

}
