package com.epam.training.ticketservice.core.booking.exception;

import com.epam.training.ticketservice.core.booking.model.Booking;

public class SeatDoesNotExistException extends RuntimeException {

    public SeatDoesNotExistException(Booking booking) {
        super(String.format("Seat %s does not exist in this room", booking));
    }

}
