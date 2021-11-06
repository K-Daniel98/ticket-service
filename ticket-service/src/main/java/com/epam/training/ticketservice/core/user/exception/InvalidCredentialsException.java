package com.epam.training.ticketservice.core.user.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Login failed due to incorrect credentials");
    }

}
