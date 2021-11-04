package com.epam.training.ticketservice.core.user.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String name) {
        super(String.format("User with name '%s' already exists", name));
    }

}
