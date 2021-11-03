package com.epam.training.ticketservice.core.room.exception;

public class RoomAlreadyExistException extends RuntimeException {

    public RoomAlreadyExistException(String name) {
        super(String.format("A room with name '%s' already exist", name));
    }

}
