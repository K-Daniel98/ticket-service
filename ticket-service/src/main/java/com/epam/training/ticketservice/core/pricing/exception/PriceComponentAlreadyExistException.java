package com.epam.training.ticketservice.core.pricing.exception;

public class PriceComponentAlreadyExistException extends RuntimeException {

    public PriceComponentAlreadyExistException(String name) {
        super(String.format("Price component '%s' already exists", name));
    }

}
