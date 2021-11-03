package com.epam.training.ticketservice.core.room.exception;

public class RoomDoesNotExistException extends RuntimeException {
    public RoomDoesNotExistException(String room) {
        super(String.format("Room '%s' does not exist", room));
    }
}
