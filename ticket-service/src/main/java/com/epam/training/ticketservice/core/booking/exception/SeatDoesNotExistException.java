package com.epam.training.ticketservice.core.booking.exception;

import com.epam.training.ticketservice.core.booking.model.BookedSeat;

public class SeatDoesNotExistException extends RuntimeException {

    public SeatDoesNotExistException(BookedSeat seat) {
        super(String.format("Seat %s does not exist in this room", seat));
    }

}
