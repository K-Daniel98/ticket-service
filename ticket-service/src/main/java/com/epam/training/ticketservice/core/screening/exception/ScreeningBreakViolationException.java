package com.epam.training.ticketservice.core.screening.exception;

public class ScreeningBreakViolationException extends RuntimeException {

    public ScreeningBreakViolationException() {
        super("This would start in the break period after another screening in this room");
    }

}
