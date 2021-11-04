package com.epam.training.ticketservice.core.screening.exception;

public class ScreeningOverlapException extends RuntimeException {

    public ScreeningOverlapException() {
        super("There is an overlapping screening");
    }

}
