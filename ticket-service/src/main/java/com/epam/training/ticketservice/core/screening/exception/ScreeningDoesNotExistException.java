package com.epam.training.ticketservice.core.screening.exception;

public class ScreeningDoesNotExistException extends RuntimeException {

    public ScreeningDoesNotExistException() {
        super("Screening does not exist");
    }

}
