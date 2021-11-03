package com.epam.training.ticketservice.core.booking.exception;

import com.epam.training.ticketservice.core.booking.model.BookedSeat;

public class SeatAlreadyTakenException extends RuntimeException {

    public SeatAlreadyTakenException(BookedSeat seat) {
        super(String.format("Seat %s is already taken", seat));
    }

}
